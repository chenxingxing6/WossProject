package com.briup.cxx.woss.mr;

import org.apache.hadoop.io.Text;

/**
 * 解析原始数据类
 */
public class WossDataParser {
    private String aaaName;
    private String nasIp;
    private String flag;
    private Long time;
    private String loginIp;
    //该字段判断解析的当前行数据是否是合理的数据
    private boolean valid;

    public void parse(String line){
        String[] strs = line.split("[|]");
        //如果分割的字符串数组长度小于5，改行数据不合理
        if (strs.length<5){
            valid=false;
            return;
        }
        aaaName=strs[0];
        nasIp=strs[1];
        flag=strs[2];
        time=Long.parseLong(strs[3])*1000;
        loginIp=strs[4];
        valid=true;
    }


    //重载parse方法
    //序列化java中占的字节更大，hadoop中类型占字节数小很多
    public void parse(Text line){
        parse(line.toString());
    }

    //get set


    public String getAaaName() {
        return aaaName;
    }

    public void setAaaName(String aaaName) {
        this.aaaName = aaaName;
    }

    public String getNasIp() {
        return nasIp;
    }

    public void setNasIp(String nasIp) {
        this.nasIp = nasIp;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
