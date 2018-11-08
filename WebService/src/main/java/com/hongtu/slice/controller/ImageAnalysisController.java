package com.hongtu.slice.controller;

import com.google.common.base.Throwables;
import com.hongtu.slice.component.MDSFileFactory;
import com.hongtu.slice.util.Add;
import com.hongtu.slice.util.MDSInfo;
import com.hongtu.slice.util.SliceParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    @RequestMapping(value = "/getTileData",method = RequestMethod.POST,produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<byte[]> getTileData(@RequestBody SliceParameter sliceParameter) {
        LOGGER.info("request getTileData service: request:{}", sliceParameter.toString());
        byte[] bytes= mdsFileFactory.getTileData(sliceParameter);
        return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }

    @ResponseBody
    @RequestMapping(value = "/getMDSInfo",method = RequestMethod.POST)
    MDSInfo getMDSInfo(@RequestBody String path) {
        LOGGER.info("request getMDSInfo service: request:{}", path);
        SliceParameter sliceParameter = new SliceParameter();
        sliceParameter.setPath(path.trim());
        return mdsFileFactory.getMDSInfo(sliceParameter);
    }
    @ResponseBody
    @RequestMapping(value = "/getThumbnail",method = RequestMethod.POST,produces = MediaType.IMAGE_JPEG_VALUE)
    ResponseEntity<byte[]> getThumbnail(@RequestBody SliceParameter sliceParameter) {
        LOGGER.info("request getThumbnail service: request:{}", sliceParameter.toString());
        byte[] bytes= mdsFileFactory.getThumbnail(sliceParameter);
        return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
    }

}
