package org.wing4j.http.server.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.server.InterfaceAccessEngine;
import org.wing4j.test.BaseTest;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/6/27.
 */
@ContextConfiguration("classpath*:testContext-simpleInterfaceAccessEngine.xml")
public class SimpleInterfaceAccessEngineTest extends BaseTest{
    @Autowired
    InterfaceAccessEngine interfaceAccessEngine;
    static Gson GSON = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMddHHmmssSSS").create();
    @Test
    public void testExecute() throws Exception {
        Request request = Request.builder().channelNo("testChannel").txType("fetchInterfaceDefine").version("10").data("this is a test").build();
        String json = GSON.toJson(request);
        String message = interfaceAccessEngine.execute(json);
        System.out.println(message);
    }
}