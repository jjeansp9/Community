package com.jspstudio.community.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityGalleryBinding
import com.jspstudio.community.databinding.ActivityMainBinding
import com.jspstudio.community.view.custom.CustomToast
import com.jspstudio.community.view.fragment.KeepStateFragment
import com.jspstudio.community.viewmodel.MainViewModel

// github token : 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

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main, "MainActivity") {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding.vmMain = viewModel
        binding.lifecycleOwner= this
        setNavigation()
        observe()
    }
    private fun setNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navigator = KeepStateFragment(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)

        navController.navigatorProvider.addNavigator(navigator)

        navController.setGraph(R.navigation.nav_graph)

        // MainActivity의 main_navi와 navController 연결
        binding.mainNavi.setupWithNavController(navController)
    }

    private fun observe() {
        viewModel?.title?.observe(this) { title ->
        }
    }
}