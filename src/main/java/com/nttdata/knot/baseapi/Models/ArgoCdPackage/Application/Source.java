package com.nttdata.knot.baseapi.Models.ArgoCdPackage.Application;


// source class
public class Source {
    private Helm helm;
    private String path;
    private String repoURL;
    private String targetRevision;
    private String ref;

    public Helm getHelm() {
        return helm;
    }

    public void setHelm(Helm helm) {
        this.helm = helm;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRepoURL() {
        return repoURL;
    }

    public void setRepoURL(String repoURL) {
        this.repoURL = repoURL;
    }

    public String getTargetRevision() {
        return targetRevision;
    }

    public void setTargetRevision(String targetRevision) {
        this.targetRevision = targetRevision;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
