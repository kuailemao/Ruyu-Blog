package xyz.kuailemao.service;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/16 17:06
 */
public interface PublicService {

    /**
     * 邮箱验证码发送
     * @param type 邮箱类型
     * @param email 邮箱地址
     * @return 是否成功
     */
    String registerEmailVerifyCode(String type, String email);

    /**
     * 邮箱通知
     * @param type 邮箱类型
     * @param email 邮箱地址
     */
    void sendEmail(String type, String email);
}
