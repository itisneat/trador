package com.itisneat.eos;

import com.itisneat.wallet.parity.ParityClient;
import com.itisneat.wallet.wrap.MyParity;
import org.web3j.protocol.core.methods.response.EthBlock;
import rx.Observable;

/**
 * Created by leo on 2017/7/4.
 */
public class Processor {
    private static MyParity parity = (MyParity) ParityClient.getParity();
    public static void main(String[] args) {
        Observable<EthBlock> blockObservable = parity.blockObservable(true);
        EosThransObserver blockObserver = new EosThransObserver(blockObservable);
        System.out.println("EOS transaction processor start.......");
        blockObservable.subscribe(blockObserver);
        System.out.println("EOS transaction processor stop.......");
    }
}
