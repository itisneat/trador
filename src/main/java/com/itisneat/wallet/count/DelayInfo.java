package com.itisneat.wallet.count;

import java.math.BigInteger;

public class DelayInfo {
	BigInteger startBlockNum;
	Boolean allFinished;
	double avgCostBlock;
	
	public BigInteger getStartBlockNum() {
		return startBlockNum;
	}
	public void setStartBlockNum(BigInteger startBlockNum) {
		this.startBlockNum = startBlockNum;
	}
	public Boolean getAllFinished() {
		return allFinished;
	}
	public void setAllFinished(Boolean allFinished) {
		this.allFinished = allFinished;
	}
	public double getAvgCostBlock() {
		return avgCostBlock;
	}
	public void setAvgCostBlock(double avgCostBlock) {
		this.avgCostBlock = avgCostBlock;
	}
	
}
