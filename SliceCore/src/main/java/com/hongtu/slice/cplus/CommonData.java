package com.hongtu.slice.cplus;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class CommonData extends Structure {
    public static class ByValue extends CommonData implements Structure.ByValue {}
    public Pointer pointer;
    public int len;
}
