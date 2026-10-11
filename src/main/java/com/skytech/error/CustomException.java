package com.skytech.error;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomException extends RuntimeException {

    private final String param;
    private final String msg;

    public CustomException(String param, String msg) {
        super(msg);
        this.param = param;
        this.msg = msg;
    }

}
