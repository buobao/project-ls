package com.vocinno.centanet.tools;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by hewei26 on 2016/6/3.
 */
public class DivideUtils {

    public static boolean isWan = true;

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param b1    被除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static BigDecimal divide(BigDecimal b1, int scale) {
        BigInteger bt = new BigInteger(b1.toString());
        BigInteger btLimit = new BigInteger("100000000");

        BigDecimal bdDivide = null;
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (bt.compareTo(btLimit) < 0) {
            //万
            bdDivide = b1.divide(new BigDecimal(Math.pow(10, 4)), 0, BigDecimal.ROUND_HALF_UP);
            isWan = true;
        } else{
            //亿
            bdDivide = b1.divide(new BigDecimal(Math.pow(10, 8)), scale, BigDecimal.ROUND_HALF_UP);
            isWan = false;
        }
        return bdDivide;
    }
}
