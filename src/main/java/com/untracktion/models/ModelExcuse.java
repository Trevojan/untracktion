package com.untracktion.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ModelExcuse {

    private String category;
    private String quoteid;
    private String quote;
    private int delay;

    public ModelExcuse(String category, String quoteid, String quote, int delay) {
        this.category = category;
        this.quoteid = quoteid;
        this.quote = quote;
        this.delay = delay;
    }

    public ModelExcuse() {
    }
}
