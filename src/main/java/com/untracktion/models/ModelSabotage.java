package com.untracktion.models;

import lombok.Getter;
import lombok.Setter;

public class ModelSabotage
{
    @Getter @Setter
    private String message;
    @Getter @Setter
    private String method;
    @Getter @Setter
    private boolean resetField;
    @Getter @Setter
    private boolean active;
    @Getter @Setter
    private double chance;

    public ModelSabotage(String msg, String mtd, boolean res, boolean act, double cent)
    {
        this.message = msg;
        this.method = mtd;
        this.resetField = res;
        this.active = act;
        this.chance = cent;
    }
}
