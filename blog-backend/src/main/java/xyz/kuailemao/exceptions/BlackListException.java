package xyz.kuailemao.exceptions;

/**
 * @author kuailemao
 * @since 2024/9/5 下午6:52
 * 自定义黑名单异常
 */
public class BlackListException extends Exception {
    public BlackListException(String message) {
        super(message);
    }
}
