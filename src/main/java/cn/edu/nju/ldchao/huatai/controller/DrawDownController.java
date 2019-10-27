package cn.edu.nju.ldchao.huatai.controller;

import cn.edu.nju.ldchao.huatai.aop.LimitAnnotaiton;
import cn.edu.nju.ldchao.huatai.exception.DataProcessingException;
import cn.edu.nju.ldchao.huatai.service.DrawDownService;
import cn.edu.nju.ldchao.huatai.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ldchao on 2019/10/26.
 */
@RestController
public class DrawDownController {

    private final DrawDownService drawDownService;
    private final RedisUtil redisUtil;
    private NumberFormat format;
    private static Logger logger = LoggerFactory.getLogger(DrawDownController.class);

    public DrawDownController(DrawDownService drawDownService, RedisUtil redisUtil) {
        this.drawDownService = drawDownService;
        this.redisUtil = redisUtil;
        format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);//设置保留几位小数
    }

    @LimitAnnotaiton(name = "MaxDrawDown", period = 100, count = 10)
    @PostMapping(value = "getMaxDrawDown")
    public String getMaxDrawDown(@RequestBody List<String> idList) {
        logger.debug("Request parameter: {}", Arrays.toString(idList.toArray()));
        if (idList.size() > getAllIds().size()) {
            logger.warn("Suspected DDOS attack, the size of idList is {}.", idList.size());
            return "Invalid id list.";
        }

        long startTime = System.currentTimeMillis();
        String result;

        //  从缓存中获取
        Collections.sort(idList);
        String key = "MDD_"+StringUtils.join(idList.toArray(), ",");
        if (redisUtil.hasKey(key)) {
            result = redisUtil.get(key);
            if (result != null) {
                redisUtil.expire(key); // 重置过期时间
                logger.debug("Request result: {}, use {} ms, through redis.", result, System.currentTimeMillis() - startTime);
                return result;
            }
        }

        try {
            result = format.format(drawDownService.getMaxDrawDown(idList));
        } catch (DataProcessingException e) {
            logger.error("Data processing Exception:{}", e.getMessage());
            result = e.getMessage();
        }
        redisUtil.set(key, result); // 将值加入缓存
        logger.debug("Request result: {}, use {} ms, through compute.", result, System.currentTimeMillis() - startTime);
        return result;
    }


    @GetMapping(value = "getAllIds")
    public List<String> getAllIds() {
        return drawDownService.getAllIds();
    }

}
