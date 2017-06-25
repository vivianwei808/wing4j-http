package org.wing4j.http.diagnose.domains;

import lombok.Data;
import lombok.ToString;
import org.wing4j.doc.api.annotation.ApidocElement;

/**
 * Created by wing4j on 2017/6/25.
 */
@Data
@ToString
public class DiagnoseRequest {
    @ApidocElement("诊断名称")
    String name;
}
