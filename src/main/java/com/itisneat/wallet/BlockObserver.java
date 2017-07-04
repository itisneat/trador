/**
 * 
 */
package com.itisneat.wallet;

import org.web3j.protocol.core.methods.response.EthBlock;

import rx.Observer;

/**
 * @author leo
 *
 */
public class BlockObserver implements Runnable, Observer<EthBlock> {
	
	Boolean stopFlag = false;
	
	EthBlock block = null;
	
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
					
					System.out.println(block.getBlock().getNumber());
					
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		

	}

	@Override
	public void onCompleted() {
		synchronized (block) {
			stopFlag = true;
			exitMsg = "complete";
			stopFlag.notify();
		}
	}

	@Override
	public void onError(Throwable e) {
		synchronized (stopFlag) {
			stopFlag = true;
			e.printStackTrace();
			exitMsg = "on error: " + e.getMessage();
			stopFlag.notify();
		}
	}

	@Override
	public void onNext(EthBlock t) {
		synchronized (stopFlag) {
			block = t;
			stopFlag.notify();
		}
	}

}
