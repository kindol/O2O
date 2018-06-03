package com.kindol.o2o.Exceptions;

public class WechatAuthOperationExecption extends RuntimeException {

    private static final long serialVersionUID = -5579699009951553928L;

    public WechatAuthOperationExecption(String message) {
        super(message);
    }
}
