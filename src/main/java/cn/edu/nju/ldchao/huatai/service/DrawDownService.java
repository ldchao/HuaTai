package cn.edu.nju.ldchao.huatai.service;

import cn.edu.nju.ldchao.huatai.exception.DataProcessingException;

import java.util.List;

/**
 * Created by ldchao on 2019/10/26.
 */
public interface DrawDownService {

    Double getMaxDrawDown(List<String> idList) throws DataProcessingException;

    List<String> getAllIds();
}
