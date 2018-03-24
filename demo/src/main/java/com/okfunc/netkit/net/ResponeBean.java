package com.okfunc.netkit.net;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by clyde on 2017/12/4.
 */

public class ResponeBean {
    @JSONField(name = "status_code")
    public int status_code;
    @JSONField(name = "message")
    public String message;
    @JSONField(name = "data")
    public String data;
    @JSONField(name = "errors")
    public String errors;
}