package com.briup.util;
import com.briup.woss.util.Configuration;
import com.briup.woss.util.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

public class LoggerImpl implements Logger{
    public LoggerImpl(){
       PropertyConfigurator.configure("C:\\Users\\cxx\\Desktop\\WossDemo_ChenXingXing\\GatherAndStore\\src\\main\\resources\\log4j.properties");
    }
    org.apache.log4j.Logger logger=org.apache.log4j.Logger.getRootLogger();
    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void fatal(String msg) {
        logger.fatal(msg);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void setConfiguration(Configuration configuration) {

    }

    @Override
    public void init(Properties properties) {

    }
}
