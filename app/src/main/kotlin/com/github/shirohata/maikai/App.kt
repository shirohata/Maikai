package com.github.shirohata.maikai

import android.app.Application
import com.github.shirohata.maikai.network.FourChanApi
import com.github.shirohata.maikai.network.create4ChanApi

/**
 * Created by hirohata on 2016/02/20.
 */

class App : Application() {
    companion object {
        lateinit var api: FourChanApi
            private set
    }

    override fun onCreate() {
        super.onCreate()
        api = create4ChanApi(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}
