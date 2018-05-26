package com.briup.test;
import com.briup.server.ServerImpl;
import com.briup.util.ConfigurationImpl;
import com.briup.util.LoggerImpl;
import com.briup.woss.server.Server;
import com.briup.woss.util.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerTest {

    public void ServerStart() throws Exception {
        ConfigurationImpl conf = new ConfigurationImpl();
        Logger log = conf.getLogger();
        try {
            //1.启动服务器
            log.info("服务器启动");
            Server server = conf.getServer();
            log.info("服务器完成");
            //2.接收数据
            log.info("等待接收数据");
            log.info("开始数据入库");
            server.reciver();
        } catch (Exception e) {
            log.error("服务器端启动异常······");
        }
    }

    public static String readLastLine(File file, String charset) throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            long len = raf.length();
            if (len == 0L) {
                return "";
            } else {
                long pos = len - 1;
                while (pos > 0) {
                    pos--;
                    raf.seek(pos);
                    if (raf.readByte() == '\n') {
                        break;
                    }
                }
                if (pos == 0) {
                    raf.seek(0);
                }
                byte[] bytes = new byte[(int) (len - pos)];
                raf.read(bytes);
                if (charset == null) {
                    return new String(bytes);
                } else {
                    return new String(bytes, charset);
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }

    public void stopserver(){
        ConfigurationImpl conf = new ConfigurationImpl();
        Logger log = null;
        try {
            log = conf.getLogger();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //1.关闭服务器
            log.info("服务器启动");
            System.exit(0);
            Server server = conf.getServer();
            server.shutDown();
        } catch (Exception e) {
            log.error("服务器端关闭异常······");
        }
    }

    private static void placeComponents(JPanel panel) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());
        panel.setLayout(null);
        JTextArea userText = new JTextArea(200,100);
        userText.setBounds(100,20,555,100);
        userText.setText(time+"日志信息...\n");
        panel.add(userText);
        // 创建登录按钮
        JButton startButton = new JButton("start");
        startButton.setBounds(10, 30, 80, 25);
        panel.add(startButton);

        JButton stopButton = new JButton("stop");
        stopButton.setBounds(10, 83, 80, 25);
        panel.add(stopButton);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ServerTest test = new ServerTest();
                userText.append(time+"开启服务\n");
                try {
                    test.ServerStart();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ServerTest test = new ServerTest();
                test.stopserver();
                userText.append("关闭服务\n");
            }
        });
    }

    public static void main(String[] args){
        // 创建 JFrame 实例
        JFrame frame = new JFrame("服务器");
        frame.setSize(700, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        placeComponents(panel);
        // 设置界面可见
        frame.setVisible(true);
	}
}
