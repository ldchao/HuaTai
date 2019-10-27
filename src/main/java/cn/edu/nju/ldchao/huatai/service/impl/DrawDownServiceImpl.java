package cn.edu.nju.ldchao.huatai.service.impl;

import cn.edu.nju.ldchao.huatai.exception.DataProcessingException;
import cn.edu.nju.ldchao.huatai.po.FundPo;
import cn.edu.nju.ldchao.huatai.service.DrawDownService;
import cn.edu.nju.ldchao.huatai.util.MemoryDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ldchao on 2019/10/26.
 */
@Service
public class DrawDownServiceImpl implements DrawDownService {

    private static Logger logger = LoggerFactory.getLogger(DrawDownServiceImpl.class);

    private final MemoryDataUtil memoryDataUtil;

    public DrawDownServiceImpl(MemoryDataUtil memoryDataUtil) {
        this.memoryDataUtil = memoryDataUtil;
    }

    @Override
    public Double getMaxDrawDown(List<String> idList,List<Double> proportionList) throws DataProcessingException {
        if (null == idList || idList.isEmpty()) {
            throw new DataProcessingException("Id list is empty.", 0d);
        }

        List<FundPo> combination = new ArrayList<>(idList.size());
        for (String id : idList) {
            if (memoryDataUtil.isContainsId(id)) {
                combination.add(memoryDataUtil.getFundById(id));
            }
        }
        if (combination.size() == 0) {
            throw new DataProcessingException("Invalid ids, not found corresponding funds.", 0d);
        } else {
            // 根据起止时间，截取数据
            List<List<Double>> fundList = formatFundList(combination);

            // 获取起始资金
            List<Double> startMoney = getStartMoney(fundList);

            // 取投资组合起始日期当天所有产品对应的每日资产最大值作为投资组合的起始资产，并按照占比分配资金至各个实际产品
            List<Double> formatStartMoney = formatStartMoney(startMoney,proportionList);

            int fundLength = startMoney.size();
            int worthLength = fundList.get(0).size();

            double[] worthList = new double[worthLength];
            for (int i = 0; i < worthLength; i++) {
                double sum = 0;
                for (int j = 0; j < fundLength; j++) {
                    sum += formatStartMoney.get(j) * fundList.get(j).get(i) / startMoney.get(j);
                }
                worthList[i] =sum;
            }

            double[] minWorthList = worthList.clone();
            for (int i = worthLength -2; i >=0 ; i--) {
                if(minWorthList[i] > minWorthList[i+1]){
                    minWorthList[i] = minWorthList[i+1];
                }
            }

            // drawdown=max（Pi-Pj）/Pi
            double maxDrawDown = 0;
            for (int i = 0; i < worthLength; i++) {
                double drawDown = (worthList[i]-minWorthList[i])/worthList[i];
                if(Double.compare(drawDown,maxDrawDown)>0){
                    maxDrawDown = drawDown;
                }
            }
            return maxDrawDown;
        }
    }

    @Override
    public List<String> getAllIds() {
        return memoryDataUtil.getAllFundId();
    }

    //  所有传入产品的最晚起始日期作为起始日期
    private String getStart(List<FundPo> combination) {
        String startDate = combination.get(0).getStartDate();
        for (FundPo fund : combination) {
            if (fund.getStartDate().compareTo(startDate) > 0) {
                startDate = fund.getStartDate();
            }
        }
        return startDate;
    }

    // 所有传入产品的最早结束日期为结束日期
    private String getEnd(List<FundPo> combination) {
        String endDate = combination.get(0).getEndDate();
        for (FundPo fund : combination) {
            if (fund.getEndDate().compareTo(endDate) < 0) {
                endDate = fund.getEndDate();
            }
        }
        return endDate;
    }


    private List<List<Double>> formatFundList(List<FundPo> combination)
            throws DataProcessingException {
        String startDate = getStart(combination);
        String endDate = getEnd(combination);
        if (endDate.compareTo(startDate) <= 0) {
            throw new DataProcessingException("The date of corresponding funds can't meet the demand.", 0d);
        }
        List<List<Double>> fundList = new ArrayList<>(combination.size());
        int lastLength = -1;
        for (FundPo fundPo : combination) {
            List<String> dateList = fundPo.getDateList();
            int startIndex = dateList.indexOf(startDate);
            int endIndex = dateList.lastIndexOf(endDate);
            List<Double> worthList = fundPo.getWorthList().subList(startIndex, endIndex + 1);
            if (lastLength != -1 && lastLength != worthList.size()) {
                throw new DataProcessingException("The data of corresponding funds is wrong.", 0d);
            }
            lastLength = worthList.size();
            fundList.add(worthList);
        }
        return fundList;
    }

    private List<Double> getStartMoney(List<List<Double>> fundList) throws DataProcessingException {
        List<Double> startMoney = new ArrayList<>(fundList.size());
        double tmpMoney;
        for (List<Double> worthList : fundList) {
            tmpMoney = worthList.get(0);
            if (Double.compare(tmpMoney, 0) <= 0) {
                throw new DataProcessingException("The data of corresponding funds is wrong.", 0d);
            } else {
                startMoney.add(tmpMoney);
            }
        }
        return startMoney;
    }

    private List<Double> formatStartMoney(List<Double> startMoney,List<Double> proportionList) {
        double maxMoney = Collections.max(startMoney);

        List<Double> result = new ArrayList<>(startMoney.size());
        for (Double d : proportionList) {
            result.add(maxMoney * d);
        }
        return result;
    }


}
