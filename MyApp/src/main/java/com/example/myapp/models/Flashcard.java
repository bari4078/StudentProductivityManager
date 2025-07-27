package com.example.myapp.models;

public class Flashcard {
    private String frontText = "Nothing to show";
    private String backText = "Nothing to show" ;

    public Flashcard() {}

    public Flashcard(String frontText,String backText) {
        this.backText = backText;
        this.frontText = frontText;
    }

    public String getFrontText() {
        return frontText;
    }
    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public String getBackText() {
        return backText;
    }
    public void setBackText(String backText) {
        this.backText = backText;
    }


}