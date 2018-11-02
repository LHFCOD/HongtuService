package com.hongtu.slice.cplus;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComDocIO {
    private Logger LOGGER = LoggerFactory.getLogger(ComDocIO.class);
    Pointer comDocFile;

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary("mds", CLibrary.class);

        Pointer ReadComDocFile(String fileName);

        void ReleaseComDocFile(Pointer comDocFile);

        Pointer ReadFromPath(Pointer mdsFile, String path);

        int GetBlockLength(Pointer block);

        Pointer GetBlockData(Pointer block);

        void ReleaseFileBlock(Pointer block);
    }

    public static void main(String[] args) {

    }

    @Override
    protected void finalize() throws Throwable {
        CLibrary.INSTANCE.ReleaseComDocFile(comDocFile);
    }

    public byte[] readFromPath(String path) {
        byte[] data = null;
        if (comDocFile != null) {
            Pointer block = CLibrary.INSTANCE.ReadFromPath(comDocFile, path);
            int len = CLibrary.INSTANCE.GetBlockLength(block);
            Pointer dataPointer = CLibrary.INSTANCE.GetBlockData(block);
            data = dataPointer.getByteArray(0, len);
            CLibrary.INSTANCE.ReleaseFileBlock(block);
        }
        return data;
    }

    public ComDocIO(String filename) {
        comDocFile = CLibrary.INSTANCE.ReadComDocFile(filename);
    }
}
