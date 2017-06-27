package org.wing4j.http.doc;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/6/28.
 */
public class DocScannerTest {
    DocScanner docScanner = new DocScanner();
    @Test
    public void testInit() throws Exception {
        docScanner.init(false, "org.wing4j.http");
    }

    @Test
    public void testListService() throws Exception {

    }

    @Test
    public void testListInterface() throws Exception {

    }

    @Test
    public void testListInterface1() throws Exception {

    }
}