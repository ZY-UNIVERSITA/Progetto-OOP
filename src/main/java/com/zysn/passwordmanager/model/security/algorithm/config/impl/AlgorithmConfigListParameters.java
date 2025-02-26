package com.zysn.passwordmanager.model.security.algorithm.config.impl;

import com.fasterxml.jackson.annotation.JsonInclude;

public class AlgorithmConfigListParameters {
    private String name;

    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer min_value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer max_value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String default_value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] options; // Per scelte multiple

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMin_value() {
        return min_value;
    }

    public void setMin_value(Integer min_value) {
        this.min_value = min_value;
    }

    public Integer getMax_value() {
        return max_value;
    }

    public void setMax_value(Integer max_value) {
        this.max_value = max_value;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
