package com.hongtu.slice.config;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class Configuration {
    private Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
    private final String SETTINGS_PROPERTIES = "settings.properties";
    private Properties props;
    private Properties mdsFileCacheProperties;
    private Properties sliceCacheProperties;
    private Properties mdsFileProperties;
    private Properties databaseProperties;

    public Configuration() {
        props = new Properties();
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream(SETTINGS_PROPERTIES));
        } catch (Exception ex) {
            LOGGER.error("init config failed : {}", Throwables.getStackTraceAsString(ex));
        }
    }

    public Properties getMDSFileCacheProperties() {
        if (mdsFileCacheProperties == null && props != null) {
            mdsFileCacheProperties = new Properties();
            mdsFileCacheProperties.setProperty("guanva.cacheTime", props.getProperty("guanva.mdsFile.cacheTime"));
        }
        return mdsFileCacheProperties;
    }

    public Properties getSliceCacheProperties() {
        if (sliceCacheProperties == null && props != null) {
            sliceCacheProperties = new Properties();
            sliceCacheProperties.setProperty("guanva.cacheTime", props.getProperty("guanva.slice.cacheTime"));
        }
        return sliceCacheProperties;
    }

    public Properties getMDSFileProperties() {
        if (mdsFileProperties == null && props != null) {
            mdsFileProperties = new Properties();
            mdsFileProperties.setProperty("xml.path", props.getProperty("xml.path"));
        }
        return mdsFileProperties;
    }

    public Properties getDatabaseProperties() {
        if (databaseProperties == null && props != null) {
            databaseProperties = new Properties();
            databaseProperties.setProperty("guanva.cacheTime", props.getProperty("guanva.database.cacheTime"));
        }
        return databaseProperties;
    }

}
