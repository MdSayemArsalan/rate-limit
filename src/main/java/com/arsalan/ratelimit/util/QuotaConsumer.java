package com.arsalan.ratelimit.util;

import java.util.HashMap;

/**
 * In a production setting, Quota Consumer will get the remaining quota details from a database such as Redis, such that
 * all the instances running this service can share the same remaining quota details.
 * For the sake of simplicity of this project, a HashMap is used as a cache to store remaining quota details.
 */
public class QuotaConsumer {

    private static final long OneHourInMillis = 60 * 60 * 1000;

    private static HashMap<String, QuotaBucket> quotaHashMap;

    /**
     * Populating Quota HashMap with dummy API keys and quota limits for demonstration
     */
    static {
        try {
            quotaHashMap = new HashMap<>();
            quotaHashMap.put("api-key1", new QuotaBucket(10, OneHourInMillis));
            quotaHashMap.put("api-key2", new QuotaBucket(20, OneHourInMillis));
            quotaHashMap.put("api-key3", new QuotaBucket(30, OneHourInMillis));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To check if the given API-KEY is valid or not
     */
    public static boolean isValidApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty())
            return false;
        return quotaHashMap.containsKey(apiKey);
    }

    /**
     * To consume from the remaining quota
     */
    public static ConsumptionProbe consumeFromQuota(String apiKey) {
        QuotaBucket bucket = quotaHashMap.get(apiKey);
        if (bucket != null) {
            return bucket.tryConsumeAndReturnRemaining();
        } else {
            return null;
        }
    }

}
