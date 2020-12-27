package com.devandy.MiddlewareApiTutorial.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TestVO {
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "age")
    private String age;
    @JsonProperty(value = "nation")
    private String nation;
    @JsonProperty(value = "position")
    private String position;
    @JsonProperty(value = "goal")
    private String goal;
}
