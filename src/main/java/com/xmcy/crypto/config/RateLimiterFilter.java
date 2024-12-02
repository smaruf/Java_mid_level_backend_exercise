package com.xmcy.crypto.config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bandwidth;


import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.time.temporal.ChronoUnit;

@Component
public class RateLimiterFilter extends HttpFilter {

    @Value("${rate.limiter.requests}")
    private int requests;

    @Value("${rate.limiter.duration}")
    private int durationValue;

    @Value("${rate.limiter.duration.unit}")
    private String durationUnit;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, this::newBucket);

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.getWriter().write("Too many requests");
        }
    }

    private Bucket newBucket(String ip) {
        Duration duration = Duration.of(this.durationValue, ChronoUnit.valueOf(this.durationUnit.toUpperCase()));
        Refill refill = Refill.greedy(requests, duration);
        Bandwidth limit = Bandwidth.classic(requests, refill);
        return Bucket.builder().addLimit(limit).build();
    }

}
