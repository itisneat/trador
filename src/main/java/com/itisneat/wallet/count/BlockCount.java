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

    public int getSum() {
        return sum;
    }

    public void countSumAndAvg(int costBlockNum) {
    	finishedTxNum++;
        this.sum = sum+costBlockNum;
        //DecimalFormat df=new DecimalFormat("0.00");
        this.avg = (double)sum/(double)finishedTxNum;

        if(finishedTxNum == pendingTx.size())
        {
            System.out.println("**************Hi,all transaction finish in block:"+ this.blockNum+" AVG:" + avg);
        }
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
    
    public boolean finished() {
    	return this.finishedTxNum == this.pendingTx.size();
    }
}
