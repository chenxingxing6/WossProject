package com.briup.cxx.woss.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class WorssMapReduce extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        //作业配置
        //获取configuration对象
        Configuration conf = getConf();
        //定义数据输入输出路径
        Path input=new Path(conf.get("input"));
        Path output= new Path(conf.get("output"));
        //定义作业对象.获取job对象
        Job job=Job.getInstance(conf,"WossMR");//单例模式
        //设置作业需要运行的类
        job.setJarByClass(this.getClass());
        //job.setJarByClass(WorssMapReduce.class);.class 反射机制
        //设置和map阶段相关的信息
        job.setMapperClass(WorssMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        //设置mapper端数据输入的格式及输入的路径
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);
        //设置reduce端相关信息
        //设置reduce执行的名字
        job.setReducerClass(WorssReduce.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        /*map输出-reduce输入 由框架决定*/
        job.setOutputFormatClass(TextOutputFormat.class);//输出格式
        TextOutputFormat.setOutputPath(job,output);
        //set add 输出路径只有一个，输入路径可有很多个

        //提交作业
        //true 占用终端
        //false
        boolean comp = job.waitForCompletion(true);
        //if(comp) return 0;
        //else return 1;
        return comp?0:1;
    }

    static class WorssMapper extends Mapper<LongWritable,Text,Text,Text>{
    //LongWritable：偏移量（字节数0 +换行符）
    //loginip当做key
    //mapper找到相同loginip交给reduce
        private WossDataParser parser = new WossDataParser();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            parser.parse(value);
            if(parser.isValid()){
                context.write(new Text(parser.getLoginIp()),value);
            }
        }
    }


    //使用sqoop2->mysql
    static class WorssReduce extends Reducer<Text,Text,NullWritable,Text>{
        private WossDataParser parser = new WossDataParser();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //map集合不可迭代
        //单链集合Iterable 最底层
            String result="";
            StringBuilder sb = new StringBuilder();
            for (Text value : values) {
                //value代表一行数据
                parser.parse(value);
                if(parser.isValid()){
                    String aaaName=null;
                    String loginIp=null;
                    Long loginDate=null;
                    Long logoutDate=null;
                    String nasIp=null;
                    Long timeDuration=null;
                    if("7".equals(parser.getFlag())){
                        aaaName=parser.getAaaName();
                        loginIp=parser.getLoginIp();
                        loginDate=parser.getTime();
                        nasIp=parser.getNasIp();
                    }else if("8".equals(parser.getFlag())){
                        logoutDate=parser.getTime();
                        timeDuration=logoutDate-loginDate;
                    }
                    sb.append(aaaName).append("\t");
                    sb.append(loginDate).append("\t");
                    sb.append(logoutDate).append("\t");
                    sb.append(loginIp).append("\t");
                    sb.append(nasIp).append("\t");
                    sb.append(timeDuration).append("\t");
                    context.write(
                            NullWritable.get(),
                            new Text(sb.toString()));
                }
            }
        }
    }
    //静态方法中不能用this
    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new WorssMapReduce(),args));
    }
}
