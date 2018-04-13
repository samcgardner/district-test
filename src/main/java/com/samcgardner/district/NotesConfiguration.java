package com.samcgardner.district;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class NotesConfiguration extends Configuration {

    @JsonProperty
    private String storepath;

    public String getStorepath() {
        return storepath;
    }

    public void setStorepath(String storepath) {
        this.storepath = storepath;
    }
}
