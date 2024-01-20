package com.example.partner.Model;

public class Quiz {
    private String quenstion;
    private String answer;

    public Quiz(String quenstion, String answer) {
        this.quenstion = quenstion;
        this.answer = answer;
    }

    public String getQuenstion() {
        return quenstion;
    }

    public void setQuenstion(String quenstion) {
        this.quenstion = quenstion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
