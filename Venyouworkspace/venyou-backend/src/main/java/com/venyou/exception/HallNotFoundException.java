package com.venyou.exception;

public class HallNotFoundException extends RuntimeException {
    public HallNotFoundException(String msg)
    {
        super(msg);
    }
}
