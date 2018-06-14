import com.alibaba.fastjson.JSONObject;
import com.mbr.chain.ChainServiceApplication;
import com.mbr.chain.common.manager.ETHManager;
import com.mbr.chain.common.manager.TransactionManager;
import com.mbr.chain.domain.bo.EthOrder;
import com.mbr.chain.domain.bo.EthTransaction;
import com.mbr.chain.repository.EthOrderRepository;
import com.mbr.chain.repository.EthTransactionRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ChainServiceApplication.class)
public class Test {

 /*   @Autowired
    private ETHManager ethManager;

    @org.junit.Test
    public void test(){
        TransactionReceipt receiptErc20 = ethManager.getTransactionReceipt("0x70aa266948100bae923595ced2495d57a81017e77e93f1fa8e57455fc2787bfe");
        System.out.println("---------------->"+ JSONObject.toJSONString(receiptErc20));
    }*/

    public static void main(String[] args){


        String url = "http://13.113.193.21:8545";


//System.out.println("block num--> "+Web3j.build(new HttpService(url)).ethBlockNumber().send().getBlockNumber());
        Web3j web3j = Web3j.build(new HttpService(url));
        try {
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt("0x261cc74469997c73222a0c6330a19a22d4561766d8524c48664b3214094e2043").send();
            TransactionReceipt transactionReceipt = ethGetTransactionReceipt.getResult();
            System.out.println("---------------->"+ Numeric.decodeQuantity(transactionReceipt.getStatus()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
