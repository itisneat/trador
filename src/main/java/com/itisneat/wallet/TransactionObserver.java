package com.itisneat.wallet;

import org.web3j.protocol.core.methods.request.Transaction;

import com.itisneat.wallet.bean.TransactionInfo;
import rx.Observer;

/**
 * Created by leo on 2017/6/11.
 */
public class TransactionObserver implements Observer<Transaction> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onNext(Transaction transactionInfo) {
//        Trader trader = new Trader();
        //trader.trasfer(transactionInfo.getAccountId(), transactionInfo.getPasssword(), transactionInfo.getToAccountId(),transactionInfo.getAmount());
    }
}
