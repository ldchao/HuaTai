package cn.edu.nju.ldchao.huatai;

import cn.edu.nju.ldchao.huatai.exception.DataProcessingException;
import cn.edu.nju.ldchao.huatai.service.DrawDownService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class HuataiApplicationTests {

    @Autowired
    DrawDownService drawDownService;

    @Test
    void contextLoads() {
        List<String> idList = new ArrayList<>();
        List<Double> proportionList = new ArrayList<>();
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);//设置保留几位小数
        try {
            double maxDrawDown = drawDownService.getMaxDrawDown(idList,proportionList);
            System.out.println(format.format(maxDrawDown));
        } catch (DataProcessingException e) {
            System.out.println(e.getMessage());
        }
        idList.add("1");
        proportionList.add(1d);
        try {
            double maxDrawDown = drawDownService.getMaxDrawDown(idList,proportionList);
            System.out.println(format.format(maxDrawDown));
        } catch (DataProcessingException e) {
            System.out.println(e.getMessage());
        }
        proportionList = new ArrayList<>();
        proportionList.add(0.2d);
        idList.add("2");
        proportionList.add(0.4d);
        idList.add("3");
        proportionList.add(0.4d);
        try {
            double maxDrawDown = drawDownService.getMaxDrawDown(idList,proportionList);
            System.out.println(format.format(maxDrawDown));
        } catch (DataProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

}
