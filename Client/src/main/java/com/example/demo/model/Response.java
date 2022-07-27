package com.example.demo.model;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 1088738910095060495L;
    public String l;
    public Response(String l){
        this.l = l;
    }
}
