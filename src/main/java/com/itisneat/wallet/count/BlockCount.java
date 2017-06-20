package com.itisneat.wallet.count;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/6/19.
 */
public class BlockCount {
    private BigInteger blockNum;
    private int finishedTxNum;
    private int sum;
    private double avg;
    private int[] distribution = new int[20];

    private List<String> pendingTx = new ArrayList<String>();

    public BlockCount(BigInteger blockNum)
    {
        this.blockNum = blockNum;
    }

    public BigInteger getBlockNum() {
        return blockNum;
    }

//    public int getPendingTxNum() {
//        return pendingTxNum;
//    }

//    public void setPendingTxNum(int pendingTxNum) {
//        this.pendingTxNum = pendingTxNum;
//    }

    int getSum() {
        return sum;
    }

    public void countSumAndAvg(int costBlockNum) {
    	if (costBlockNum < 1) {
    		System.out.println("ERROR: cost block num can not be less than 1.");
    		return;
    	}
    	if (costBlockNum < 20) {
    		this.distribution[costBlockNum - 1] ++;
    	} else {
    		this.distribution[19]++;
    	}
    	
    	finishedTxNum++;
        this.sum = sum+costBlockNum;
        //DecimalFormat df=new DecimalFormat("0.00");
        this.avg = (double)sum/(double)finishedTxNum;

        if(finishedTxNum == pendingTx.size())
        {
            System.out.println("**************Hi,all transaction finish in block:"+ this.blockNum +" AVG:" + avg + " distribution: " + this.getDistributionString());
        }
    }
    
    public String getDistributionString() {
    	StringBuilder sb = new StringBuilder("[");
    	for (int i = 0; i < 20; i++) {
    		if (i == 19) {
    			sb.append(">").append(i + 1).append(":").append(this.distribution[i]).append("]");
    		} else {
    			sb.append(i + 1).append(":").append(this.distribution[i]).append(" , ");
    		}
    	}
    	return sb.toString();
    }

    public double getAvg() {
        return avg;
    }

//    public void setAvg(int avg) {
//        this.avg = avg;
//    }

    public void addPendingTx(String txHash)
    {
        pendingTx.add(txHash);
    }

    public List<String> getPendingTx()
    {
        return pendingTx;
    }
    
    public String getFinishedInfo() {
    	return String.format("%-9s", this.finishedTxNum + "/" + this.pendingTx.size());
    }
}
