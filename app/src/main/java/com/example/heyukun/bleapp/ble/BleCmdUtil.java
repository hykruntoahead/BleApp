package com.example.heyukun.bleapp.ble;

import android.util.Log;

import com.example.heyukun.bleapp.HexUtils;

/**
 * Created by heyukun on 2017/8/31.
 * 发送和返回的处理指令集
 * 1b + cmd + address + number + data + cs + 05
 */

public class BleCmdUtil {


    private static final String FRAME_TOP = "1B";//帧头
    private static final String FRAME_END = "05";//帧尾

    /**
     * 命令字
     */
    private static final String CMD_R = "PR";//读 2个ascii字符
    private static final String CMD_W = "PW";//写
    private static final String CMD_RUN= "CW";//运动控制

    /**
     * 寄存器地址
     */
    private static final String ADDRESS_HEIGHT = "1030";// 桌子高度

    private static final String ADDRESS_RUN = "2010"; //运动控制

    private static final String ADDRESS_ROAD = "2002";// 行程校准

    /**
     * 长度值
     */
    private static final String NUMBER_R = "01";
    private static final String NUMBER_RUN = "02";


    /**
     * 数据值
     */

    private static final String DATA_AUTO = "0001";//自学习
    private static final String DATA_RESET = "0002";//复位

    private static final String DATA_STOP = "00000000"; //停止

    private static final String DATA_UP = "00040000"; //上箭头按下
    private static final String DATA_UP_CANCEL = "00050000"; //上箭头弹起

    private static final String DATA_DOWN = "00060000"; //下箭头按下
    private static final String DATA_DOWN_CANCEL = "00070000"; //下箭头按下

    private static final String DATA_SET_HEIGHT = "0003";//指定高度（前半部分）


    /**
     * 非校验码功能字段
     */
    private static final String CODE_GET_HEIGHT = CMD_R+ADDRESS_HEIGHT+NUMBER_R;//获取高度


    private static final String CODE_AUTO_LEARN = CMD_W+ADDRESS_ROAD+NUMBER_R+DATA_AUTO;//自学习
    private static final String CODE_RESET = CMD_W+ADDRESS_ROAD+NUMBER_R+DATA_RESET;//复位


    private static final String CODE_RUN_STOP = CMD_RUN+ADDRESS_RUN+NUMBER_RUN+DATA_STOP;

    private static final String CODE_RUN_UP = CMD_RUN+ADDRESS_RUN+NUMBER_RUN+DATA_UP;
    private static final String CODE_RUN_UP_CANCEL =CMD_RUN+ADDRESS_RUN+NUMBER_RUN+DATA_UP_CANCEL;

    private static final String CODE_RUN_DOWN = CMD_RUN+ADDRESS_RUN+NUMBER_RUN+DATA_DOWN;
    private static final String CODE_RUN_DOWN_CANCEL=CMD_RUN+ADDRESS_RUN+NUMBER_RUN+DATA_DOWN_CANCEL;

    private static final String CODE_RUN_SET_HEIGHT=CMD_RUN+ADDRESS_RUN+NUMBER_RUN+DATA_SET_HEIGHT;


    private static String addToCompleteStr(String code){
        return FRAME_TOP+code+FRAME_END;
    }



    /**
     * @return 获取设备高度的发送指令
     */
    public static String getReadHeightCmd(){
       return addToCompleteStr(HexUtils.generateCheckCode(CODE_GET_HEIGHT));
    }

    /**
     *
     * @return 行程校准--自学习
     */
    public static String getAutoLearnCmd(){
        return addToCompleteStr(HexUtils.generateCheckCode(CODE_AUTO_LEARN));
    }

    /**
     *
     * @return 行程校准--复位
     */
    public static String getResetCmd(){
        return addToCompleteStr(HexUtils.generateCheckCode(CODE_RESET));
    }


    /**
     *
     * @return 运动控制--停止
     */
    public static String getStopCmd(){
        return addToCompleteStr(HexUtils.generateCheckCode(CODE_RUN_STOP));
    }

    /**
     *
     * @return 运动控制--上按键按下
     */
    public static String getUpCmd(){
        return addToCompleteStr(HexUtils.generateCheckCode(CODE_RUN_UP));
    }

    /**
     *
     * @return 运动控制--上按键弹起
     */
    public static String getUpCancelCmd(){
        return addToCompleteStr(HexUtils.generateCheckCode(CODE_RUN_UP_CANCEL));
    }

    /**
     *
     * @return 运动控制--下按键按下
     */
    public static String getDownCmd(){
        return addToCompleteStr(HexUtils.generateCheckCode(CODE_RUN_DOWN));
    }

    /**
     *
     * @return 运动控制--下按键弹起
     */
    public static String getDownCancelCmd(){
        return addToCompleteStr(HexUtils.generateCheckCode(CODE_RUN_DOWN_CANCEL));
    }




    /**
     *
     * @param height 高度值 单位为 0.1mm
     * @return 指定高度cmd
     */
    public static String getSetHeightCmd(int height){
        return addToCompleteStr(HexUtils.generateCheckCode(CODE_RUN_SET_HEIGHT + HexUtils.toHexData(height * 100)));
    }


    public static int getReturnHeight(String RecStr){
        String height = RecStr.substring(4, RecStr.length() - 6);
        String hex = HexUtils.hexToAscii(height);
        Log.d("Ble-","return-"+Integer.parseInt(hex,16));
        return Integer.parseInt(hex,16)/10;
    }
}
