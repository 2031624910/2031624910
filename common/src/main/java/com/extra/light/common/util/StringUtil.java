package com.extra.light.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 字符串处理工具类
 *
 * @author admin
 */
@Slf4j
public class StringUtil {

    /**
     * 对象为空
     *
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (null == object) {
            return true;
        }
        if (object instanceof String && "".equals(((String) object).trim())) {
            return true;
        }
        if (object instanceof List && ((List) object).size() == 0) {
            return true;
        }
        if (object instanceof Map && ((Map) object).isEmpty()) {
            return true;
        }
        if (object instanceof CharSequence && ((CharSequence) object).length() == 0) {
            return true;
        }
        return object instanceof Arrays && (Array.getLength(object) == 0);
    }

    /**
     * 对象不为空
     *
     * @param object
     * @return
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * 查询字符串中某个字符首次出现的位置 从1计数
     *
     * @param string 字符串
     * @param c
     * @return
     */
    public static int strFirstIndex(String c, String string) {
        Matcher matcher = Pattern.compile(c).matcher(string);
        if (matcher.find()) {
            return matcher.start() + 1;
        } else {
            return -1;
        }
    }

    /**
     * 两个对象是否相等
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 instanceof String && obj2 instanceof String) {
            obj1 = ((String) obj1).replace("\\*", "");
            obj2 = ((String) obj2).replaceAll("\\*", "");
            if (obj1.equals(obj2) || obj1 == obj2) {
                return true;
            }
        }
        if (obj1.equals(obj2) || obj1 == obj2) {
            return true;
        }
        return false;
    }

    /**
     * 字符串两两倒叙
     * 仅适用于偶数位字符串
     *
     * @param str
     */
    public static String reverseStr2(String str) {
        //转换
        String[] strArray = new String[(str.length()) / 2];
        for (int i = 0; i < str.length(); i = i + 2) {
            strArray[i / 2] = str.substring(i, i + 2);
        }
        Collections.reverse(Arrays.asList(strArray));
        StringBuilder newStr = new StringBuilder();
        for (String s : strArray) {
            newStr.append(s);
        }
        return newStr.toString();
    }

    /**
     * DLT645校验码
     *
     * @param data
     * @return
     */
    public static String makeDLT645CheckNum(String data) {
        return makeChecksum(data, true);
    }


    /**
     * 16进制 取低位校验和
     *
     * @param data
     * @return
     */
    public static String makeChecksum(String data) {
        return makeChecksum(data, false);
    }


    private static String makeChecksum(String data, boolean isDlt645) {
        if (StringUtil.isEmpty(data)) {
            return "";
        }
        int iTotal = 0;
        int iLen = data.length();
        int iNum = 0;
        while (iNum < iLen) {
            String s = data.substring(iNum, iNum + 2);
            iTotal += Integer.parseInt(s, 16);
            iNum = iNum + 2;
            if (!isDlt645) {
                iLen = s.length();
            }
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int iMod = iTotal % 256;
        String sHex = Integer.toHexString(iMod);
        iLen = sHex.length();
        //如果不够校验位的长度，补0,这里用的是两位校验
        if (iLen < 2) {
            sHex = "0" + sHex;
        }
        return sHex.toUpperCase();
    }


    /**
     * 根据字节截取内容
     *
     * @param bytes   自定义字节数组
     * @param content 需要截取的内容
     * @return
     */
    public static String[] separatorByBytes(double[] bytes, String content) {
        String[] contentArray = new String[bytes.length];
        double[] array = new double[bytes.length + 1];
        array[0] = 0;
        //复制数组
        System.arraycopy(bytes, 0, array, 1, bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            content = content.substring((int) (array[i] * 2));
            contentArray[i] = content;
        }
        String[] strings = new String[bytes.length];
        for (int i = 0; i < contentArray.length; i++) {
            strings[i] = contentArray[i].substring(0, (int) (bytes[i] * 2));
        }
        return strings;
    }

    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText  源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }


    /**
     * 将字符串str每隔2个分割存入数组
     *
     * @param str
     * @return
     */
    public static String[] setStr(String str) {
        int m = str.length() / 2;
        if (m * 2 < str.length()) {
            m++;
        }
        String[] strings = new String[m];
        int j = 0;
        for (int i = 0; i < str.length(); i++) {
            if (i % 2 == 0) {
                //每隔两个
                strings[j] = "" + str.charAt(i);
            } else {
                strings[j] = strings[j] + str.charAt(i);
                j++;
            }
        }
        return strings;
    }


    /**
     * 定义一个StringBuffer，利用StringBuffer类中的reverse()方法直接倒序输出
     * 倒叙字符串
     *
     * @param s
     */
    public static String reverseString2(String s) {
        if (s.length() > 0) {
            StringBuilder buffer = new StringBuilder(s);
            return buffer.reverse().toString();
        } else {
            return "";
        }
    }

    /**
     * 将字符串中日期时间格式替换为日期格式
     *
     * @param condition 条件
     * @return {@link String}
     */
    public static String replaceDateTimeToDate(String condition) {
        List<String> dateTimeStrList = StringUtil.dateTimeSubAll(condition);
        if (StringUtil.isNotEmpty(dateTimeStrList)) {
            for (String dateTimeStr : dateTimeStrList) {
                List<String> dateStrList = StringUtil.dateSubAll(dateTimeStr);
                if (StringUtil.isNotEmpty(dateStrList)) {
                    condition = condition.replace(dateTimeStr, dateStrList.get(0));
                }
            }
        }
        return condition;
    }

    /**
     * 截取有日期时间分钟
     *
     * @param str str
     * @return {@link List}<{@link String}>
     */
    public static List<String> dateTimeMinuteSubAll(String str) {
        try {
            List<String> dateTimeStrList = new ArrayList<>();
            String regex = "[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}";
            Pattern pattern = compile(regex);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                String group = matcher.group();
                dateTimeStrList.add(group);
            }
            return dateTimeStrList;
        } catch (Exception e) {
            log.error("截取字符串中的所有日期时间时错误-->>" + e.getMessage());
            return Collections.emptyList();
        }
    }


    /**
     * 日期时间子
     * 截取字符串中的所有日期时间
     *
     * @param str str
     * @return {@link List}<{@link String}>
     */
    public static List<String> dateTimeSubAll(String str) {
        try {
            List<String> dateTimeStrList = new ArrayList<>();
            String regex = "[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}[:][0-9]{1,2}";
            Pattern pattern = compile(regex);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                String group = matcher.group();
                dateTimeStrList.add(group);
            }
            return dateTimeStrList;
        } catch (Exception e) {
            log.error("截取字符串中的所有日期时间时错误-->>" + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 截取字符串中的所有日期
     *
     * @param str
     * @return
     */
    public static List<String> dateSubAll(String str) {
        try {
            List<String> dateStrList = new ArrayList<>();
            Pattern pattern = compile("[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                String group = matcher.group();
                dateStrList.add(group);
            }
            return dateStrList;
        } catch (Exception e) {
            log.error("截取字符串中的所有日期时错误-->>" + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 获取随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /*方法二：推荐，速度最快
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */

    public static boolean isNumber(String str) {
        String regex = "^[-\\+]?[\\d]*$";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(str);
        return matcher.matches();
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     *
     * @param code
     * @return
     */
    public static String autoGenericCode(String code, int num) {
        return String.format("%0" + num + "d", Integer.parseInt(code));
    }

    /**
     * @param data 指定字符串
     * @param str  需要定位的特殊字符或者字符串
     * @param num  第n次出现
     * @return 第n次出现的位置索引
     */
    public static int getIndexOf(String data, String str, int num) {
        Pattern pattern = Pattern.compile(str);
        Matcher findMatcher = pattern.matcher(data);
        //标记遍历字符串的位置
        int indexNum = 0;
        while (findMatcher.find()) {
            indexNum++;
            if (indexNum == num) {
                break;
            }
        }
        return findMatcher.start();
    }

    /**
     * 转化为小写
     *
     * @param c
     * @return
     */
    public static char toLowerCaseChar(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char) (c + 32);
        }
        return c;
    }

    /**
     * 转化为大写
     *
     * @param c
     * @return
     */
    public static char toUpperCaseChar(char c) {
        if (c >= 'a' && c <= 'z') {
            return (char) (c - 32);
        }
        return c;
    }
}
