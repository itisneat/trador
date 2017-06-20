/**
 * 
 */
package com.itisneat.wallet;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;

import com.itisneat.wallet.observer.MyBlockObserver;
import com.itisneat.wallet.observer.MyPendingTransactionObserver;
import com.itisneat.wallet.parity.ParityClient;
import com.itisneat.wallet.wrap.MyParity;
import com.itisneat.wallet.wrap.MyTransaction;

import rx.Observable;

/**
 * @author leo
 *
 */
public class Trader {

	private static MyParity parity = (MyParity) ParityClient.getParity();
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args == null || args.length == 0) {
			Observable<EthBlock> blockObservable = parity.blockObservable(true);
			MyBlockObserver blockObserver = new MyBlockObserver(blockObservable);
			blockObservable.subscribe(blockObserver);
			
			Observable<org.web3j.protocol.core.methods.response.Transaction> txObservable = parity.pendingTransactionObservable();
			MyPendingTransactionObserver transObserver = new MyPendingTransactionObserver(txObservable);
			txObservable.subscribe(transObserver);
		} else {
			transToStatus(args);
		}
		
		
	}
	
	static void transToStatus(String[] args) {
		String account = "";
		String pwd = "";
		String toAccount = "0x55d34b686aa8C04921397c5807DB9ECEdba00a4c";
		BigInteger block = new BigInteger("3903900");
		String gasL = "200000";
		String gasP = "50";
		List<AheadTx> aheadTxs = new ArrayList<>();
		for(String arg : args) {
			String[] p = arg.split("=");
			if ("account".equalsIgnoreCase(p[0])) {
				account = p[1];
			} else if("toAccount".equalsIgnoreCase(p[0])){
				toAccount = p[1];
			}
			else if("pwd".equalsIgnoreCase(p[0])) {
				pwd = p[1];
			} else if("block".equalsIgnoreCase(p[0])) {
				block = new BigInteger(p[1]);
			} else if("ahead".equalsIgnoreCase(p[0])) {
				aheadTxs.add(new AheadTx(p[1]));
			} else if("gasLimit".equalsIgnoreCase(p[0])) {
				gasL = p[1];
			} else if("gasPrice".equalsIgnoreCase(p[0])) {
				gasP = p[1];
			}
		}
		
		BigInteger gasPrice = Convert.toWei(gasP, Convert.Unit.GWEI).toBigInteger();
		BigInteger gasLimit = new BigInteger(gasL);
		int nonceOffset = 0;
		for(AheadTx t : aheadTxs) {
			doTrans(account, pwd, toAccount, t.getValue(), gasPrice, gasLimit, block.subtract(new BigInteger(t.getAheadBlock())), nonceOffset++);
		}
	}
	

//	public static void main(String[] args) {
//		BigInteger value = Convert.toWei("0.012", Convert.Unit.ETHER).toBigInteger();
//		doTrans("0xa59A166B4df37Cfc72","iin62", "0x00bD71cdF69aE874a1",
//				value, new BigInteger("894012500"), new BigInteger("21000"), new BigInteger("3893405"));
//	}
	

	public static boolean doTrans(String accountId, String passsword, String toAccountId, BigInteger amount,
							   BigInteger gasPrice, BigInteger gasLimit, BigInteger block, int nonceOffset){
		String transHash;
		try {
			EthGetTransactionCount ethGetTransactionCount = parity.ethGetTransactionCount(accountId,  DefaultBlockParameterName.LATEST).sendAsync().get();
			if (ethGetTransactionCount.hasError()) {
				System.out.println("faild to get transaction count, msg: " + ethGetTransactionCount.getError().getMessage());
				return false;
			}
			
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();
			nonce = nonce.add(new BigInteger(String.valueOf(nonceOffset)));
			
			Transaction trans = new MyTransaction(accountId, nonce, gasPrice, gasLimit, toAccountId,
					amount, null, block, null);
			EthSendTransaction sendResult = parity.personalSendTransaction(trans, passsword).send();
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
	
	static class AheadTx {
		String aheadBlock;
		BigInteger value;
		
		AheadTx(String para) {
			String[] p = para.split(":");
			this.aheadBlock = p[0];
			value = Convert.toWei(p[1], Convert.Unit.ETHER).toBigInteger();
		}
		
		public String getAheadBlock() {
			return aheadBlock;
		}
		public BigInteger getValue() {
			return value;
		}
	}



}
