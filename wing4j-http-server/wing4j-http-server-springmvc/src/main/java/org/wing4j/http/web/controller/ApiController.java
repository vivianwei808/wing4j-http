package org.wing4j.http.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wing4j.http.server.InterfaceAccessEngine;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by wing4j on 2017/6/27.
 */
@Controller
public class ApiController {
    @Autowired
    InterfaceAccessEngine interfaceAccessEngine;

    @RequestMapping("/api")
    public String api(@RequestBody String body, String encode) throws UnsupportedEncodingException {
        if("true".equals(encode)){
            body = URLEncoder.encode(body, "UTF-8");
        }
        return interfaceAccessEngine.execute(body);
    }
}
