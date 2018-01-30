package com.sam.graduation.design.gdemailserver.controller.pub;

import com.sam.graduation.design.gdemailserver.constvalue.ServiceResultType;

public class AppException extends RuntimeException {

    private static final long serialVersionUID = 9181283732589990949L;
    private int errCode = 0;

    public AppException(String errMsg, int errCode) {
        super(errMsg);
        this.errCode = errCode;
    }

    public AppException(Throwable cause, int ErrCode) {
        super(cause);
        this.errCode = ErrCode;
    }

    public AppException(String cause) {
        super(cause);
        this.errCode = ServiceResultType.RESULT_TYPE_SYSTEM_ERROR;
    }


    public int getErrCode() {
        return errCode;
    }


}
