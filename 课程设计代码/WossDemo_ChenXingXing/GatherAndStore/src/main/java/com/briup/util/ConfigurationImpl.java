package com.briup.util;

import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import com.briup.woss.common.ConfigurationAWare;
import com.briup.woss.common.WossModule;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;
import com.briup.woss.util.BackUP;
import com.briup.woss.util.Configuration;
import com.briup.woss.util.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigurationImpl implements Configuration {

    String filePath="GatherAndStore\\src\\main\\java\\com\\briup\\File\\conf.xml";
    //存入woss模块对象
    Map<String,WossModule> wossMap=new HashMap<>();
    //存放配置信息
    Properties pro=new Properties();
    @Override
    public BackUP getBackup() throws Exception {
        return (BackUP) wossMap.get("backup");
    }

    @Override
    public Logger getLogger() throws Exception {
        return (Logger) wossMap.get("logger");
    }

    @Override
    public Server getServer() throws Exception {
        return (Server) wossMap.get("server");
    }

    @Override
    public DBStore getDbStore() throws Exception {
        return (DBStore) wossMap.get("dbstore");
    }

    @Override
    public Client getClient() throws Exception {
        return (Client) wossMap.get("client");
    }

    @Override
    public Gather getGather() throws Exception {
        return (Gather) wossMap.get("gather");
    }


    public static void main(String[] args) throws Exception {
        new ConfigurationImpl().getDbStore();
    }

    public ConfigurationImpl() {
        try {
            //1.获取解析器。读取conf.xml
            //创建SAXReader读取器，专门用于读取xml
            SAXReader saxReader=new SAXReader();
            //2.获取根节
            Document document=saxReader.read(filePath);
            Element rootElement=document.getRootElement();
            //3.获取子节点--属性值
            List elements=rootElement.elements();
            for(Object object:elements){
                Element e=(Element)object;
                String name=e.getName();
                String attValue=e.attributeValue("class");
                //通过反射获取对象
                WossModule woss;
                try {
                    woss = (WossModule)Class.forName(attValue).newInstance();
                    //System.out.println(woss);
                    wossMap.put(name, woss);
                    //System.out.println(name);
                    for(String key:wossMap.keySet()){
                    //System.out.println(key+":"+wossMap.get(key));
                    //4.固定值-->Properties
                    List ee=e.elements();
                    for(Object obj:ee){
                        Element el=(Element)obj;
                        String key1=el.getName();
                        String value=el.getText();
//						System.out.println(key1);
//						System.out.println(el.getName()+"*:*"+el.getText());
                        pro.put(key1, value);
                        String po=(String)pro.get("po");
                    }
                    //配置信息依赖注入
                    for(Object obj:wossMap.values()){
                        //调用init（）方法，注入配置信息
                        if(obj instanceof WossModule){
                            ((WossModule) obj).init(pro);
                        }
                        if(obj instanceof ConfigurationAWare){
                            ((ConfigurationAWare)obj).setConfiguration(this);
                        }
                    }
                    }
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
