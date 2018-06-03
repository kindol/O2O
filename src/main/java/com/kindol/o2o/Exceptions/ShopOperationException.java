package com.kindol.o2o.Exceptions;

public class ShopOperationException extends RuntimeException {

    private static final long serialVersionUID = 241428850229283967L;

    public ShopOperationException(String message) {
        super(message);
    }
}
