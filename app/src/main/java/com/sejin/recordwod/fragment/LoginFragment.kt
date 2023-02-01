package com.sejin.recordwod.fragment

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
import com.sejin.recordwod.R
import com.sejin.recordwod.base.BaseFragment
import com.sejin.recordwod.data.database.UserInfo
import com.sejin.recordwod.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    companion object {
        fun newInstance() = LoginFragment()
    }
    val database = Firebase.database("https://recordwod-default-rtdb.firebaseio.com/")
    val myRef = database.getReference("users")

    override fun initView() {
        with(binding) {
            KakaoLoginBtn.setOnClickListener {
                Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }

        // 쓰기
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
}

