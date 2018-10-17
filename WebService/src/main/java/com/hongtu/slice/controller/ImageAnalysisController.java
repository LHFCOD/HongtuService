package com.hongtu.slice.controller;

import com.hongtu.slice.util.Add;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageAnalysisController {
    private static Logger LOGGER = LoggerFactory.getLogger(ImageAnalysisController.class);

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    String add(@RequestBody Add param) {
        Double a = Double.valueOf(param.getA());
        Double b = Double.valueOf(param.getB());
        LOGGER.info("requst add service: a:{},b:{}",a,b);
        return String.format("%f", a + b);
    }
}
