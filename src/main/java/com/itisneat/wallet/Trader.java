/**
 * 
 */
package com.itisneat.wallet;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.parity.Parity;

import com.itisneat.wallet.parity.ParityClient;

import rx.Observable;

/**
 * @author leo
 *
 */
public class Trader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Parity partity = ParityClient.getParity();
		Observable<EthBlock> blockObservable = partity.blockObservable(false);
		BlockObserver observer = new BlockObserver();
		blockObservable.subscribe(observer);
		Thread task = new Thread(observer);
		task.start();
		try {
			task.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
