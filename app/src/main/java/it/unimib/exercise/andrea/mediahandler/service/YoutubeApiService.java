package it.unimib.exercise.andrea.mediahandler.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interface for Service to get news from the Web Service.
 */
public interface YoutubeApiService {
    /* @GET(TOP_HEADLINES_ENDPOINT)
    Call<NewsApiResponse> getNews(
            @Query(TOP_HEADLINES_COUNTRY_PARAMETER) String country,
            @Query(TOP_HEADLINES_PAGE_SIZE_PARAMETER) int pageSize,
            @Header("Authorization") String apiKey);*/
}
