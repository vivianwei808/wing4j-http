package org.wing4j.http.client;

import org.junit.Test;
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/6/25.
 */
public class ClientTest {

    @Test
    public void testCall() throws Exception {
        Client client = new Client("localhost", 8001, "");
        Request request = Request.builder().service("demoCH").version("1.0.0").cipherType("AES").signType("MD5").data("{}").build();
        Response response = client.call(request);
    }
}