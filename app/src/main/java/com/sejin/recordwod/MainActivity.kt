package com.sejin.recordwod

import android.app.Activity
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.sejin.recordwod.databinding.ActivityMainBinding
import com.sejin.recordwod.utils.hide
import com.sejin.recordwod.utils.show
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import android.content.Intent
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RecordWOD)
        super.onCreate(savedInstanceState)

        // 뷰 바인딩
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // 뷰 바인딩
        setSupportActionBar(binding.topAppBar) // 액션바 바인딩

        // NavController 객체를 사용하여 NavHost 내에서 앱 탑색을 관리 -->
        val navHostFragment : NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        navController = navHostFragment.findNavController()

        observeNavElements(binding, navHostFragment.navController) // loginFragment 일 때, 액션바 숨기기

        with(navHostFragment.navController) {
            appBarConfiguration = AppBarConfiguration(graph)
            setupActionBarWithNavController(this, appBarConfiguration)
        }
        // <--
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment)
        .navigateUp(appBarConfiguration)

    private fun observeNavElements(
        binding: ActivityMainBinding,
        navController: NavController
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> binding.topAppBar.hide()
//                R.id.registerFragment -> binding.topAppBar.hide()
                else -> binding.topAppBar.show()
            }
        }
    }
}
