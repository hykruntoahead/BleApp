package com.example.heyukun.bleapp.ble.e;

import com.example.heyukun.bleapp.MyApp;
import com.example.heyukun.bleapp.R;
import com.example.heyukun.bleapp.ble.BleErrorCode;

/**
 * Created by heyukun on 2017/9/1.
 */
public class WarnEntity {
    /**
     * code
     * name 报警名称
     * prompt 报警提示内容
     * solution 报警解除方案
     */

    private int code;
    private String name;
    private String prompt;
    private String solution;


    public WarnEntity(int code) {
        this.code = code;
    }

    public String getName() {
        switch (this.code) {
            case BleErrorCode.WARN_POWER_OVER_VOLTAGE:
                this.name = MyApp.getAppInstance().getString(R.string.warn_name_POWER_OVER_VOLTAGE);

                break;
            case BleErrorCode.WARN_POWER_UNDER_VOLTAGE:
                this.name = MyApp.getAppInstance().getString(R.string.warn_name_POWER_UNDER_VOLTAGE);

                break;
            case BleErrorCode.WARN_OVER_LOAD:
                this.name = MyApp.getAppInstance().getString(R.string.warn_name_OVER_LOAD);

                break;
            case BleErrorCode.WARN_SUPER_HEAT:
                this.name = MyApp.getAppInstance().getString(R.string.warn_name_SUPER_HEAT);

                break;
            case BleErrorCode.WARN_COLLISION:
                this.name = MyApp.getAppInstance().getString(R.string.warn_name_COLLISION);

                break;
            case BleErrorCode.WARN_STEP_OUT:
                this.name = MyApp.getAppInstance().getString(R.string.warn_name_STEP_OUT);

                break;
            case BleErrorCode.WARN_NO_HOLZER:
                this.name = MyApp.getAppInstance().getString(R.string.warn_name_NO_HOLZER);

                break;

            case BleErrorCode.WARN_UN_KNOW:
                this.name = MyApp.getAppInstance().getString(R.string.warn_name_UN_KNOW);

                break;
            default:
                break;
        }
        return name;
    }

    public String getPrompt() {
        switch (this.code) {
            case BleErrorCode.WARN_POWER_OVER_VOLTAGE:
                this.prompt = MyApp.getAppInstance().getString(R.string.warn_prompt_POWER_OVER_VOLTAGE);

                break;
            case BleErrorCode.WARN_POWER_UNDER_VOLTAGE:
                this.prompt = MyApp.getAppInstance().getString(R.string.warn_prompt_POWER_UNDER_VOLTAGE);

                break;
            case BleErrorCode.WARN_OVER_LOAD:
                this.prompt = MyApp.getAppInstance().getString(R.string.warn_prompt_OVER_LOAD);

                break;
            case BleErrorCode.WARN_SUPER_HEAT:
                this.prompt = MyApp.getAppInstance().getString(R.string.warn_prompt_SUPER_HEAT);

                break;
            case BleErrorCode.WARN_COLLISION:
                this.prompt = MyApp.getAppInstance().getString(R.string.warn_prompt_COLLISION);

                break;
            case BleErrorCode.WARN_STEP_OUT:
                this.prompt = MyApp.getAppInstance().getString(R.string.warn_prompt_STEP_OUT);

                break;
            case BleErrorCode.WARN_NO_HOLZER:
                this.prompt = MyApp.getAppInstance().getString(R.string.warn_prompt_NO_HOLZER);

                break;

            case BleErrorCode.WARN_UN_KNOW:
                this.prompt = MyApp.getAppInstance().getString(R.string.warn_prompt_UN_KNOW);

                break;
            default:
                break;
        }
        return prompt;
    }

    public String getSolution() {
        switch (this.code) {
            case BleErrorCode.WARN_POWER_OVER_VOLTAGE:
                this.solution = MyApp.getAppInstance().getString(R.string.warn_solution_POWER_OVER_VOLTAGE);

                break;
            case BleErrorCode.WARN_POWER_UNDER_VOLTAGE:
                this.solution = MyApp.getAppInstance().getString(R.string.warn_solution_POWER_UNDER_VOLTAGE);

                break;
            case BleErrorCode.WARN_OVER_LOAD:
                this.solution = MyApp.getAppInstance().getString(R.string.warn_solution_OVER_LOAD);

                break;
            case BleErrorCode.WARN_SUPER_HEAT:
                this.solution = MyApp.getAppInstance().getString(R.string.warn_solution_SUPER_HEAT);

                break;
            case BleErrorCode.WARN_COLLISION:
                this.solution = MyApp.getAppInstance().getString(R.string.warn_solution_COLLISION);

                break;
            case BleErrorCode.WARN_STEP_OUT:
                this.solution = MyApp.getAppInstance().getString(R.string.warn_solution_STEP_OUT);

                break;
            case BleErrorCode.WARN_NO_HOLZER:
                this.solution = MyApp.getAppInstance().getString(R.string.warn_solution_NO_HOLZER);

                break;

            case BleErrorCode.WARN_UN_KNOW:
                this.solution = MyApp.getAppInstance().getString(R.string.warn_solution_UN_KNOW);

                break;
            default:
                break;
        }
        return solution;
    }
}
