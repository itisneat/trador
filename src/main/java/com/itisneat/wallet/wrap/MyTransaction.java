package com.itisneat.wallet.wrap;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;

/**
 * Created by leo on 2017/6/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyTransaction extends Transaction{

    private TransCondition condition;

    public MyTransaction(String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data, BigInteger block, BigInteger time) {
        super(from, nonce, gasPrice, gasLimit, to, value, data);
        if(block != null) {
            this.condition = new TransCondition(block, true);
        } else if (time != null){
            this.condition = new TransCondition(time, false);
        }
    }

}
