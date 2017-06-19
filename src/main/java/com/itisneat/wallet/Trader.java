/**
 * 
 */
package com.itisneat.wallet;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;

import com.itisneat.wallet.parity.ParityClient;
import com.itisneat.wallet.wrap.MyParity;
import com.itisneat.wallet.wrap.MyTransaction;

import rx.Observable;

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
		try {
			EthTransaction etx = parity.ethGetTransactionByHash("0x82f2afa33ec1a71b8be53ae51b603ae387473ae2e615990a59ddfaed86289dba").sendAsync().get();
			if (etx.hasError()) {
				System.out.println("failed get transaction, msg: " + etx.getError().getMessage());
			} else {
				System.out.println(etx.getTransaction().get().getBlockNumber());
				System.out.println(etx.getTransaction().get().getInput());
				System.out.println(etx.getTransaction().get().getV());
				System.out.println(etx.getTransaction().get().getR());
				System.out.println(etx.getTransaction().get().getS());
				System.out.println(etx.getTransaction().get().getBlockNumberRaw());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		BigInteger value = Convert.toWei("0.012", Convert.Unit.ETHER).toBigInteger();
//		doTrans("0xa59A166B4df37Cfc72","iin62", "0x00bD71cdF69aE874a1",
//				value, new BigInteger("894012500"), new BigInteger("21000"), new BigInteger("3893405"));
//	}
	

	public static boolean doTrans(String accountId, String passsword, String toAccountId, BigInteger amount,
							   BigInteger gasPrice, BigInteger gasLimit, BigInteger block){
		String transHash;
		try {
			EthGetTransactionCount ethGetTransactionCount = parity.ethGetTransactionCount(accountId,  DefaultBlockParameterName.LATEST).sendAsync().get();
			if (ethGetTransactionCount.hasError()) {
				System.out.println("faild to get transaction count, msg: " + ethGetTransactionCount.getError().getMessage());
				return false;
			}
			
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();
			
			Transaction trans = new MyTransaction(accountId, nonce, gasPrice, gasLimit, toAccountId,
					amount, null, block, null);
			EthSendTransaction sendResult = parity.personalSendTransaction(trans, "pwd").send();
			if (sendResult.hasError()) {
				System.out.print("failed to send transaction, msg: " + sendResult.getError().getMessage());
			} else {
				transHash = sendResult.getTransactionHash();
				System.out.print("sucessfully, transhash is " + transHash);
//					parity.ethGetBlockTransactionCountByHash()
//					EthTransaction ethTrans = parity.ethGetTransactionByHash(transHash).send();
//					ethTrans.getTransaction().isPresent()
			}
			
//			PersonalUnlockAccount unlockResult =  parity.personalUnlockAccount(accountId, passsword, null).send();
//			if (unlockResult.hasError()) {
//                System.out.print("failed unlock account, msg: " + unlockResult.getError().getMessage());
//            } else {
//			}


		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	



}
