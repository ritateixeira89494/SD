package com.sd.ssutilizadores.exceptions;

public class UtilizadorExisteException extends Exception {
    public UtilizadorExisteException() {
        super();
    }
    public UtilizadorExisteException(String msg) {
        super(msg);
    }
}