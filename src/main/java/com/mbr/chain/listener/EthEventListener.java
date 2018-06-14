package com.mbr.chain.listener;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.utils.Numeric;

import com.alibaba.fastjson.JSONObject;
import com.mbr.chain.common.utils.AddressUtil;
import com.mbr.chain.common.web3j.Web3jConnector;
import com.mbr.chain.domain.TransactionStatus;
import com.mbr.chain.domain.bo.BlockObservable;
import com.mbr.chain.domain.bo.ERC20TransferEventRecord;
import com.mbr.chain.domain.bo.EthTransaction;
import com.mbr.chain.manager.BlockNumManager;
import com.mbr.chain.manager.BlockObservableManager;
import com.mbr.chain.manager.EthTransactionManager;

import rx.Subscription;

public class EthEventListener {

	private Subscription pendingTransactionSubscription;
	private Subscription transactionSubscription;
	private Subscription blockSubscription;
	private Subscription erc20Subscription;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EthTransactionManager ethTransactionManager;

	@Autowired
	private BlockObservableManager blockObservableManager;

	@Autowired
	private BlockNumManager blockNumManager;

	private static String formt = "yyyy-MM-dd HH:mm:ss";

	private String erc20TransferEventTopic = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
	@Autowired
	private Web3jConnector web3jConnector;

	public void destroy() {
		try {
			if (pendingTransactionSubscription != null) {
				pendingTransactionSubscription.unsubscribe();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (transactionSubscription != null) {
				transactionSubscription.unsubscribe();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (blockSubscription != null) {
				blockSubscription.unsubscribe();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (erc20Subscription != null) {
				erc20Subscription.unsubscribe();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		transactionPendingListener();
		transactionTxListener();
		blockListener();
		erc20Listener();
	}

	// 接受所有已经提交到网络中的等待处理的交易。(它们必须分在同一个区块中。)
	public void transactionPendingListener() {
		try {
			pendingTransactionSubscription = web3jConnector.connector().pendingTransactionObservable()
				.subscribe(pending -> {// onNext
					logger.debug("pending Hash：->{}", pending.getHash());
					EthTransaction pendingEthTransaction = new EthTransaction();
					BeanUtils.copyProperties(pending, pendingEthTransaction);
					pendingEthTransaction.setCreateTime(DateFormatUtils.format(new Date(), formt));
					pendingEthTransaction.setStatus(0);
					pendingEthTransaction.setTxStatus(TransactionStatus.TRANSACTION_PENDING);
					pendingEthTransaction.setValue(pending.getValue().toString());
					ethTransactionManager.save(pendingEthTransaction);
				}, err -> {// onError
					if (!(err instanceof NoSuchElementException)){
						logger.warn("pending is error", err);
					}
					transactionPendingListener();
				}, () -> {// onCompleted
							// logger.info("pending is completed");
				});
		}catch (Exception e){
			logger.error("pendingTransactionObservable   Exception - >{}",e.getMessage());
		}
	}

	// 接受所有新的交易并把它们添加到区块链中。
	public void transactionTxListener() {
		transactionSubscription = web3jConnector.connector().transactionObservable().subscribe(tx -> {

			logger.debug("tx Hash：->{}", tx.getHash());
			EthTransaction ethTransaction = new EthTransaction();
			BeanUtils.copyProperties(tx, ethTransaction);
			ethTransaction.setGasPrice(tx.getGasPrice().toString());
			ethTransaction.setGas(tx.getGas().toString());
			ethTransaction.setTransactionIndex(tx.getTransactionIndex().toString());
			ethTransaction.setBlockNumber(tx.getBlockNumber().toString());
			ethTransaction.setNonce(tx.getNonce().toString());
			ethTransaction.setValue(tx.getValue().toString());
			ethTransactionManager.updateEthTransaction(ethTransaction);
		}, err -> {
			// err.printStackTrace();
			logger.error("tx is error", err);
			transactionTxListener();
		}, () -> {
			// logger.info("tx is completed");
		});
	}

	// 接受所有新的区块
	public void blockListener() {
		blockSubscription = web3jConnector.connector().blockObservable(true).subscribe(ethBlock -> {
			Response.Error error = ethBlock.getError();
			if (error != null) {
				// logger.error(error.getMessage());
				return;
			}

			EthBlock.Block block = ethBlock.getBlock();
			// 线程添加区块到数据库
			new Thread(new Runnable() {
				@Override
				public void run() {
					blockNumManager.save(block.getNumberRaw());
				}
			}).start();

			if (block != null) {
				final AtomicInteger count = new AtomicInteger(12);
				BigInteger start = block.getNumber().subtract(new BigInteger(count.toString()));
				if (start.compareTo(BigInteger.ZERO) <= 0) {
					start = BigInteger.ZERO;
				}
				BlockObservable blockObservable = new BlockObservable();
				blockObservable.setStart(start);
				blockObservable.setEnd(block.getNumber());
				blockObservable.setNumber(block.getNumber());

				ethTransactionManager.replayBlocksObservable(blockObservable);
				// //添加到队列
				// try {
				// logger.info("LinkedBlockingQueue put
				// ->{}",JSONObject.toJSONString(blockObservable));
				// BlockObservableQueue.linkedBlockingQueue.put(blockObservable);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }

				// this.blockObservableManager.save(blockObservable);
			}
		}, err -> {
			// logger.info("<---------------------------接受所有新的区块异常-------------------->");
			blockListener();
		}, () -> {

		});
	}

	// 接受所有ERC20
	public void erc20Listener() {
		EthFilter ethFilter = new EthFilter();
		ethFilter.addSingleTopic(erc20TransferEventTopic);
		erc20Subscription = web3jConnector.connector().ethLogObservable(ethFilter).map(log -> {
			ERC20TransferEventRecord record = new ERC20TransferEventRecord();
			record.setBelongTransaction(log.getTransactionHash());
			if (log.getTopics() != null && log.getTopics().size() > 0) {
				try {
					record.setFrom(AddressUtil.toAddress(log.getTopics().get(1)));
					record.setTo(AddressUtil.toAddress(log.getTopics().get(2)));
				} catch (Exception e) {
					// e.printStackTrace();
					record.setFrom("");
					record.setTo("");
				}
			}
			record.setTokenAddress(AddressUtil.toAddress(log.getAddress()));
			if (isValidHexQuantity(log.getData())) {
				record.setValue(new BigDecimal(Numeric.decodeQuantity(log.getData())));
			}
			record.setBlockNumber(log.getBlockNumber());
			return record;
		}).subscribe(record -> {
			logger.debug("erc20内容为->{}", JSONObject.toJSONString(record));

			this.ethTransactionManager.saveErc20(record);
		}, err -> {
			logger.error("ERC20 is error", err);
			// logger.info("<---------------------------ERC20异常-------------------->");
			erc20Listener();
		}, () -> {

		});
	}

	private static boolean isValidHexQuantity(String value) {
		if (value == null) {
			return false;
		} else if (value.length() < 3) {
			return false;
		} else {
			return value.startsWith("0x");
		}
	}
}
