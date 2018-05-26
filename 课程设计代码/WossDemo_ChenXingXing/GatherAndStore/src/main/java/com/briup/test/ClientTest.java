package com.briup.test;

import com.briup.client.ClientImpl;
import com.briup.client.GatherImpl;
import com.briup.util.ConfigurationImpl;
import com.briup.util.LoggerImpl;
import com.briup.woss.bean.BIDR;
import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import com.briup.woss.util.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class ClientTest {
    public void clientstart(){
        ConfigurationImpl conf = new ConfigurationImpl();
        Logger log = null;
        try {
            log = conf.getLogger();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // 1.采集数据
            log.debug("开始采集数据");
            Gather aa = conf.getGather();
            List<BIDR> list= (List<BIDR>) aa.gather();
            //2.启动服务器端
            Client client = conf.getClient();
            log.debug("启动客户端");
            //3.发送数据
            log.debug("数据开始传输");
            client.send(list);
        } catch (Exception e) {
            log.error("服务器端启动异常");
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

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(100,20,555,100);
        panel.add(scrollPane);
        JTextArea userText = new JTextArea(100,100);
        userText.setBounds(100,20,555,100);
        userText.setText("日志信息...\n");
        panel.add(userText);
        scrollPane.setViewportView(userText);
        JButton startButton = new JButton("start");
        startButton.setBounds(10, 30, 80, 25);
        panel.add(startButton);

        JButton stopButton = new JButton("stop");
        stopButton.setBounds(10, 83, 80, 25);
        panel.add(stopButton);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClientTest test = new ClientTest();
                test.clientstart();
                userText.append("开启服务\n");
                File file = new File("C:\\Users\\cxx\\Desktop\\WossDemo_ChenXingXing\\GatherAndStore\\src\\main\\java\\com\\briup\\File\\log.txt");
                try {
                    String lastLine = readLastLine(file,"GBK");
                    userText.append(lastLine+"\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("客户端");
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
