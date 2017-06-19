/**
 * 
 */
package com.itisneat.wallet;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.parity.Parity;

import rx.Observer;

/**
 * @author leo
 *
 */
public class MyPendingTransactionObserverForTest implements Observer<Transaction> {
	
	private TXHashHolder txHashHodler;
	private Parity parity;
	private BigInteger startBlockNum;
	private static int count = 100;
	boolean handled = false;
	
	public MyPendingTransactionObserverForTest(Parity parity, TXHashHolder hodler) {
		
	}

	@Override
	public void onCompleted() {
		System.out.println("Transaction observer completed!");
	}

	@Override
	public void onError(Throwable e) {
		System.out.println("Transaction observer On error! err: " + e.getMessage());
		e.printStackTrace();
	}

	@Override
	public void onNext(Transaction tx) {
		if (--count == 0 && !handled) {
			System.out.println("start handle tx: " + tx.getHash() + "------" + tx.getBlockNumberRaw());
//			parity.
		}
		
	}

}
