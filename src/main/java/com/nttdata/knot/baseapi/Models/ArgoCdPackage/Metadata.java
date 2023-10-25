package com.nttdata.knot.baseapi.Models.ArgoCdPackage;

import com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application.Label;

// Metadata class
public class Metadata {
    private String name;
    private Label labels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Label getLabels() {
        return labels;
    }

    public void setLabels(Label labels) {
        this.labels = labels;
    }
}