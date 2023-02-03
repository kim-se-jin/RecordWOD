package com.sejin.recordwod.fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.sejin.recordwod.R
import com.sejin.recordwod.base.BaseFragment
import com.sejin.recordwod.data.database.UserInfo
import com.sejin.recordwod.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    companion object {
        fun newInstance() = LoginFragment()
    }
    // 데이터베이스
    val database = Firebase.database("https://recordwod-default-rtdb.firebaseio.com/")
    val myRef = database.getReference("users")

    // 카카오 로그인
    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            GoMain()
        }
    }
    override fun initView() {
        val context : Context = requireContext()
        // 버튼 클릭했을 때 로그인
        with(binding) {
            KakaoLoginBtn.setOnClickListener {

                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                        if (error != null) {
                            Log.e(TAG, "카카오톡으로 로그인 실패", error)

                            // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                            // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            }

                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                        } else if (token != null) {
                            Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                            GoMain()
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                }


            }
        }
        //<--

        // FireBase Realtime Database 쓰기
        WriteDataBase = Firebase.database.reference
        CreateUser("1","안영서",true,true,true,"코너 크로스핏")

        // 읽기
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userinfo = snapshot.child("user1")
//                val value = dataSnapshot.getValue<String>()
//                Log.d(TAG, "Value is: $value")
                for(info in userinfo.children){
                    Log.d("snap",info.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 읽어오기 실패했을 때
            }
        })
    }

    private lateinit var WriteDataBase : DatabaseReference
    fun CreateUser(userId: String, name:String, BBweight:Boolean, DBweight:Boolean, KBweight:Boolean, userBox:String ){
        val newUser = UserInfo(userId, name, BBweight, DBweight, KBweight, userBox)
        WriteDataBase.child("users").child(userId).setValue(newUser)
    }

    private fun navigateToNotesScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    fun GoMain(){
        // 로그인 -> 메인
        Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mainFragment)
    }
}

