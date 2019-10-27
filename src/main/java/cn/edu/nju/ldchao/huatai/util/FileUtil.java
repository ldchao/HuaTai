package cn.edu.nju.ldchao.huatai.util;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

/**
 * Created by ldchao on 2019/10/26.
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static List<String> readFile(File file) {
        logger.debug("Read file: {}", file.getName());
        List<String> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                result.add(tempString);
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }

        logger.debug("File size: {}", result.size());
        return result;
    }

}
