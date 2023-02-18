package com.sejin.recordwod.view.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.sejin.recordwod.repository.UserRepository
import com.sejin.recordwod.repository.remote.RemoteWodRepository
import com.sejin.recordwod.view.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//class LoginViewModel(application: Application) : AndroidViewModel(application) {
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository : UserRepository
) : BaseViewModel<LoginState>(initialState = LoginState()) {

    // 데이터를 캡슐화하여 외부(뷰)에서 접근할 수 없고
    // 외부 접근 프로퍼티는 immutable 타입으로 제한해 변경할 수 없도록 한당

    private val _data = MutableLiveData<String>("")
    val data : LiveData<String> = _data

//    private val context = getApplication<Application>().applicationContext
//    private val repository = RemoteWodRepository()

    fun kakaoLogin() {
//        setState { state -> state.copy(title = title) }
        val response = userRepository.UserKakaoLogin()
    }

}