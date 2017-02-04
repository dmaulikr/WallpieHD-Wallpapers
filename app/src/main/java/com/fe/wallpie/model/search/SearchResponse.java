
package com.fe.wallpie.model.search;

import java.util.List;
import com.google.gson.annotations.SerializedName;
public class SearchResponse {

    @SerializedName("results")
    private List<Result> mResults;
    @SerializedName("total")
    private Long mTotal;
    @SerializedName("total_pages")
    private Long mTotalPages;

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

    public Long getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Long total_pages) {
        mTotalPages = total_pages;
    }

}
