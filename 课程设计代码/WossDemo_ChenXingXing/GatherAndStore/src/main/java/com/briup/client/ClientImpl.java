package com.briup.client;

import com.briup.woss.bean.BIDR;
import com.briup.woss.client.Client;
import com.briup.woss.util.Configuration;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

public class ClientImpl implements Client {
    private static String id;
    private static int port;
    private static ObjectOutputStream oo;
    Configuration conf;
    @Override
    public void init(Properties p) {
        id=p.getProperty("id");
        port=Integer.parseInt(p.getProperty("port"));
    }
    @Override
    public void send(Collection<BIDR> collection) throws Exception {
        Socket socket=new Socket(id,port);
        OutputStream os=socket.getOutputStream();
        oo=new ObjectOutputStream(os);
        System.out.println("********send");
        oo.writeObject(collection);
        System.out.println(collection);
        oo.flush();
        oo.close();
        socket.close();
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.conf=configuration;
    }
}

