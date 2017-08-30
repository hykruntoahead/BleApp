package com.example.heyukun.bleapp;

/**
 * Created by heyukun on 2017/8/28.
 */

public class HexUtils {

    /**
     * 字节流转成十六进制表示
     */
    public static String encode(byte[] src) {
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < src.length; n++) {
            strHex = Integer.toHexString(src[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }

    /**
     * 字符串转成字节流
     */
    public static byte[] decode(String src) {
        int m = 0, n = 0;
        int byteLen = src.length() / 2; // 每两个字符描述一个字节
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
            ret[i] = Byte.valueOf((byte) intVal);
        }
        return ret;
    }


    //转hex字符串转字节数组
    public static byte[] HexToByteArr(String inHex) {
        inHex = inHex.replaceAll(" ", "");
        byte[] result;
        int hexLen = inHex.length();
        if (isOdd(hexLen) == 1) {
            hexLen++;
            result = new byte[(hexLen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexLen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexLen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    public static int isOdd(int num) {
        return num & 1;
    }

    //Hex字符串转byte
    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * 字节数组转为普通字符串（ASCII对应的字符）
     *
     * @param bytearray
     *            byte[]
     * @return String
     */
    public static String bytetoString(byte[] bytearray) {
        String result = "";
        char temp;

        int length = bytearray.length;
        for (int i = 0; i < length; i++) {
            temp = (char) bytearray[i];
            result += temp;
        }
        return result;
    }
}