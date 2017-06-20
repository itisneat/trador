package com.itisneat.wallet.count;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by leo on 2017/6/19.
 */
public class TxCountCenter {
    private BigInteger currentBlock;
    //key=startBlockId
    private TreeMap<BigInteger, BlockCount> blockCountMap = new TreeMap<BigInteger, BlockCount>();
    //key=TxHash
    private Map<String, Tx> allTxMap = new HashMap<String, Tx>();

    private static volatile TxCountCenter txCountCenter = new TxCountCenter();
    
    private TxCountCenter(){}
    
    public static TxCountCenter getInstance()
    {
        return txCountCenter;
    }

    BigInteger getCurrentBlock() {
        return currentBlock;
    }

    Map<BigInteger, BlockCount> getBlockCountMap() {
        return blockCountMap;
    }

    Map<String, Tx> getAllTxMap() {
        return allTxMap;
    }

    public synchronized void setCurrentBlock(BigInteger currentBlock) {
        if(blockCountMap.containsKey(currentBlock))
        {
            System.out.println("WARN: Block number already exist. currentBlock="+currentBlock);
        }
        else
        {
            this.currentBlock = currentBlock;
            System.out.println("INFO: set currentBlock="+currentBlock);
        }
    }

    public synchronized void addNewPendingTx(String txHash)
    {
        if(currentBlock == null)
        {
            System.out.println("ERROR: Failed to add new pending transaction since currentBlock is null.txHash="+txHash);
            return;
        }
        if(allTxMap.containsKey(txHash))
        {
            System.out.println("ERROR: duplicate transaction hash.txHash="+txHash);
            return;
        }
        Tx tx = new Tx(txHash);
        tx.setStartBlock(currentBlock);
        tx.setTxStatus(Tx.TxStatus.pending);
        BlockCount blockCount = blockCountMap.get(currentBlock);
        if(blockCount == null)
        {
            blockCount = new BlockCount(currentBlock);
            blockCountMap.put(currentBlock, blockCount);
        }
        blockCount.addPendingTx(txHash);  //ToDO
        allTxMap.put(txHash, tx);
        System.out.println("INFO: new pending transaction added. txHash="+txHash+"startBlock="+currentBlock);
    }

    public synchronized void txFinishCount(String txHash, BigInteger endBlock)
    {
        Tx tx = allTxMap.get(txHash);
        if(tx == null)
        {
            System.out.println("WARN: won't do txFinishCount since no record of this txHash. txHash="+txHash);
            return;
        }
        tx.setEndBlock(endBlock);
        tx.setTxStatus(Tx.TxStatus.finish);
        BlockCount blockCount = blockCountMap.get(tx.getStartBlock());
        BigInteger costBlockNum = tx.getEndBlock().subtract(tx.getStartBlock());
        blockCount.countSumAndAvg(costBlockNum.intValue());
    }

    public synchronized List<DelayInfo> getRecentlyDelayInfo(Integer c) {
    	if (c == null) {
    		c = 10;
    	}
    	
    	Set<BigInteger> desKeySet = blockCountMap.descendingKeySet();
    	LinkedList<DelayInfo> infos = new LinkedList<>();
    	
    	for (BigInteger bn : desKeySet) {
    		if (c -- == 0) {
    			break;
    		}
    		BlockCount bc = blockCountMap.get(bn);
    		DelayInfo info = new DelayInfo();
    		info.setStartBlockNum(bc.getBlockNum());
    		info.setAvgCostBlock(bc.getAvg());
    		info.setAllFinished(bc.finished());
    		
    		infos.addFirst(info);
    	}
    	
    	return infos;
    }
    
    public static void main(String[] args)
    {
        TxCountCenter txCenter = TxCountCenter.getInstance();
        txCenter.setCurrentBlock(new BigInteger("3898400"));
        txCenter.addNewPendingTx("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d9e8b");
        System.out.println("111111111111111111111111111111111111111111");
        System.out.println("CurrentBlock:"+txCenter.getCurrentBlock());
        System.out.println("txCenter.getBlockCountMap().size():"+ txCenter.getBlockCountMap().size());
        System.out.println("txCenter.getAllTxMap().size():"+ txCenter.getAllTxMap().size());
        System.out.println("getAvg:"+txCenter.getBlockCountMap().get(new BigInteger("3898400")).getAvg());
        System.out.println("getSum:"+txCenter.getBlockCountMap().get(new BigInteger("3898400")).getSum());
        System.out.println("getTxHash:"+txCenter.getAllTxMap().get("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d9e8b").getTxHash());
        System.out.println("getStartBlock:"+txCenter.getAllTxMap().get("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d9e8b").getStartBlock());

        txCenter.setCurrentBlock(new BigInteger("3898400"));
        txCenter.addNewPendingTx("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d1234");
        System.out.println("222222222222222222222222222222222222222222222222222222222222222222");
        System.out.println("CurrentBlock:"+txCenter.getCurrentBlock());
        System.out.println("txCenter.getBlockCountMap().size():"+ txCenter.getBlockCountMap().size());
        System.out.println("txCenter.getAllTxMap().size():"+ txCenter.getAllTxMap().size());
        System.out.println("getAvg:"+txCenter.getBlockCountMap().get(new BigInteger("3898400")).getAvg());
        System.out.println("getSum:"+txCenter.getBlockCountMap().get(new BigInteger("3898400")).getSum());
        System.out.println("getTxHash:"+txCenter.getAllTxMap().get("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d1234").getTxHash());
        System.out.println("getStartBlock:"+txCenter.getAllTxMap().get("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d1234").getStartBlock());

        txCenter.setCurrentBlock(new BigInteger("3898401"));
        txCenter.addNewPendingTx("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d5678");
        System.out.println("33333333333333333333333333333333333333333333333333333333333333333333333");
        System.out.println("CurrentBlock:"+txCenter.getCurrentBlock());
        System.out.println("txCenter.getBlockCountMap().size():"+ txCenter.getBlockCountMap().size());
        System.out.println("txCenter.getAllTxMap().size():"+ txCenter.getAllTxMap().size());
        System.out.println("getAvg:"+txCenter.getBlockCountMap().get(new BigInteger("3898401")).getAvg());
        System.out.println("getSum:"+txCenter.getBlockCountMap().get(new BigInteger("3898401")).getSum());
        System.out.println("getTxHash:"+txCenter.getAllTxMap().get("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d5678").getTxHash());
        System.out.println("getStartBlock:"+txCenter.getAllTxMap().get("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d5678").getStartBlock());

        txCenter.txFinishCount("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d9e8b", new BigInteger("3898405"));
        txCenter.txFinishCount("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d1234", new BigInteger("3898406"));
        txCenter.txFinishCount("0x776df74148f14d5a55123a71db6e4653dbc3e971bd57f7c943f94ae2976d5678", new BigInteger("3898405"));
        System.out.println("getAvg:"+txCenter.getBlockCountMap().get(new BigInteger("3898400")).getAvg());
        System.out.println("getSum:"+txCenter.getBlockCountMap().get(new BigInteger("3898400")).getSum());
        System.out.println("getAvg:"+txCenter.getBlockCountMap().get(new BigInteger("3898401")).getAvg());
        System.out.println("getSum:"+txCenter.getBlockCountMap().get(new BigInteger("3898401")).getSum());

    }
}
