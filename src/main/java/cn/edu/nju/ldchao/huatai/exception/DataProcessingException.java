package cn.edu.nju.ldchao.huatai.exception;

/**
 * Created by ldchao on 2019/10/26.
 */
public class DataProcessingException extends Exception {
    private static final long serialVersionUID = -1L;
    private double value;

    public DataProcessingException(String message, double value) {
        super(message);
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
