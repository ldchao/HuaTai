package cn.edu.nju.ldchao.huatai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HuataiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuataiApplication.class, args);
    }

}
