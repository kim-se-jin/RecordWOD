package com.sejin.recordwod

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
/* HiltAndroidApp 컨테이너 역할 (기본 클래스를 비롯하여 Hilt의 코드 생성을 트리거 )
Hilt 구성요소는 Application 객체의 수명 주기에 연결되며 이와 관련한 종속 항목을 제공
앱의 상위 구성요소이므로 다른 구성요소는 이 상위 구성요소에서 제공하는 종속 항목에 액세스 가능
 */
class myApplication : Application() {
    companion object {
        var instance: myApplication? = null
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

    fun getGlobalApplicationContext(): myApplication? {
        checkNotNull(instance) { "This Application does not inherit com.kakao.GlobalApplication" }
        return instance
    }

}

