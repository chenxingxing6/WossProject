package com.briup.test;
import com.briup.client.GatherImpl;
import com.briup.server.DBStoreimpl;
import com.briup.woss.bean.BIDR;
import org.junit.Test;

import java.util.List;

public class main{

    @Test
    public void test(){
        GatherImpl aa = new GatherImpl();
        try {
            //数据采集及入库
            List<BIDR> list = (List<BIDR>) aa.gather();
            DBStoreimpl db = new DBStoreimpl();
            db.saveToDB(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
