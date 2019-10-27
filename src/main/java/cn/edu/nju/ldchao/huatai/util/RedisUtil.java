package cn.edu.nju.ldchao.huatai.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by ldchao on 2019/10/27.
 */
@Component
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private static final long DEFAULT_EXPIRE_TIME = 30L;
    private volatile boolean isUse = true;

    public RedisUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 是否存在key
     */
    public Boolean hasKey(String key) {
        return isUse ? redisTemplate.hasKey(key) : Boolean.FALSE;
    }

    /**
     * 读取缓存
     */
    public String get(final String key) {
        return isUse ? redisTemplate.opsForValue().get(key) : null;
    }

    /**
     * 刷新缓存过期时间
     */
    public Boolean expire(final String key) {
        return isUse ? redisTemplate.expire(key, DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES) : Boolean.FALSE;
    }

    /**
     * 写入缓存
     */
    public boolean set(final String key, String value) {
        boolean result = false;
        if (isUse) {
            try {
                redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
                result = true;
            } catch (Exception e) {
                logger.error("Set redis error: {}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 更新缓存
     */
    public boolean getAndSet(final String key, String value) {
        boolean result = false;
        if (isUse) {
            try {
                redisTemplate.opsForValue().getAndSet(key, value);
                result = true;
            } catch (Exception e) {
                logger.error("Update redis error: {}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 删除缓存
     */
    public boolean delete(final String key) {
        boolean result = false;
        if (isUse) {
            try {
                redisTemplate.delete(key);
                result = true;
            } catch (Exception e) {
                logger.error("Delete redis error: {}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 删除缓存，因为keys可能阻塞，需要加锁表示redis是否可用
     */
    public boolean clear() {
        boolean result = false;
        if (isUse) {
            //同步
            synchronized (RedisUtil.class) {
                if (isUse) {
                    //多线程环境下可能会出现问题的地方
                    isUse = false;
                    try {
                        // 容易阻塞，待整改
                        Set<String> keys = redisTemplate.keys("MDD_*");
                        redisTemplate.delete(keys);
                        result = true;
                    } catch (Exception e) {
                        logger.error("Clear redis error: {}", e.getMessage());
                    } finally {
                        isUse = true;
                    }
                }
            }
        }

        return result;
    }
}
