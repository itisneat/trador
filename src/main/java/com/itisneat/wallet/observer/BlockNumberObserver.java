/**
 * 
 */
package com.itisneat.wallet.observer;

import org.web3j.protocol.core.methods.response.EthBlockNumber;

import rx.Observer;

/**
 * @author leo
 *
 */
public class BlockNumberObserver implements Runnable, Observer<EthBlockNumber> {
	
	Boolean stopFlag = false;
	
	EthBlockNumber blockNumber = null;
	
	String exitMsg;

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		synchronized (stopFlag) {
			while (!stopFlag) {
				try {
					stopFlag.wait(20000);
					
					if (stopFlag) {
						System.out.println("BlockObserver be stopped: msg: [" + exitMsg + "]");
						return;
					}
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		

	}

	@Override
	public void onCompleted() {
		System.out.println("block number observer receive complete event");
		synchronized (stopFlag) {
			stopFlag = true;
			stopFlag.notify();
		}
	}

	@Override
	public void onError(Throwable e) {
		System.out.println("block number observer receive error event");
		synchronized (stopFlag) {
			stopFlag = true;
			System.out.println("on error: " + e.getMessage());
			stopFlag.notify();
		}
	}

	@Override
	public void onNext(EthBlockNumber t) {
		synchronized (stopFlag) {
			blockNumber = t;
			System.out.println(t.getBlockNumber());
			stopFlag.notify();
		}
	}

}
