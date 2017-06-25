package org.wing4j.http.client;

import org.junit.Test;
import org.wing4j.http.diagnose.service.DiagnoseService;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/6/25.
 */
public class EndpointTest {

    @Test
    public void testLookup() throws Exception {
        ServiceContext ctx = ServiceContext.builder()
                .host("localhost")
                .port(8001)
                .appContext("")
                .channelNo("DEMECHE")
                .signPassword("password1")
                .cipherPassword("password2")
                .build();
        DiagnoseService diagnoseService = Endpoint.lookup(DiagnoseService.class, ctx);
        diagnoseService.diagnose1(null);
    }
}