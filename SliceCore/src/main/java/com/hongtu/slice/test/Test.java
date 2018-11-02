package com.hongtu.slice.test;

import com.hongtu.slice.cplus.ComDocIO;

public class Test {
    public static void main(String[] args) {
        String littleMDS = "/home/mi/project/slicePro/sliceapi/data/1.mds";
        String largeMDS = "/home/mi/document/1.mds";
        ComDocIO comDocIO = new ComDocIO(littleMDS);
        byte[] bytes = comDocIO.readFromPath("/DSI0/MoticDigitalSlideImage");
        int i=0;
    }
}
