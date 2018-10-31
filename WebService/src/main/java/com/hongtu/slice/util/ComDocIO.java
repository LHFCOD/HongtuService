package com.hongtu.slice.util;

import com.google.common.base.Throwables;
import org.apache.poi.poifs.filesystem.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class ComDocIO {
    private Logger LOGGER = LoggerFactory.getLogger(ComDocIO.class);
    private POIFSFileSystem fs;
    DirectoryEntry root;

    byte[] readFromPath(String path) {
        byte[] content = new byte[0];
        String[] path_list = path.substring(1).split("/");
        try {
            DirectoryEntry entry = root;
            for (int i = 0; i < path_list.length - 1; i++) {
                Entry temp_entry = entry.getEntry(path_list[i]);
                if (temp_entry instanceof DirectoryEntry) {
                    entry = (DirectoryEntry) temp_entry;
                } else {
                    throw new Exception("temp_entry is not a DirectoryEntry");
                }
            }
            Entry temp_entry = entry.getEntry(path_list[path_list.length - 1]);
            if (temp_entry instanceof DocumentEntry) {
                DocumentEntry documentEntry = (DocumentEntry) temp_entry;
                DocumentInputStream stream = new DocumentInputStream(documentEntry);
                content = new byte[stream.available()];
                stream.read(content);
            } else {
                throw new Exception("temp_entry is not a DocumentEntry");
            }
        } catch (Exception ex) {
            LOGGER.error("ReadFromPath error occur:{}", Throwables.getStackTraceAsString(ex));
        }
        return content;
    }

    public ComDocIO(String filename) {
        try {
            NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(filename));
            root = fs.getRoot();
        } catch (Exception ex) {
            LOGGER.error("Read compound file faild :{}", Throwables.getStackTraceAsString(ex));
        }
    }
}
