/**
 * 
 */
package com.itisneat.wallet.observer;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;

import com.itisneat.wallet.count.DelayInfo;
import com.itisneat.wallet.count.TxCountCenter;

import rx.Observable;
import rx.Observer;

/**
 * @author leo
 *
 */
public class MyBlockObserver implements Observer<EthBlock> {
	
	TxCountCenter txCountCenter;
	Observable<EthBlock> blockObservable;
	
	public MyBlockObserver(Observable<EthBlock> observable) {
		txCountCenter = TxCountCenter.getInstance();
		blockObservable = observable;
	}

	@Override
	public void onCompleted() {
		System.out.println("on my block observer completed");
	}

	@Override
	public void onError(Throwable e) {
		System.out.println("on my block observer error");
		this.blockObservable.subscribe(this);
	}

	@Override
	public void onNext(EthBlock ethBlock) {
		if(ethBlock.hasError()) {
			System.out.println("recived eth block with error: " + ethBlock.getError().getMessage());
		} else {
			EthBlock.Block block = ethBlock.getBlock();
			BigInteger blockNum = block.getNumber();
			txCountCenter.setCurrentBlock(blockNum);
			for(TransactionResult txR: block.getTransactions()) {
				EthBlock.TransactionObject txObj = (EthBlock.TransactionObject)txR.get();
				txCountCenter.txFinishCount(txObj.getHash(), blockNum);
			}
			
			List<DelayInfo> infos = txCountCenter.getRecentlyDelayInfo(30);
			StringBuilder sb = new StringBuilder("");
			for(DelayInfo di : infos) {
				sb.append("-start block number: ").append(di.getStartBlockNum())
					.append(" | avg: ").append(String.format("%-7s", new DecimalFormat("0.00").format(di.getAvgCostBlock())))
					.append(" | finished: ").append(di.getFinishInfo()).append(" | ").append(di.getDistribution()).append("-\n");
			}
			System.out.println(sb.toString());
			
		}
	}
	

}
