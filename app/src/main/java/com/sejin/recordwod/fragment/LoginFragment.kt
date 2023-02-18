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
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sejin.recordwod.R
import com.sejin.recordwod.base.BaseFragment
import com.sejin.recordwod.databinding.FragmentLoginBinding
import com.sejin.recordwod.view.state.LoginState
import com.sejin.recordwod.view.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginState, LoginViewModel>() {

    override val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.nav_graph)

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
            KakaoLoginBtn.setOnClickListener { viewModel.kakaoLogin() }
            googleLoginBtn.setOnClickListener { }
        }
        //<--

    }

    override fun render(state: LoginState) {
        if(state.isLoggedIn){
            GoMain()
        }

        val errorMessage = state.error
        if(errorMessage != null ){
//            Toast
        }
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

    // 구글 로그인 첫번째
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        if(currentUser != null ) updateUI(currentUser)
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

    // 신규유저 회원가입
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
     // git test
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
//            binding.googleLoginBtn.visibility = View.GONE
//        }
        GoMain()
    }
}

