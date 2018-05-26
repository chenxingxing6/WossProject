package com.briup.server;

import com.briup.woss.bean.BIDR;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;
import com.briup.woss.util.Configuration;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

public class ServerImpl implements Server{
    private ServerSocket ss;
    private int port;
    private ObjectInputStream oi;
    Configuration conf;

    @Override
    public void init(Properties p) {
        port=Integer.parseInt((String) p.get("port"));
    }
    @Override
    public void reciver() throws Exception {
        //创建服务器端Socket，并绑定在某一端口上
        ss=new ServerSocket(port);
        //接收客户请求，获取客户端Socket
        while(true) {
            Socket s = ss.accept();
            //通过客户端Socket，获取客户端的输入流
            oi = new ObjectInputStream(s.getInputStream());
            List<BIDR> c = (List<BIDR>) oi.readObject();
            for (int i=0;i<c.size();i++){
                System.out.println(c.get(i).getLogin_ip());
            }
            DBStore dbStore = conf.getDbStore();
            dbStore.saveToDB(c);
        }

    }

    @Override
    public void shutDown() throws Exception {
        Socket s = ss.accept();
        s.shutdownOutput();
    }


    @Override
    public void setConfiguration(Configuration configuration) {
        this.conf=configuration;
    }
}
