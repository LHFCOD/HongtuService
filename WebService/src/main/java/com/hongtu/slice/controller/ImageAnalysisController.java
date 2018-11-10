package com.hongtu.slice.controller;

import com.google.common.base.Throwables;
import com.hongtu.slice.component.MDSFileFactory;
import com.hongtu.slice.db.entity.TbCatalog;
import com.hongtu.slice.db.mapper.CatalogMapper;
import com.hongtu.slice.util.Add;
import com.hongtu.slice.util.MDSInfo;
import com.hongtu.slice.util.SliceParameter;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.CodePointLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class ImageAnalysisController {
    @Autowired
    MDSFileFactory mdsFileFactory;

    private Logger LOGGER = LoggerFactory.getLogger(ImageAnalysisController.class);

    @ResponseBody
    @RequestMapping(value = "/add")
    Double add(@RequestParam Double a, @RequestParam Double b) {
        LOGGER.info("request add service: a:{},b:{}", a, b);
        return a + b;
    }

    @GetMapping(value = "/getTileData", produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<byte[]> getTileData(@RequestParam("id") Integer id,
                                       @RequestParam("level") Integer level,
                                       @RequestParam("x") Integer x,
                                       @RequestParam("y") Integer y) {
        SliceParameter sliceParameter = new SliceParameter();
        sliceParameter.setPath(getSlicePathByID(id));
        sliceParameter.setLevel(level);
        sliceParameter.setX(x);
        sliceParameter.setY(y);
        LOGGER.info("request getTileData service: request:{}", sliceParameter.toString());
        byte[] bytes = mdsFileFactory.getTileData(sliceParameter);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }

    @GetMapping(value = "/getMDSInfo")
    MDSInfo getMDSInfo(@RequestParam("id") Integer id) {
        SliceParameter sliceParameter = new SliceParameter();
        sliceParameter.setPath(getSlicePathByID(id));
        LOGGER.info("request getMDSInfo service: request:{}", sliceParameter.toString());
        return mdsFileFactory.getMDSInfo(sliceParameter);
    }

    @GetMapping(value = "/getThumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<byte[]> getThumbnail(@RequestParam("id") Integer id,
                                        @RequestParam("thumbnailWidth") Integer thumbnailWidth) {
        SliceParameter sliceParameter = new SliceParameter();
        sliceParameter.setPath(getSlicePathByID(id));
        sliceParameter.setThumbnailWidth(thumbnailWidth);
        LOGGER.info("request getThumbnail service: request:{}", sliceParameter.toString());
        byte[] bytes = mdsFileFactory.getThumbnail(sliceParameter);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }

    @Autowired
    private CatalogMapper catalogMapper;

    String getSlicePathByID(int id) {
        List<TbCatalog> catalogList = catalogMapper.selectCatalogByID(id);
        String path = null;
        if (catalogList.size() == 1) {
            path = String.format("/root/slice/subject/%s/%d.mds",catalogList.get(0).getIdcode(),id);
        } else {
            LOGGER.error("getSlicePathByID failed !");
        }
        return path;
    }

}
