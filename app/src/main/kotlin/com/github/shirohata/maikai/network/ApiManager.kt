package com.github.shirohata.maikai.network

import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by hirohata on 2016/02/20.
 */


fun create4ChanApi(context: Context): FourChanApi {
    Stetho.initializeWithDefaults(context)
    val ok = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

    // PicassoとOkHttpClientを共有する
    Picasso.setSingletonInstance(Picasso.Builder(context).downloader(OkHttp3Downloader(ok)).build())

    val retrofit = Retrofit.Builder()
            .baseUrl("http://a.4cdn.org")
            .client(ok)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    return retrofit.create(FourChanApi::class.java)
}

fun getThumbUri(boardName: String, tim: Long): String {
    return "http://t.4cdn.org/$boardName/${tim}s.jpg"
}

fun getImageUri(boardName: String, tim: Long, ext: String): String {
    return "http://i.4cdn.org/$boardName/$tim$ext"
}
