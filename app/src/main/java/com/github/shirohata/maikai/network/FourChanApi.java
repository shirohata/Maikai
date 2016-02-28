package com.github.shirohata.maikai.network;

import com.github.shirohata.maikai.model.Catalog;
import com.github.shirohata.maikai.model.Thread;
import com.github.shirohata.maikai.model.ThreadList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hirohata on 2016/02/20.
 */
public interface FourChanApi {
    @GET("/{board}/thread/{threadnumber}.json")
    Call<Thread> getThread(@Path("board") String board, @Path("threadnumber") long threadNumber);

    @GET("/{board}/thread/{threadnumber}.json")
    Observable<Thread> getThreadRx(@Path("board") String board, @Path("threadnumber") long threadNumber);

    @GET("/{board}/{pagenumber}.json")
    Call<ThreadList> getThreadList(@Path("board") String board, @Path("pagenumber") int pageNumber);

    @GET("/{board}/catalog.json")
    Call<ThreadList> getCatalog(@Path("board") String board);

    @GET("/{board}/catalog.json")
    Observable<Catalog[]> getCatalogRx(@Path("board") String board);

    @GET("/{board}/threads.json")
    Call<ThreadList> getThreadIDs(@Path("board") String board);

    @GET("/{board}/archive.json")
    Call<ThreadList> getArchive(@Path("board") String board);
}
