package cn.sunline.tiny.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Yang Jian
 * @date : 2020/6/16 14:36
 */
public class Tools {
    //金额验证
    public static boolean isNumber(String str){
        Pattern pattern= Pattern.compile("\\d+(\\.\\d{1,2})?"); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(str);
        return match.matches();
    }
}
