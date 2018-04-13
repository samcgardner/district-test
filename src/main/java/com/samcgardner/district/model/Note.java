package com.samcgardner.district.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Note {

    @JsonProperty
    private String secret;
    @JsonProperty
    private String message;
    @JsonProperty
    private String version;
    @JsonProperty("_links")
    private Map<String, Link> link;
    private int id;

    public int getId() {
        return id;
    }

    @JsonProperty
    public void setId(int id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Link> getLink() {
        return link;
    }

    public void setLink(Map<String, Link> link) {
        this.link = link;
    }

}
