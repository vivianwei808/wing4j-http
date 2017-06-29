package org.wing4j.http.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wing4j.http.server.InterfaceAccessEngine;

import java.io.UnsupportedEncodingException;

/**
 * Created by wing4j on 2017/6/27.
 */
@Controller
public class ApiController {
    @Autowired
    InterfaceAccessEngine interfaceAccessEngine;

    @RequestMapping("/api")
    public String api(String body) throws UnsupportedEncodingException {
        return interfaceAccessEngine.execute(body);
    }
}
