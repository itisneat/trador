/**
 * 
 */
package com.itisneat.wallet;

import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.parity.Parity;

import com.itisneat.wallet.parity.ParityClient;

import rx.Observable;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author leo
 *
 */
public class Trader {

	private static BigInteger nonce = new BigInteger("0");

	private static BigInteger gasPrice = new BigInteger("1");

	private static BigInteger gasLimit = new BigInteger("50");

	private static Parity parity = ParityClient.getParity();

	public String trasfer(String accountId,String passsword,String toAccountId, BigDecimal amount)  {

		String tradeHash = null;
		Transaction transaction = Transaction.createEtherTransaction(accountId,null,null,null,toAccountId,amount.toBigInteger());
		try{
			EthSendTransaction ethSendTransaction =parity.personalSignAndSendTransaction(transaction,passsword).send();
			if(ethSendTransaction!=null){
				tradeHash = ethSendTransaction.getTransactionHash();
				System.out.println(String.format("账户:[%s]转账到账户:[%s],交易hash:[%s]",accountId,toAccountId,tradeHash));
			}
		}catch (Exception e){
			System.out.println(String.format("账户:[%]交易失败!",accountId));
			e.printStackTrace();
		}
		return tradeHash;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Observable<EthBlock> blockObservable = parity.blockObservable(false);
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
