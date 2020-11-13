package com.example.cooplas.models.home.searchUser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchUserModel {

    @SerializedName("next_offset")
    @Expose
    private Integer nextOffset;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public Integer getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(Integer nextOffset) {
        this.nextOffset = nextOffset;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
