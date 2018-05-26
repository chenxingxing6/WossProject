package com.briup.test;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class log4j {
    public static void main(String[] args) {
        Logger logger=Logger.getRootLogger();
        BasicConfigurator.configure();
        //PropertyConfigurator.configure("GatherAndStore\\src\\main\\resources\\log4j.properties");
        logger.setLevel(Level.INFO);
        logger.debug("log4j--debug");
        logger.info("log4j--info");
        logger.warn("log4j--warn");
        logger.error("log4j--error");
    }

}
