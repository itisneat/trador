/**
 * 
 */
package com.itisneat.wallet.observer;

import org.web3j.protocol.core.methods.response.Transaction;

import com.itisneat.wallet.count.TxCountCenter;

import rx.Observable;
import rx.Observer;

/**
 * @author leo
 *
 */
public class MyPendingTransactionObserver implements Observer<Transaction> {
	
	String statusAddress = "0x55d34b686aa8C04921397c5807DB9ECEdba00a4c";
	
	TxCountCenter txCountCenter;
	Observable<org.web3j.protocol.core.methods.response.Transaction> txObservable;
	
	public MyPendingTransactionObserver(Observable<org.web3j.protocol.core.methods.response.Transaction> observable) {
		txCountCenter = TxCountCenter.getInstance();
		txObservable = observable;
	}

	@Override
	public void onCompleted() {
		System.out.println("Transaction observer completed!");
	}

	@Override
	public void onError(Throwable e) {
		System.out.println("Transaction observer On error! err: " + e.getMessage());
		this.txObservable.subscribe(this);
	}

	@Override
	public void onNext(Transaction tx) {
		if(statusAddress.equalsIgnoreCase(tx.getTo())) {
			txCountCenter.addNewPendingTx(tx.getHash());
		}
		
	}

}
