package com.hongtu.slice.util;


import com.google.common.base.Throwables;
import com.hongtu.slice.component.MDSFileFactory;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import jdk.internal.util.xml.impl.Input;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.*;

public class Tools {
    private Logger LOGGER = LoggerFactory.getLogger(Tools.class);

    public static void main(String[] args) {

        try {
            String littleMDS = "/home/mi/project/slicePro/sliceapi/data/1.mds";
            String largeMDS = "/home/mi/document/1.mds";
            MDSFileFactory mdsFileFactory = new MDSFileFactory();
            StopWatch sw = new StopWatch();
            sw.start();
            SlicePositionParameter slicePositionParameter=new SlicePositionParameter(littleMDS,14,0,0);
            for (int i = 0; i < 1000; i++) {
                byte[] data = mdsFileFactory.getTileData(slicePositionParameter);
            }
            sw.stop();
            System.out.println("耗时间：" + sw.getTotalTimeMillis());
            int i = 0;
        } catch (Exception ex) {
            System.out.println(Throwables.getStackTraceAsString(ex));
        }
    }

}
