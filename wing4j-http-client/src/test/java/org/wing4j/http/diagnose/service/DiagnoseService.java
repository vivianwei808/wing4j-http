package org.wing4j.http.diagnose.service;

import org.wing4j.doc.api.annotation.ApidocInterface;
import org.wing4j.doc.api.annotation.ApidocService;
import org.wing4j.http.diagnose.domains.DiagnoseRequest;
import org.wing4j.http.diagnose.domains.DiagnoseResponse;
import org.wing4j.http.protocol.annotation.RequestBody;
import org.wing4j.http.protocol.domains.Request;
import org.wing4j.http.protocol.domains.Response;

/**
 * Created by wing4j on 2017/6/25.
 */
@ApidocService("诊断服务")
public interface DiagnoseService {
    @ApidocInterface(value = "诊断1", version = "1.0.1")
    DiagnoseResponse diagnose1(DiagnoseRequest request);
    @ApidocInterface(value = "诊断2", version = "1.0.2")
    Response diagnose2(DiagnoseRequest request);
    @ApidocInterface(value = "诊断3", version = "1.0.3")
    DiagnoseResponse diagnose3(Request request);
    @ApidocInterface(value = "诊断4", version = "1.0.4")
    Response diagnose4(Request request);
    @ApidocInterface(value = "诊断5", version = "1.0.5")
    DiagnoseResponse diagnose5(@RequestBody DiagnoseRequest request, String test);
    @ApidocInterface(value = "诊断6", version = "1.0.6")
    DiagnoseResponse diagnose6(String test, @RequestBody DiagnoseRequest request);
}
