package com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application;

import java.util.List;

// Helm class
public class Helm {
    private List<String> valueFiles;

    public List<String> getValueFiles() {
        return valueFiles;
    }

    public void setValueFiles(List<String> valueFiles) {
        this.valueFiles = valueFiles;
    }
}
