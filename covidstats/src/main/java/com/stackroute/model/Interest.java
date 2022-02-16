package com.stackroute.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Interest {
    @Id
    private String interestId;
    private String userId;
    private List<String> countries;

    public Interest(){

    }

    public Interest(String interestId, String userId, List<String> countries) {
        this.interestId = interestId;
        this.userId = userId;
        this.countries = countries;
    }

    public String getInterestId() {
        return interestId;
    }

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }
}
