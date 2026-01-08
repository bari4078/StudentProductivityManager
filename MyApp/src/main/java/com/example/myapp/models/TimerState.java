package com.example.myapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimerState {
    @JsonProperty public int totalSeconds;
    @JsonProperty public int initialSeconds;
    @JsonProperty public String mode;
    @JsonProperty public boolean running;
}
