package cn.edu.nju.ldchao.huatai.schedute;

import cn.edu.nju.ldchao.huatai.util.MemoryDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by ldchao on 2019/10/27.
 */
@Component
public class ReadFundInfoTask {

    private static final Logger logger = LoggerFactory.getLogger(ReadFundInfoTask.class);

    private final MemoryDataUtil memoryDataUtil;

    public ReadFundInfoTask(MemoryDataUtil memoryDataUtil) {
        this.memoryDataUtil = memoryDataUtil;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void readNewFundInfoTask() {
        logger.debug("Schedule task start.");
        memoryDataUtil.initFunds();
        logger.debug("Schedule task finished.");
    }

}
