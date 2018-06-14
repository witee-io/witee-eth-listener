package com.mbr.chain.domain;

public interface TransactionStatus {

    public static final int TRANSACTION_SEND = -1;//交易发送

    public static final int TRANSACTION_PENDING = 0;// 交易pending

    public static final int TRANSACTION_FAILED = 1;// 交易失败

    public static final int TRANSACTION_CONFIRMED = 2;// 交易确认

    public static final int TRANSACTION_SUCCESS = 3;//交易成功
}
