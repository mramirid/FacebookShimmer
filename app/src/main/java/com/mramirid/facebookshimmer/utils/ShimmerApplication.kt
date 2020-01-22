package com.mramirid.facebookshimmer.utils

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class ShimmerApplication : Application() {

    companion object {
        const val TAG = "MyApplication"

        private lateinit var INSTANCE: ShimmerApplication

        @Synchronized
        fun getInstance(): ShimmerApplication {
            return INSTANCE
        }
    }

    private lateinit var requestQueue: RequestQueue

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    fun getRequestQueue(): RequestQueue {
        if (!::requestQueue.isInitialized) {
            requestQueue = Volley.newRequestQueue(applicationContext)
        }
        return requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        req.tag = if (tag.isEmpty()) TAG else tag
        getRequestQueue().add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        getRequestQueue().add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (!::requestQueue.isInitialized) {
            requestQueue.cancelAll(tag)
        }
    }
}