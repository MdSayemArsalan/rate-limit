package com.arsalan.ratelimit.util;

import java.util.Calendar;
import java.util.TimeZone;

public class QuotaBucket {

    // Eg. For rate limit of 10 requests per second, capacity = 10 and intervalInMillis = 1000
    private final int capacity;
    private final long intervalInMillis;

    // remaining quota
    private int remaining = 0;

    // timestamp of last refill
    private long refillTimeMillis = 0;


    public QuotaBucket(int capacity, long intervalInMillis) throws Exception {
        if (capacity < 0)
            throw new Exception("Invalid capacity. Capacity should be greater than or equals to 0");
        if (intervalInMillis < 0)
            throw new Exception("Invalid interval. Interval should be greater than or equals to 0");
        this.capacity = capacity;
        this.intervalInMillis = intervalInMillis;
    }

    /**
     * This function is declared as synchronized to make it thread safe
     */
    public synchronized ConsumptionProbe tryConsumeAndReturnRemaining() {

        long currentTime = getCurrentTimeInMillis();

        // refilling based on whether refilling interval has passed or not
        if (currentTime - refillTimeMillis > intervalInMillis) {
            remaining = capacity;
            refillTimeMillis = currentTime;
        }

        // consuming based on remaining quota
        if (remaining > 0) {
            remaining--;
            return new ConsumptionProbe(true, 0);
        } else {
            long tryAfterInMillis = refillTimeMillis + intervalInMillis - currentTime;
            return new ConsumptionProbe(false, tryAfterInMillis);
        }
    }

    private long getCurrentTimeInMillis() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();
    }

}
