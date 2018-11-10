package com.hongtu.slice.db.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.hongtu.slice.component.GuanvaCacheGetter;
import com.hongtu.slice.config.Configuration;
import com.hongtu.slice.db.entity.TbCatalog;
import com.hongtu.slice.db.mapper.CatalogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.List;

@Service
public class DatabaseIO {
    private Logger LOGGER = LoggerFactory.getLogger(DatabaseIO.class);
    @Autowired
    private CatalogMapper catalogMapper;
    Configuration configuration;
    private GuanvaCacheGetter guanvaCacheGetter;
    private static final String getSlicePathByID_hash = "getSlicePathByID:%d";

    public String getSlicePathByID(int id) {
        String path = null;
        HashCode code = Hashing.md5().hashString(String.format(getSlicePathByID_hash, id), Charset.defaultCharset());
        if (guanvaCacheGetter != null) {
            path = (String) guanvaCacheGetter.get(code);
            if (path == null) {
                List<TbCatalog> catalogList = catalogMapper.selectCatalogByID(id);
                if (catalogList.size() == 1) {
                    path = String.format("/root/slice/subject/%s/%d.mds", catalogList.get(0).getIdcode(), id);
                    guanvaCacheGetter.set(code, path);
                } else {
                    LOGGER.error("getSlicePathByID failed !");
                }
            }
        }
        return path;
    }

    @Autowired
    public DatabaseIO(Configuration configuration) {
        this.configuration=configuration;
        guanvaCacheGetter = new GuanvaCacheGetter(configuration.getDatabaseProperties());
    }
}
