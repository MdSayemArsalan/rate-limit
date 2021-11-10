package com.arsalan.ratelimit.interceptor;

import com.arsalan.ratelimit.util.ConsumptionProbe;
import com.arsalan.ratelimit.util.QuotaConsumer;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * In a production setting, we will more likely have an API gateway (such as Spring Cloud Gateway, Netflix Zuul, etc) to
 * direct or reject the requests based on the remaining quota and other filters.
 * For the sake of simplicity of this project, instead of a gateway, an Interceptor is used to achieve the same
 * functionality.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String invalidApiKeyMsg = "Invalid Header: api-key, try using api-key: api-key1";


    /**
     * Interceptor to check if the provided API-KEY has remaining quota to consume the service.
     * If quota is available then request is allowed to consume the service, else TOO_MANY_REQUESTS error is returned
     * with retry-after-seconds header in response.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Checking if provided api-key is valid or not
        String apiKey = request.getHeader("api-key");
        if (!QuotaConsumer.isValidApiKey(apiKey)) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), invalidApiKeyMsg);
            return false;
        }

        // Trying to consume from the remaining quota for the respective api-key
        ConsumptionProbe probe = QuotaConsumer.consumeFromQuota(apiKey);
        if (probe == null) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), invalidApiKeyMsg);
            return false;
        }

        if (probe.isConsumed()) {
            return true;
        } else {
            // If no more quota is remaining, then responding with TOO_MANY_REQUESTS error with retry-after-seconds
            long secondsToWait = probe.getTryAfterInMillis() / 1000;
            response.addHeader("retry-after-seconds", String.valueOf(secondsToWait));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have exhausted your API request quota. Try after " + secondsToWait + " seconds");
            return false;
        }

    }
}
