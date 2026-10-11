package com.skytech.error;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorItem {
    private String param;
    private String msg;

    public ErrorItem() {}

    public ErrorItem(String param, String msg) {
        this.param = param;
        this.msg = msg;
    }

}