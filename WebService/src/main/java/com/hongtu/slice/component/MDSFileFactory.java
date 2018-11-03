package com.hongtu.slice.component;

import com.google.common.base.Throwables;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.hongtu.slice.util.MDSInfo;
import com.hongtu.slice.util.SlicePositionParameter;
import com.hongtu.slice.util.MDSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class MDSFileFactory {
    private Logger LOGGER = LoggerFactory.getLogger(MDSFileFactory.class);
    private final String SETTINGS_PROPERTIES = "settings.properties";
    private Properties props;
    private GuanvaCacheGetter mdsFileCache;
    private GuanvaCacheGetter sliceCache;

    public void initConfig() {
        props = new Properties();
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream(SETTINGS_PROPERTIES));
        } catch (Exception ex) {
            LOGGER.error("init config failed : {}", Throwables.getStackTraceAsString(ex));
        }
    }

    public MDSFile generateMDSFile(String fileName) {
        MDSFile mdsFile = null;
        if (mdsFileCache != null)
            mdsFile = (MDSFile) mdsFileCache.get(fileName);
        if (mdsFile == null) {
            mdsFile = new MDSFile(props);
            mdsFile.init(fileName);
            mdsFileCache.set(fileName, mdsFile);
        }
        return mdsFile;
    }

    public byte[] getTileData(SlicePositionParameter SlicePositionParameter) {
        byte[] data = null;
        HashCode code = Hashing.md5().hashString(SlicePositionParameter.toString(), Charset.defaultCharset());
        if (sliceCache != null) {
            data = (byte[]) sliceCache.get(code);
            if (data == null) {
                MDSFile mdsFile = generateMDSFile(SlicePositionParameter.getPath());
                if (mdsFile != null) {
                    data = mdsFile.getTileData(SlicePositionParameter.getLevel(), SlicePositionParameter.getX(), SlicePositionParameter.getY());
                    if (data != null) {
                        sliceCache.set(code, data);
                    }
                }
            }
        }
        return data;
    }

    public MDSInfo getMDSInfo(String path) {
        MDSInfo mdsInfo=null;
        MDSFile mdsFile = generateMDSFile(path);
        if (mdsFile != null) {
             mdsInfo = new MDSInfo();
             mdsInfo.setCellHeight(mdsFile.getCellHeight());
             mdsInfo.setCellWidth(mdsFile.getCellWidth());
             mdsInfo.setHeight(mdsFile.getHeight());
             mdsInfo.setWidth(mdsFile.getWidth());
             mdsInfo.setLayerCount(mdsFile.getLayerCount());
             mdsInfo.setMaxLevel(mdsFile.getMaxLevel());
             mdsInfo.setMinLevel(mdsFile.getMinLevel());
        }
        return mdsInfo;
    }

    public MDSFileFactory() {
        initConfig();
        mdsFileCache = new GuanvaCacheGetter(props);
        sliceCache = new GuanvaCacheGetter(props);
    }
}
