package com.me.remenber.pojo;

import java.io.Serializable;

public class JSONResponse implements Serializable {

    private String name;

    public JSONResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
