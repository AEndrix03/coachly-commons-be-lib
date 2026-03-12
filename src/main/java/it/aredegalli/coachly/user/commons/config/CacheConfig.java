package it.aredegalli.coachly.user.commons.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String USER_ID_CACHE = "userIdCache";
    private static final Duration DEFAULT_USER_ID_TTL = Duration.ofMinutes(5);
    private static final long DEFAULT_MAXIMUM_SIZE = 10_000L;

    @Bean
    public CacheManager cacheManager(
        @Value("${coachly.services.users.cache.user-id-ttl:5m}") Duration userIdTtl,
        @Value("${coachly.services.users.cache.maximum-size:10000}") long maximumSize
    ) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(USER_ID_CACHE);
        cacheManager.setAllowNullValues(false);
        cacheManager.setCaffeine(buildCaffeine(userIdTtl, maximumSize));
        return cacheManager;
    }

    @Bean(name = USER_ID_CACHE)
    public Cache<String, String> userIdCache(
        @Value("${coachly.services.users.cache.user-id-ttl:5m}") Duration userIdTtl,
        @Value("${coachly.services.users.cache.maximum-size:10000}") long maximumSize
    ) {
        return buildCache(userIdTtl, maximumSize);
    }

    public static <K, V> Cache<K, V> buildCache(Duration ttl, long maximumSize) {
        return buildCaffeine(ttl, maximumSize).build();
    }

    private static Caffeine<Object, Object> buildCaffeine(Duration ttl, long maximumSize) {
        Duration effectiveTtl = ttl == null || ttl.isZero() || ttl.isNegative() ? DEFAULT_USER_ID_TTL : ttl;
        long effectiveMaximumSize = maximumSize <= 0 ? DEFAULT_MAXIMUM_SIZE : maximumSize;
        return Caffeine.newBuilder()
            .maximumSize(effectiveMaximumSize)
            .expireAfterWrite(effectiveTtl);
    }
}
