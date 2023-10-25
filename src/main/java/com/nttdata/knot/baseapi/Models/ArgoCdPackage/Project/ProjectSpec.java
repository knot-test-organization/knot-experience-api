package com.nttdata.knot.baseapi.Models.ArgoCdPackage.Project;

import java.util.List;

// project spec class
public class ProjectSpec {
    private String description;
    private List<ProjectDestination> destinations;
    private List<String> sourceRepos;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectDestination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<ProjectDestination> destinations) {
        this.destinations = destinations;
    }

    public List<String> getSourceRepos() {
        return sourceRepos;
    }

    public void setSourceRepos(List<String> sourceRepos) {
        this.sourceRepos = sourceRepos;
    }
} 