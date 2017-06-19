/**
 * 
 */
package com.itisneat.wallet;

import org.web3j.protocol.core.methods.response.Transaction;

import rx.Observer;

/**
 * @author leo
 *
 */
public class MyTransactionObserver implements Observer<Transaction> {
	
//	Boolean stopFlag = false;
	
//	EthBlock block = null;
	
//	String exitMsg;

//	/* (non-Javadoc)
//	 * @see java.lang.Runnable#run()
//	 */
//	@Override
//	public void run() {
//		synchronized (stopFlag) {
//			while (!stopFlag) {
//				try {
//					stopFlag.wait(20000);
//					
//					if (stopFlag) {
//						System.out.println("TransacionObserver be stopped: msg: [" + exitMsg + "]");
//						return;
//					}
//					
//					
//				} catch (InterruptedException e) {
//					System.out.println(e.getMessage());
//				}
//			}
//		}
//		
//
//	}

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
		System.out.println(tx.getHash() + "------" + tx.getBlockNumberRaw());
//			System.out.println(block.getBlock().getNumber());
	}

}
