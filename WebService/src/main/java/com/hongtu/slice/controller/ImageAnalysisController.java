package com.hongtu.slice.controller;

import com.google.common.base.Throwables;
import com.hongtu.slice.component.MDSFileFactory;
import com.hongtu.slice.util.Add;
import com.hongtu.slice.util.SlicePositionParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
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
    @RequestMapping(value = "/getTileData",method = RequestMethod.POST)
    byte[] getTileData(@RequestBody SlicePositionParameter slicePositionParameter) {
        LOGGER.info("request handleSlicePositionParameter service: request:{}", slicePositionParameter.toString());
        return mdsFileFactory.getTileData(slicePositionParameter);
    }
}
