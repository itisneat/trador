/**
 * 
 */
package com.itisneat.wallet.parity;

import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;

/**
 * @author leo
 *
 */
public class ParityClient {
	
    private static String ip = "http://106.15.137.107:8545/";

    private ParityClient(){}

    private static class ClientHolder{
        private static final Parity parity = Parity.build(new HttpService(ip));
    }

    public static final  Parity getParity(){
        return ClientHolder.parity;
    }
}
