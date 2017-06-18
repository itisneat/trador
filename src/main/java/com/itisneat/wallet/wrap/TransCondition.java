package com.itisneat.wallet.wrap;

import java.math.BigInteger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by leo on 2017/6/18.
 */
@JsonInclude(Include.NON_NULL)
public class TransCondition {
    private BigInteger block;
    private BigInteger time;

    public TransCondition(BigInteger v, Boolean isBlock){
        if (isBlock) {
            this.block = v;
        } else {
            this.time = v;
        }
    }

    public BigInteger getBlock() {
        return block;
    }

    public BigInteger getTime() {
        return time;
    }


}
