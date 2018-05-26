package com.briup.woss;

import com.briup.woss.bean.BIDR;
import com.briup.woss.util.BackUP;
import com.briup.woss.util.Configuration;
import com.briup.woss.util.Logger;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class BackupImpl implements BackUP{
    String filePath="C:\\Users\\cxx\\Desktop\\WossDemo_ChenXingXing\\GatherAndStore\\src\\main\\java\\com\\briup\\File";
    Configuration conf;
    Logger log;
    @Override
    public Object load(String key,boolean flag){
        try {
            log = conf.getLogger();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("开始加载备份文件");
        Map<String, BIDR> map = null;
        try {
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(filePath+"\\"+key));
        map=(Map<String, BIDR>) ois.readObject();
            log.debug("备份文件加载结束");
            for(String ke:map.keySet()){
                if (key.equals(key)){
                    return map.get(key);
                }
            }
            ois.close();//关流
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public void store(String key,Object data,boolean flag){
        try {
            log = conf.getLogger();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("开始备份文件文件");
        //通过key找备份文件，键值就是文件名
        File file=new File(filePath,key);
        if(file.exists()){
            System.out.println("文件已经存在!");
        }else {
            try {
                file.createNewFile();
                ObjectOutputStream is = new ObjectOutputStream(new FileOutputStream(file,flag));
                is.writeObject(data);
                is.flush();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.debug("文件备份完成");
        }
    }

    @Override
    public void setConfiguration(Configuration configuration){
        this.conf=configuration;
    }

    @Override
    public void init(Properties p){

    }

    public static void main(String[] args) {
        BackupImpl bb= new BackupImpl();
        bb.load("map.txt",true);
    }
}
