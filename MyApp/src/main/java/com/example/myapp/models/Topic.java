package com.example.myapp.models;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String name;
    private List<Flashcard> flashcards;

    public Topic() {
        flashcards = new ArrayList<>();
    }

    public Topic(String name) {
        this.name = name;
        this.flashcards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }
    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
    public Flashcard getFlashcard(int index) {
        return flashcards.get(index);
    }

    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
    }

    public void removeFlashcard(Flashcard flashcard) {
        flashcards.remove(flashcard);
    }
}
