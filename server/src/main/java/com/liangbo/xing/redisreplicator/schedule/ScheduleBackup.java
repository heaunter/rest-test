package com.liangbo.xing.redisreplicator.schedule;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.IntStream;

@EnableScheduling
@Configuration
public class ScheduleBackup {

    private final Logger logger = LoggerFactory.getLogger(ScheduleBackup.class);

    public static final String REDIS_KEY = "exam_eid";
    public static final String REDIS_MAPPING_KEY = "mt_exam_paper_eid";

    public static final String HASH_EY = "name";

    private static int cursor = 0;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    //    @Scheduled(cron = "0 0/1 * * * ?")
    public void schedule() {
        if (cursor > 0) {
            return;
        }

        logger.info("cursor:" + cursor);
        IntStream.range(1, 11).forEach(value -> {

            String uuid = UUID.randomUUID().toString();
            String hashKey = HASH_EY + value + uuid;
            redisTemplate.opsForHash().put(REDIS_KEY, hashKey, value);
        });
        cursor++;
    }

//    @Scheduled(cron = "0 0/1 * * * ?")
    public void schedule2() {
        if (cursor > 0) {
            return;
        }

        logger.info("cursor:" + cursor);
        long begin = System.currentTimeMillis();

        IntStream.range(1, 5001).forEach(value -> {
            String[] keyColumns = new String[]{"exam_id"};
            String[] valueColumns = new String[]{"paper_id", "version"};

            String realKey = String.format(indexBuilder(keyColumns), "exam_id:" + value);

            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("paper_id", value);
            valueMap.put("version", "version:" + value);


            Object values = redisTemplate.opsForHash().get(REDIS_MAPPING_KEY, realKey);
            if (ObjectUtils.isEmpty(values)) {
                Set<Map<String, Object>> list = new HashSet<>();
                list.add(valueMap);
                redisTemplate.opsForHash().put(REDIS_MAPPING_KEY, realKey, list);
            } else {
                ((Set) values).add(valueMap);
                redisTemplate.opsForHash().put(REDIS_MAPPING_KEY, realKey, values);
            }

        });
        System.out.println(System.currentTimeMillis() - begin);
        cursor++;
    }

    private String indexBuilder(String[] unionKeys) {
        String placeholder = "%s";
        String joiner = "_";
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, unionKeys.length).forEach(value -> {
            builder.append(placeholder).append(joiner);
        });
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

}
