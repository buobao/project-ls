package com.vocinno.centanet.tools;

import java.math.BigDecimal;

/**
 * Created by hewei26 on 2016/6/3.
 */
public class DivideUtils {

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     * @param s1 被除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static BigDecimal div(String s1,int scale){
        BigDecimal str = null;
        if(scale<0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(s1);
        if(s1.length() <= 9){   //万
            str = b1.divide(new BigDecimal(Math.pow(10,4)),0,BigDecimal.ROUND_HALF_UP);
        }else if(s1.length() > 9){  //亿
            str = b1.divide(new BigDecimal(Math.pow(10,8)),scale,BigDecimal.ROUND_HALF_UP);
        }
        return str;
    }
}
