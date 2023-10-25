package com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application;

import java.util.List;

// SyncPolicy class
public class SyncPolicy {
    private Automated automated;
    private List<String> syncOptions;

    public Automated getAutomated() {
        return automated;
    }

    public void setAutomated(Automated automated) {
        this.automated = automated;
    }

    public List<String> getSyncOptions() {
        return syncOptions;
    }

    public void setSyncOptions(List<String> syncOptions) {
        this.syncOptions = syncOptions;
    }
}