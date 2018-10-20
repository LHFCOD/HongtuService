package com.hongtu.slice.controller;

import com.google.common.base.Throwables;
import com.hongtu.slice.util.Add;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ImageAnalysisController {
    private Logger LOGGER = LoggerFactory.getLogger(ImageAnalysisController.class);

    @ResponseBody
    @RequestMapping(value = "/add")
    Double add(@RequestParam Double a, @RequestParam Double b) {
        LOGGER.info("requst add service: a:{},b:{}", a, b);
        return a + b;
    }
}
