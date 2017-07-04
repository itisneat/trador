package com.itisneat.eos;

import com.itisneat.collector.model.TSDBDatapoint;
import org.web3j.protocol.core.methods.response.EthBlock;
import rx.Observable;
import rx.Observer;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 2017/7/3.
 */
public class EosThransObserver implements Observer<EthBlock> {
    private Observable<EthBlock> blockObservable;
    private static String EOS_ADDRESS="0xd0a6e6c54dbc68db5db3a091b171a77407ff7ccf";
    //2017.7.1 21:00:00  second
    private static long EOS_STARTTIME = 1498914000l;

    public EosThransObserver(Observable<EthBlock> observable)
    {
        this.blockObservable = observable;
    }
    @Override
    public void onCompleted() {
        System.out.println("EosThransObserver completed");
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("EosThransObserver error");
        this.blockObservable.subscribe(this);
    }

    @Override
    public void onNext(EthBlock ethBlock) {

        if(ethBlock.hasError()) {
            System.out.println("EosThransObserver recived eth block with error: " + ethBlock.getError().getMessage());
        } else {
            EthBlock.Block block = ethBlock.getBlock();
            BigInteger blockNum = block.getNumber();
            System.out.println("EosThransObserver get a block:"+blockNum);
            BigInteger blockTimeStamp = block.getTimestamp();
            List<EthBlock.TransactionResult> transactionResults = block.getTransactions();
            for(EthBlock.TransactionResult transaction:transactionResults)
            {
                EthBlock.TransactionObject txObj = (EthBlock.TransactionObject)transaction.get();
                String toAddress = txObj.getTo();
                if (EOS_ADDRESS.equals(toAddress))
                {
                    BigInteger value = txObj.getValue();
                    Map<String, String> tags = new HashMap<>();
                    long sectionNum =getSectionNum(blockTimeStamp.longValue());
                    tags.put("Section", String.valueOf(sectionNum));
                    TSDBDatapoint data = new TSDBDatapoint("EOS Trasaction",value.toString(),blockTimeStamp.longValue(),tags);
                    System.out.println("Get a transaction to EOS: value="+value.toString()+" blockTimeStamp="+blockTimeStamp.toString()+" Section="+sectionNum);
                    //send TSDB data into DB

                    //calculate total and average of each section


                }
            }
        }
        System.out.println("EosThransObserver finish a block.");
    }

    private long getSectionNum(long timeStamp)
    {
        long sub = timeStamp - EOS_STARTTIME;
        return (sub/(23*60*60l))+2;
    }

}
