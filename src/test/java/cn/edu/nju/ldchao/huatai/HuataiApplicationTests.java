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
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);//设置保留几位小数
        try {
            double maxDrawDown = drawDownService.getMaxDrawDown(idList);
            System.out.println(format.format(maxDrawDown));
        } catch (DataProcessingException e) {
            System.out.println(e.getMessage());
        }
        idList.add("1");
        try {
            double maxDrawDown = drawDownService.getMaxDrawDown(idList);
            System.out.println(format.format(maxDrawDown));
        } catch (DataProcessingException e) {
            System.out.println(e.getMessage());
        }
        idList.add("2");
        idList.add("3");
        try {
            double maxDrawDown = drawDownService.getMaxDrawDown(idList);
            System.out.println(format.format(maxDrawDown));
        } catch (DataProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

}
