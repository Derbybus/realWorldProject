package com.accede.realWorld.exceptions;

public class IdNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public IdNotFoundException() {
        super();
    }


    public IdNotFoundException(String message) {
        super(message);
    }
}
