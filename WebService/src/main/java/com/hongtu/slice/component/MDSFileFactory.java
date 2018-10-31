package com.hongtu.slice.component;

import com.google.common.base.Throwables;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
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
    private GuanvaCacheGetter guanvaCacheGetter;

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
        if (guanvaCacheGetter != null)
            mdsFile = (MDSFile) guanvaCacheGetter.get(fileName);
        if (mdsFile == null) {
            mdsFile = new MDSFile(props);
            mdsFile.init(fileName);
            guanvaCacheGetter.set(fileName, mdsFile);
        }
        return mdsFile;
    }

    public byte[] getTileData(SlicePositionParameter SlicePositionParameter) {
        byte[] data = null;
        HashCode code = Hashing.md5().hashString(SlicePositionParameter.toString(), Charset.defaultCharset());
        if (guanvaCacheGetter != null) {
            data = (byte[]) guanvaCacheGetter.get(code);
            if (data == null) {
                MDSFile mdsFile = generateMDSFile(SlicePositionParameter.getPath());
                if (mdsFile != null) {
                    data = mdsFile.getTileData(SlicePositionParameter.getLevel(), SlicePositionParameter.getX(), SlicePositionParameter.getY());
                    if (data != null) {
                        guanvaCacheGetter.set(code, data);
                    }
                }
            }
        }
        return data;
    }

    public MDSFileFactory() {
        initConfig();
        guanvaCacheGetter = new GuanvaCacheGetter(props);
    }
}
