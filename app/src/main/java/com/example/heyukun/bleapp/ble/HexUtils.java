package com.example.heyukun.bleapp.ble;

/**
 * Created by heyukun on 2017/8/28.
 */

public class HexUtils {

    public static String getBit(byte by){
        StringBuffer sb = new StringBuffer();
        sb.append((by>>7)&0x1)
                .append((by>>6)&0x1)
                .append((by>>5)&0x1)
                .append((by>>4)&0x1)
                .append((by>>3)&0x1)
                .append((by>>2)&0x1)
                .append((by>>1)&0x1)
                .append((by>>0)&0x1);
        return sb.toString();
    }


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
     * 获取字节（有8bit）所在位（第num bit处）的数值
     * 为0还是1
     * 为1时返回true
     * @param by 字节
     * @param index 位置
     * @return
     */
    public static boolean getBitOnIndexIsTrue(byte by,int index){
        StringBuffer sb = new StringBuffer();
        sb.append((by>>index)&0x1);
        return sb.toString().equals("1");
    }

    /**
     *
     *ascii码 ---> HexStr
     *
     */

    public static String asciiStringToHex(String str){

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
    }


    /**
     *
     *HexStr ---> ascii码
     *
     */

    public static String hexToAscii(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "ASCII");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 校验码计算并拼接
     */

    public static String generateCheckCode(String str){

        String ascStr = hexToAscii(asciiStringToHex(str));

        int cs = 0 ;
        char[] chars = ascStr.toCharArray();
        for(char c:chars){
            cs = cs + c;
        }
        return asciiStringToHex(str)+asciiStringToHex(Integer.toHexString(cs % 256).toUpperCase());

    }


    public static String toHexData(int num){
        String hex = Integer.toHexString(num).toUpperCase();

        char[] chars = hex.toCharArray();
        if(chars.length==1){
            hex = "000"+hex;
        }else if (chars.length == 2){
            hex = "00"+hex;
        }else if (chars.length ==3){
            hex = "0"+hex;
        }
        return hex;
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