package com.itisneat.wallet.count;

import java.math.BigInteger;

/**
 * Created by leo on 2017/6/19.
 */
public class TxInfo {
    private String txHash;
    private BigInteger startBlock;
    private BigInteger endBlock;
    private TxStatus txStatus;
    
    public enum TxStatus
    {
        pending, finish
    }

    public TxInfo(String txHash)
    {
        this.txHash = txHash;
    }

    public String getTxHash() {
        return txHash;
    }

    public BigInteger getStartBlock() {
        return startBlock;
    }

    public void setStartBlock(BigInteger startBlock) {
        this.startBlock = startBlock;
    }

    public BigInteger getEndBlock() {
        return endBlock;
    }

    public void setEndBlock(BigInteger endBlock) {
        this.endBlock = endBlock;
    }

    public TxStatus getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(TxStatus txStatus) {
        this.txStatus = txStatus;
    }
}



