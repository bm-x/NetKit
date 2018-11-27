package com.okfunc.netkit.bean;

public class ResponeWrap<T> {


    /**
     * code : 200
     * msg : success
     */

    public int code;
    public String msg;

    public Page content;

    public T page;
}
