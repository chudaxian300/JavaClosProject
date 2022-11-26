package com.zgnba.clos.exception;

public class ClosException extends RuntimeException{

    private ClosExceptionCode code;

    public ClosException(ClosExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public ClosExceptionCode getCode() {
        return code;
    }

    public void setCode(ClosExceptionCode code) {
        this.code = code;
    }

    /**
     * 不写入堆栈信息，提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
