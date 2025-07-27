package com.example.myapp.models;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private String name;
    private List<Topic> topics;

    public Subject() {
        topics = new ArrayList<>();
    }

    public Subject(String name) {
        this.name = name;
        this.topics = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Topic> getTopics() {
        return topics;
    }
    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public void addTopic(Topic topic) {
        this.topics.add(topic);
    }
    public void removeTopic(Topic topic) {
        this.topics.remove(topic);
    }

    public boolean hasTopic(Topic topic) {
        return topics.contains(topic);
    }

}
