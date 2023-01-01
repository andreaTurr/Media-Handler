package it.unimib.exercise.andrea.mediahandler.models.video;

public class PageInfo {
    long totalResults;
    long resultsPerPage;

    public PageInfo(long totalResults, long resultsPerPage) {
        this.totalResults = totalResults;
        this.resultsPerPage = resultsPerPage;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(long resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
}
