package com.sejin.recordwod.repository.remote

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.sejin.recordwod.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteUserRepository @Inject internal constructor(
    private val context: Context
    ) : UserRepository {
    /* . Singleton : 앱이 실행되는 동안 하나의 인스턴스만 생성된다는 의미
    -> 액티비티나 프래그먼트의 생명주기 상태가 변경되어도 계속 유지될 수 있다. (앱이 삭제되면 삭제)
    따라서, 앱이 시작될 때, Repository가 생성되게 해야한다.
     프로그램 시작과 종료까지 클래스의 인스턴스를 단 한 번만 생성하여 사용하는 패턴을 의미
     프로그램 전역에서, 이 인스턴스를 공유하며 사용할 수 있게끔 하는 것이다.
     */

    override suspend fun addUser(
        username: String,
        password : String
    ){

    }

    // 카카오 로그인
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    override suspend fun UserKakaoLogin(){

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
//                    GoMain()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }


}