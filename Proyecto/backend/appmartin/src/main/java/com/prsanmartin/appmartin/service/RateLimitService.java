package com.prsanmartin.appmartin.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> lastReset = new ConcurrentHashMap<>();

    public boolean isAllowed(String key, int capacity, Duration duration) {
        long now = System.currentTimeMillis();
        long resetTime = lastReset.getOrDefault(key, now);
        
        // Reset counter if duration has passed
        if (now - resetTime > duration.toMillis()) {
            requestCounts.put(key, 0);
            lastReset.put(key, now);
        }
        
        int currentCount = requestCounts.getOrDefault(key, 0);
        if (currentCount >= capacity) {
            return false;
        }
        
        requestCounts.put(key, currentCount + 1);
        return true;
    }

    public boolean isLoginAllowed(String ipAddress) {
        return isAllowed("login:" + ipAddress, 5, Duration.ofMinutes(15));
    }

    public boolean isLoginAllowedByUser(String username) {
        return isAllowed("user:" + username, 10, Duration.ofMinutes(15));
    }

    public boolean isApiAllowed(String ipAddress) {
        return isAllowed("api:" + ipAddress, 100, Duration.ofMinutes(1));
    }

    public void resetBucket(String key) {
        requestCounts.remove(key);
        lastReset.remove(key);
    }

    public void resetAllBuckets() {
        requestCounts.clear();
        lastReset.clear();
    }
}
