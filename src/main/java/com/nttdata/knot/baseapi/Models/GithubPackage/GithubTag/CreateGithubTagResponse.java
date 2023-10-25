package com.nttdata.knot.baseapi.Models.GithubPackage.GithubTag;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CreateGithubTagResponse {
    @JsonProperty("sha")
    private String sha;
    @JsonProperty("url")
    private String url;
    @JsonProperty("tagger")
    private Person tagger;
    @JsonProperty("object")
    private ObjectTag object;
    @JsonProperty("tag")
    private String tag;
    @JsonProperty("message")
    private String message;

}
