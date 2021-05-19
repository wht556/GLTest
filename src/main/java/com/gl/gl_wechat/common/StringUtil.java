
package com.gl.gl_wechat.common;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 字符串工具
 * </p>
 * ClassName: StringUtil <br/>
 * Author: Du.Hx  <br/>
 * Date: 2017/5/10 19:19 <br/>
 * Version: 1.0 <br/>
 */
public class StringUtil {

    /**
     * 默认随机数范围
     */
    public static final String DEFAULT_RANDOM_NUM = "0123456789";

    /**
     * 默认随机数字字母范围
     */
    public static final String DEFAULT_RANDOM_NUM_AND_LETTER =
            "0123456789abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * <p>
     * 判断字符或字符串串是否为空（null或""）
     * </p>
     * <pre>
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = false
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
     * </pre>
     * Author: Du.hx <br/>
     * Date: 2017/5/10 19:21
     *
     * @param str 判断的字符串
     * @return 如果字符串是空（null或""），返回true
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>
     * 判断字符串是否为空白（null、""或" "）
     * </p>
     * <pre>
     *     StringUtils.isBlank(null)      = true
     *     StringUtils.isBlank("")        = true
     *     StringUtils.isBlank(" ")       = true
     *     StringUtils.isBlank("bob")     = false
     *     StringUtils.isBlank("  bob  ") = false
     * </pre>
     * Author: Du.hx <br/>
     * Date: 2017/5/10 19:44
     *
     * @param str 判断的字符串
     * @return 如果字符串是空白（null、""或" "），返回true
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断List<?>  是否为空
     * 如果为空，返回true；
     * 否则返回 false
     * </>
     *
     * @param list
     * @return
     */
    public static boolean isEmptyList(List<?> list) {
        if (null == list || list.isEmpty() || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断List<?>  是否为空
     * 如果为空，返回true；
     * 否则返回 false
     * </>
     *
     * @param set
     * @return
     */
    public static boolean isEmptySet(Set<?> set) {
        if (null == set || set.isEmpty() || set.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * 根据指定字符分割字符串并生成数组
     * </p>
     * Author: Du.hx <br/>
     * Date: 2017/5/10 19:51
     *
     * @param str    待分割字符串
     * @param symbol 分割符
     * @return 分割后数组
     */
    public static String[] splitStr2Array(String str, String symbol) {
        if (!isBlank(str) && null != symbol) {
            return str.split(symbol);
        }
        return null;
    }

    /**
     * <p>
     * 根据指定字符组合字符串数组并生成字符串
     * </p>
     * Author: Du.hx <br/>
     * Date: 2017/5/10 19:58
     *
     * @param array  待组合数组
     * @param symbol 组合符
     * @return 以组合符连接数组元素的字符串
     */
    public static String uniteArray2Str(String[] array, String symbol) {
        StringBuilder sb = new StringBuilder();
        String result = "";
        if (null != array && null != symbol) {
            for (String temp : array) {
                if (null != temp && temp.trim().length() > 0) {
                    sb.append(temp);
                    sb.append(symbol);
                }
            }
            if (sb.length() > 1 && !isEmpty(symbol)) {
                result = sb.toString();
                result = result.substring(0, result.length() - symbol.length());
            }
        }
        return result;
    }

    /**
     * <p>
     * 指定长度，随机生成包含大小写字母、数字的字符串
     * </p>
     * Author: Du.hx <br/>
     * Date: 2017/5/12 11:40
     *
     * @param length   指定生成的长度
     * @param strRange 随机字符范围
     * @return 返回定长的随机字符串
     */
    public static String getRandomStr(int length, String strRange) {
        StringBuilder buffer = new StringBuilder(strRange);
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }


    /**
     * <sql转译> <将_和%转译成\_和\%>
     *
     * @param likeStr 需要转译的字符
     * @return 转译后的字符
     * @see [类、类#方法、类#成员]
     */
    public static String escapeSQLLike(String likeStr) {
        String str = StringUtils.replace(likeStr, "_", "/_");
        str = StringUtils.replace(str, "%", "/%");
        return str;
    }

    //将 截止时间的：时分  替换
    public static Date getNextDay(Date newDate, Date oldDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        Calendar newdar = Calendar.getInstance();
        newdar.set(newdar.get(Calendar.YEAR), newdar.get(Calendar.MONTH), newdar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        return newdar.getTime();
    }


    public final static boolean isBlank(Object obj) {
        if (obj == null || isBlank(obj.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象所有属性是否为  null 或  ""  或  " "
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static boolean isAllFieldNull(Object obj) throws Exception {
        Class stuCla = (Class) obj.getClass();// 得到类对象
        Field[] fs = stuCla.getDeclaredFields();//得到属性集合
        boolean flag = true;
        if (fs.length > 0) {
            for (Field f : fs) {//遍历属性
                f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
                String serialVersionUID = "serialVersionUID";  //去除序列号参数
                if (!f.getName().equals(serialVersionUID)) {
                    Object val = f.get(obj);// 得到此属性的值
                    try {
                        if (!isBlank(val)) {//只要有1个属性不为空,那么就不是所有的属性值都为空
                            flag = false;
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return flag;
    }


    public static void main(String[] args) {
   /*     List<CapitalAssertsUse> stringList = new ArrayList<CapitalAssertsUse>();
        CapitalAssertsUse use = new CapitalAssertsUse();
        use.setCapitalId("1");
        use.setEmployeeName("name");
        CapitalAssertsUse use1 = new CapitalAssertsUse();
        use1.setCapitalId("2");
        use1.setEmployeeName("name1");
        CapitalAssertsUse use2 = new CapitalAssertsUse();
        use2.setCapitalId("1");
        use2.setEmployeeName("name2");

        stringList.add(use);
        stringList.add(use1);
        stringList.add(use2);

        *//**
         * 对象：根据相同字段去重
         *//*
        Set<CapitalAssertsUse> set = new TreeSet<>(new Comparator<CapitalAssertsUse>() {
            @Override
            public int compare(CapitalAssertsUse o1, CapitalAssertsUse o2) {

                return o1.getCapitalId().compareTo(o2.getCapitalId());
            }
        });
        set.addAll(stringList);
        for (CapitalAssertsUse capitalAssertsUse:set){
            System.out.println(capitalAssertsUse.getCapitalId() + ":" + capitalAssertsUse.getEmployeeName());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,11,23,15,30);
        Date  date = new Date();
        Date  date1 = getNextDay(date,calendar.getTime());
        System.out.println(date1);*/


    }

}
