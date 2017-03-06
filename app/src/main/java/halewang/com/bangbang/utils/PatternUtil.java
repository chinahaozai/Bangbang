package halewang.com.bangbang.utils;

import java.util.regex.Pattern;

/**
 * Created by halewang on 2017/1/6.
 */

public class PatternUtil {

    /**
     * 正则表达式:验证密码(不包含特殊字符)
     */
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式:验证手机号
     */
    private static final String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";

    public static boolean matchUserName(String userName){
        return Pattern.matches(REGEX_MOBILE, userName);
    }

    public static boolean matchPassword(String password){
        return Pattern.matches(REGEX_PASSWORD, password);
    }
}
