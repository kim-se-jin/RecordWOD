package com.sejin.recordwod

import android.app.Application
import com.kakao.sdk.common.KakaoSdk


class GlobalApplication : Application() {
    companion object {
        var instance: GlobalApplication? = null
    }
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        instance = this
        KakaoSdk.init(this, "87a64fddbb099cc0e34d9fb1f3a12e74")
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getGlobalApplicationContext(): GlobalApplication? {
        checkNotNull(instance) { "This Application does not inherit com.kakao.GlobalApplication" }
        return instance
    }

}

