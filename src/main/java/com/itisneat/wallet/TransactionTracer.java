/**
 * 
 */
package com.itisneat.wallet;

import java.util.concurrent.ExecutionException;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.parity.Parity;

/**
 * @author leo
 *
 */
public class TransactionTracer implements Runnable {
	
	Parity parity;
	String txHash;
	
	public TransactionTracer(Parity parity, String hash) {
		this.parity = parity;
		this.txHash = hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		

	}
	
	private void doTrace() {
		try {
			EthTransaction tx = parity.ethGetTransactionByHash(txHash).sendAsync().get();
			if (tx.hasError()) {
				System.out.println("failed get transaction, msg: " + tx.getError().getMessage());
			} else {
				System.out.println(tx.toString());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
