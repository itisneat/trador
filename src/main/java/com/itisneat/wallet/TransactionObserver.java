package com.itisneat.wallet;

import com.itisneat.wallet.bean.TransactionInfo;
import rx.Observer;

/**
 * Created by leo on 2017/6/11.
 */
public class TransactionObserver implements Observer<TransactionInfo> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onNext(TransactionInfo transactionInfo) {
        Trader trader = new Trader();
        //trader.trasfer(transactionInfo.getAccountId(), transactionInfo.getPasssword(), transactionInfo.getToAccountId(),transactionInfo.getAmount());
    }
}
