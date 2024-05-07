package com.me.remenber.dto;

import java.io.Serializable;

public class KeyResposeDTO implements Serializable {

    private String name;

    public KeyResposeDTO() {
    }

    public KeyResposeDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
