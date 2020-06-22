package com.gruppo42.app.api.models;

import java.util.List;

public class TrailerQueryResultDTO {
    private int id;
    private List<TrailerResultDTO> results;

    public TrailerQueryResultDTO() {}

    public TrailerQueryResultDTO(int id, List<TrailerResultDTO> results) {
        this.id = id;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TrailerResultDTO> getResults() {
        return results;
    }

    public void setResults(List<TrailerResultDTO> results) {
        this.results = results;
    }

    public int getTotal_results() {
        return results.size();
    }
}
