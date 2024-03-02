package xyz.kuailemao.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/8 14:12
 * 浏览器信息工具类
 */
public class BrowserUtil {

    /**
     * 获取浏览器名称及版本
     * @param request request
     * @return 名称 - 版本号
     */
    public static String browserName(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        Browser browser = ua.getBrowser();
        return browser.getName() + "-" + browser.getVersion(userAgent);
    }

    /**
     * 获取操作系统名称
     * @param request request
     * @return 名称
     */
    public static String osName(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        OperatingSystem os = ua.getOperatingSystem();
        return os.getName();
    }

}
