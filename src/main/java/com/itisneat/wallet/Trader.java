/**
 * 
 */
package com.itisneat.wallet;

import com.itisneat.wallet.parity.ParityClient;
import com.itisneat.wallet.wrap.MyParity;
import com.itisneat.wallet.wrap.MyTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author leo
 *
 */
public class Trader {

//	private static BigInteger nonce = new BigInteger("0");
//
//	private static BigInteger gasPrice = new BigInteger("1");
//
//	private static BigInteger gasLimit = new BigInteger("50");

	private static MyParity parity = (MyParity) ParityClient.getParity();
//
//	public String trasfer(String accountId,String passsword,String toAccountId, BigDecimal amount)  {
//
//		String tradeHash = null;
//		Transaction transaction = Transaction.createEtherTransaction(accountId,null,null,null,toAccountId,amount.toBigInteger());
//		try{
//			EthSendTransaction ethSendTransaction =parity.personalSendTransaction(transaction,passsword).send();
//			if(ethSendTransaction!=null){
//				tradeHash = ethSendTransaction.getTransactionHash();
//				System.out.println(String.format("账户:[%s]转账到账户:[%s],交易hash:[%s]",accountId,toAccountId,tradeHash));
//			}
//		}catch (Exception e){
//			System.out.println(String.format("账户:[%]交易失败!",accountId));
//			e.printStackTrace();
//		}
//		return tradeHash;
//	}
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		Observable<EthBlock> blockObservable = parity.blockObservable(false);
//		BlockObserver observer = new BlockObserver();
//		blockObservable.subscribe(observer);
//		Thread task = new Thread(observer);
//		task.start();
//		try {
//			task.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static void main(String[] args) {
		BigInteger value = Convert.toWei("0.012", Convert.Unit.ETHER).toBigInteger();
		doTrans("0xa59A166B4df37Cfc728030E0d96E01AAe49c8927","iin62", "0x00bD71cdF69aE874a191848F89833eF48AB9FBE0",
				value, new BigInteger("894012500"), new BigInteger("21000"),new BigInteger("8"),
				new BigInteger("3893405"));
	}

	public static void doTrans(String accountId, String passsword, String toAccountId, BigInteger amount,
							   BigInteger gasPrice, BigInteger gasLimit, BigInteger nonce,
							   BigInteger block){
		String transHash;
		try {

//			PersonalUnlockAccount unlockResult =  parity.personalUnlockAccount(accountId, passsword, null).send();
//			if (unlockResult.hasError()) {
//                System.out.print("failed unlock account, msg: " + unlockResult.getError().getMessage());
//            } else {
				Transaction trans = new MyTransaction(accountId, nonce, gasPrice, gasLimit, toAccountId,
						amount, null, block, null);
				EthSendTransaction sendResult = parity.personalSendTransaction(trans).send();
				if (sendResult.hasError()) {
					System.out.print("failed to send transaction, msg: " + sendResult.getError().getMessage());
				} else {
					transHash = sendResult.getTransactionHash();
					System.out.print("sucessfully, transhash is " + transHash);
//					parity.ethGetBlockTransactionCountByHash()
//					EthTransaction ethTrans = parity.ethGetTransactionByHash(transHash).send();
//					ethTrans.getTransaction().isPresent()
				}
//			}


		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
