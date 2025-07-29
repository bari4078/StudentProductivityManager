package com.example.myapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimerState {
    @JsonProperty public int totalSeconds;
    @JsonProperty public int initialSeconds;
    @JsonProperty public String mode;  // "pomodoro", "short", or "long"
    @JsonProperty public boolean running;
}
