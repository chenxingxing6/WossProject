package com.briup.client;

import com.briup.util.LoggerImpl;
import com.briup.woss.BackupImpl;
import com.briup.woss.bean.BIDR;
import com.briup.woss.client.Gather;
import com.briup.woss.common.ConfigurationAWare;
import com.briup.woss.util.BackUP;
import com.briup.woss.util.Configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.*;

public class GatherImpl implements Gather,ConfigurationAWare{
    String pathName;
    List<BIDR> list=new ArrayList<BIDR>();
    Map<String,BIDR> map=new HashMap();
    Configuration conf=null;
    @Override
    public void init(Properties p) {
        pathName=(String) p.get("src-file");
    }

    @Override
    public Collection<BIDR> gather() throws Exception {
        System.out.println("======="+pathName);
        InputStream is = ClassLoader.getSystemResourceAsStream(pathName);
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        LoggerImpl log = new LoggerImpl();
        log.debug("采集数据");
        String filebackname ="map.txt";
        BackUP bi=conf.getBackup();
        Map<String, BIDR> newmap= (Map<String, BIDR>)bi.load(filebackname,bi.LOAD_REMOVE);
        if (newmap!=null){
            System.out.println(newmap.toString());
        }
        String str ="";
        while ((str=br.readLine())!=null){
            String[] line = str.split("[|]");
            if (line[2].equals("7")){
                BIDR bidr = new BIDR();
                bidr.setAAA_login_name(line[0].substring(1));
                bidr.setNAS_ip(line[1]);
                Long login_date=Long.parseLong(line[3]);
                Timestamp login_time=new Timestamp(login_date*1000);
                bidr.setLogin_date(login_time);
                bidr.setLogin_ip(line[4]);
                map.put(line[4],bidr);
            }else if(line[2].equals("8")){
                BIDR bidr1 = map.get(line[4]);
                //有7有8
                if(bidr1!=null){
                    Long logout_date=Long.parseLong(line[3]);
                    Timestamp logout_time=new Timestamp(logout_date*1000);
                    bidr1.setLogout_date(logout_time);
                    Integer time_deration=(int) (logout_date - (bidr1.getLogin_date().getTime())/1000);
                    bidr1.setTime_duration(time_deration);
                    list.add(bidr1);
                    map.remove(line[4]);
                }
            }
        }
        bi.store(filebackname,map,true);
        System.out.println(list.size());
        br.close();
        return list;
    }


    @Override
    public void setConfiguration(Configuration co) {
        this.conf=co;
    }

    public static void main(String[] args) throws Exception {
        GatherImpl aa = new GatherImpl();
        List<BIDR> list= (List<BIDR>) aa.gather();
    }


}
