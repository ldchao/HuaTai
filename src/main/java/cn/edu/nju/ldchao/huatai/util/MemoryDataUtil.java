package cn.edu.nju.ldchao.huatai.util;

import cn.edu.nju.ldchao.huatai.po.FundPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ldchao on 2019/10/26.
 */
@Component
public class MemoryDataUtil {

    private final RedisUtil redisUtil;

    @Value("${customize.default.file.path}")
    private String defaultFilePath;

    @Value("${customize.default.useCache}")
    private boolean useCache;

    public MemoryDataUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    private Map<String, FundPo> allFunds = new HashMap<>();
    private List<String> ids;
    private static Logger logger = LoggerFactory.getLogger(MemoryDataUtil.class);

    private long lastModifiedTime = 0L;

    @PostConstruct
    public void initFunds() {
        logger.debug("Init start.");
        Map<String, FundPo> tmpAllFunds = new HashMap<>();
        File file = new File(defaultFilePath);
        if (file.isFile() && file.exists()) {
            if (file.lastModified() > lastModifiedTime) {
                lastModifiedTime = file.lastModified();
                List<String> lines = FileUtil.readFile(file);
                lines.remove(0); // 移除表头
                FundPo fundPo = new FundPo();
                boolean isFirst = true;
                for (String line : lines) {
                    String[] items = line.split("\t");
                    if (isFirst || !fundPo.getId().equals(items[0])) { // 首次
                        if (isFirst) {
                            isFirst = false;
                        } else {
                            tmpAllFunds.put(fundPo.getId(), fundPo);
                            fundPo = new FundPo();
                        }
                        fundPo.setId(items[0]);
                        fundPo.setStartDate(items[1]);
                        fundPo.setDateList(new ArrayList<>());
                        fundPo.setWorthList(new ArrayList<>());
                    }
                    fundPo.setEndDate(items[1]);
                    fundPo.getDateList().add(items[1]);
                    fundPo.getWorthList().add(Double.valueOf(items[2]));
                }
                if (!isFirst) {
                    tmpAllFunds.put(fundPo.getId(), fundPo);
                }
                ids = new ArrayList<>(tmpAllFunds.keySet());
                allFunds = tmpAllFunds;
                if (useCache) {
                    redisUtil.clear();
                }
            } else {
                logger.debug("No need to refresh the funds.");
            }
        } else {
            logger.error("File path config error: {}.", defaultFilePath);
        }
        logger.debug("Init end.");
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Map<String, FundPo> getAllFunds() {
        return allFunds;
    }

    public FundPo getFundById(String id) {
        return allFunds.get(id);
    }

    public List<String> getAllFundId() {
        return ids;
    }

    public boolean isContainsId(String id) {
        return allFunds.containsKey(id);
    }
}
