package com.itisneat.wallet.wrap;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.parity.JsonRpc2_0Parity;

import java.util.Arrays;

/**
 * Created by leo on 2017/6/18.
 */
public class MyParity extends JsonRpc2_0Parity {
    public MyParity(Web3jService web3jService)
    {
       super(web3jService);
    }

    public Request<?, EthSendTransaction> personalSendTransaction(Transaction transaction, String pwd) {
        return new Request("personal_sendTransaction",
                Arrays.asList(new Object[]{transaction, pwd}),
                1L, this.web3jService,
                EthSendTransaction.class);
    }

}

