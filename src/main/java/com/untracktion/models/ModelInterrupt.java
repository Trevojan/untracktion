package com.untracktion.models;

import lombok.Getter;
import lombok.Setter;

public class ModelInterrupt
{
    @Getter @Setter
    private String method;
    @Getter @Setter
    private int delay;
    @Getter @Setter
    private String loading;

    public ModelInterrupt(String mainMtd, int wait, String loadMtd)
    {
        this.method = mainMtd;
        this.delay = wait;
        this.loading = loadMtd;
    }
}
