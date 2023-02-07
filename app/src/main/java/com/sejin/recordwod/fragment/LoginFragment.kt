package com.sejin.recordwod.fragment

import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
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
import com.sejin.recordwod.view.viewmodel.LoginViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    companion object {
        fun newInstance() = LoginFragment()
    }
    //뷰모델

    // 데이터베이스
    val database = Firebase.database("https://recordwod-default-rtdb.firebaseio.com/")
    val myRef = database.getReference("users")

    // 카카오 로그인
    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨

    // 구글로그인
    private lateinit var auth: FirebaseAuth
    private lateinit var signInClient: SignInClient

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            handleSignInResult(result.data)
        }


    override fun initView() {
        val context: Context = requireContext()

        // 구글 로그인 -->
        auth = Firebase.auth

        // 활동을 초기화할 때 사용자가 현재 로그인 되어있는 지 확인
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
//            reload()
        }
        // <--

        // 버튼 클릭했을 때 로그인
        with(binding) {
//            KakaoLoginBtn.setOnClickListener {}

        }


        //<--

        // FireBase Realtime Database 쓰기
        WriteDataBase = Firebase.database.reference
        CreateUser("1", "안영서", true, true, true, "코너 크로스핏")

        // 읽기
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userinfo = snapshot.child("user1")
//                val value = dataSnapshot.getValue<String>()
//                Log.d(TAG, "Value is: $value")
                for (info in userinfo.children) {
                    Log.d("snap", info.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 읽어오기 실패했을 때
            }
        })
    }

    private lateinit var WriteDataBase: DatabaseReference
    fun CreateUser(
        userId: String,
        name: String,
        BBweight: Boolean,
        DBweight: Boolean,
        KBweight: Boolean,
        userBox: String
    ) {
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

    // 로그인 -> 메인
    fun GoMain() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_loginFragment_to_mainFragment)
    }

    // 구글 로그인
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener { signIn() }
//        binding.signOutButton.setOnClickListener { signOut() }

        signInClient = Identity.getSignInClient(requireContext())
        // Initialize Firebase Auth
        auth = Firebase.auth

        // Display One-Tap Sign In if user isn't logged in
        val currentUser = auth.currentUser
        if (currentUser == null) {
            oneTapSignIn()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    private fun handleSignInResult(data: Intent?) {
        // Result returned from launching the Sign In PendingIntent
        try {
            // Google Sign In was successful, authenticate with Firebase
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                firebaseAuthWithGoogle(idToken)
            } else {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        showProgressBar()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }

                hideProgressBar()
            }
    }


    private fun signIn() {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(com.firebase.ui.auth.R.string.default_web_client_id))
            .build()

        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Google Sign-in failed", e)
            }
    }

    private fun oneTapSignIn() {
        // Configure One Tap UI
        val oneTapRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

        // Display the One Tap UI
        signInClient.beginSignIn(oneTapRequest)
            .addOnSuccessListener { result ->
                launchSignIn(result.pendingIntent)
            }
            .addOnFailureListener { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
            }
    }

    private fun launchSignIn(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't start Sign In: ${e.localizedMessage}")
        }
    }
//test
    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        signInClient.signOut().addOnCompleteListener(requireActivity()) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressBar()
//        if (user != null) {
//            binding.signInButton.visibility = View.GONE
//        }
        GoMain()
    }
}

