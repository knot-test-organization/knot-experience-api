package com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApplicationSpec {
    private ApplicationDestination destination;
    private String project;
    private List<Source> sources;
    private SyncPolicy syncPolicy;
}
