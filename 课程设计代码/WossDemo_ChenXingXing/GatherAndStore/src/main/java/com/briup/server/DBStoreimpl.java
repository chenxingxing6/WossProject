package com.briup.server;

import com.briup.util.LoggerImpl;
import com.briup.woss.bean.BIDR;
import com.briup.woss.server.DBStore;
import com.briup.woss.util.BackUP;
import com.briup.woss.util.Configuration;
import com.briup.woss.util.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class DBStoreimpl implements DBStore {
    private PreparedStatement pres;
    private Connection conn=null;
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    static int i = 0;
    private static String pathName = "src/main/java/com/briup/File/list.txt";
    Configuration conf;
    @Override
    public void init(Properties p) {
        driver=p.getProperty("driver");
        url=p.getProperty("url");
        username=p.getProperty("userName");
        password=p.getProperty("passWord");
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.conf=configuration;
    }

    @Override
    public void saveToDB(Collection<BIDR> collection) throws Exception {
        Logger log = null;
        BackUP bi=null;
        List<BIDR> list = (List<BIDR>) collection;
        try {
            log = conf.getLogger();
            bi = conf.getBackup();
            Class.forName(driver); //classLoader,加载对应驱动
            log.debug("加载mysql驱动");
            conn = (Connection) DriverManager.getConnection(url, username, password);
        log.debug("保存完整用户数据");
        int batchSize = 5000;//设置批量处理的数量
        pres = conn.prepareStatement("insert into t_detail() "
                + "values (?,?,?,?,?,?)");
        conn.setAutoCommit(false);// 关闭事务自动提交
        for (int j = 0; j < list.size(); j++){
            ++i;
            pres.setString(1,list.get(j).getAAA_login_name());
            pres.setString(2,list.get(j).getLogin_ip());
            pres.setTimestamp(3,list.get(j).getLogin_date());
            pres.setTimestamp(4,list.get(j).getLogout_date());
            pres.setString(5,list.get(j).getNAS_ip());
            pres.setInt(6,list.get(j).getTime_duration());
            pres.addBatch();
            if ( i % batchSize == 0 ) {
                pres.executeBatch();
                conn.commit();
            }
        }
        if ( i % batchSize != 0 ) {
            pres.executeBatch();
            conn.commit();
        }
        if(pres!=null){
            pres.close();
        }
        if(conn!=null){
            conn.close();
        }
            log.info("入库数据的个数" + list.size());
            collection.removeAll(list);
            log.info("未入库数据的个数" + collection.size());
        } catch (Exception e) {
            conn.rollback();
            bi.store(pathName, list, BackUP.STORE_OVERRIDE);
            log.debug("备份数据为"+list.size());
            log.debug("未入库数据的个数" + collection.size());
            log.debug("插入个数为："+i);
        }
    }

    public static void main(String[] args) throws IOException {
        //DBStoreimpl db = new DBStoreimpl();
    }
}