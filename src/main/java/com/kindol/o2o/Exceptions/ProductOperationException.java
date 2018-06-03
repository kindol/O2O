package com.kindol.o2o.Exceptions;

public class ProductOperationException extends RuntimeException {

    private static final long serialVersionUID = -3994377877580205761L;

    public ProductOperationException(String message) {
        super(message);
    }
}
