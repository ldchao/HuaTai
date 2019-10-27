package cn.edu.nju.ldchao.huatai.po;

import lombok.Data;

import java.util.List;

/**
 * Created by ldchao on 2019/10/26.
 */
@Data
public class FundPo {

    private String id;
    private String startDate;
    private String endDate;
    private List<String> dateList;
    private List<Double> worthList;

}
