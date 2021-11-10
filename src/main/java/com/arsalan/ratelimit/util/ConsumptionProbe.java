package com.arsalan.ratelimit.util;

import lombok.Getter;

@Getter
public class ConsumptionProbe {

    public boolean isConsumed;
    public long tryAfterInMillis;

    public ConsumptionProbe(boolean isConsumed, long tryAfterInMillis) {
        this.isConsumed = isConsumed;
        this.tryAfterInMillis = tryAfterInMillis;
    }

}
