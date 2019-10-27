package cn.edu.nju.ldchao.huatai.vo;

import lombok.Data;

/**
 * Created by ldchao on 2019/10/28.
 */
@Data
public class IdVO {
    private String id;
    private Double proportion;

    @Override
    public String toString() {
        return "{" + id + "," + proportion + '}';
    }
}
