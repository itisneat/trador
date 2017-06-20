package com.itisneat.wallet.count;

import java.math.BigInteger;

public class DelayInfo {
	BigInteger startBlockNum;
	String finishInfo;
	double avgCostBlock;
	String distribution;
	
	public BigInteger getStartBlockNum() {
		return startBlockNum;
	}
	public void setStartBlockNum(BigInteger startBlockNum) {
		this.startBlockNum = startBlockNum;
	}
	public double getAvgCostBlock() {
		return avgCostBlock;
	}
	public void setAvgCostBlock(double avgCostBlock) {
		this.avgCostBlock = avgCostBlock;
	}
	public String getDistribution() {
		return distribution;
	}
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}
	public String getFinishInfo() {
		return finishInfo;
	}
	public void setFinishInfo(String finishInfo) {
		this.finishInfo = finishInfo;
	}
}
