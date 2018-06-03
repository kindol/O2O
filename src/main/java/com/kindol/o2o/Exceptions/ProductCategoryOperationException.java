package com.kindol.o2o.Exceptions;

public class ProductCategoryOperationException extends RuntimeException {

    private static final long serialVersionUID = 3100220866011203409L;

    public ProductCategoryOperationException(String message) {
        super(message);
    }
}
