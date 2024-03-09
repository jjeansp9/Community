package com.jspstudio.community.view.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityGalleryBinding
import com.jspstudio.community.firebase.StorageMgr
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.view.fragment.KeepStateFragment
import com.jspstudio.community.viewmodel.GalleryViewModel

class GalleryActivity : BaseActivity<ActivityGalleryBinding>(R.layout.activity_gallery, "GalleryActivity") {

    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmGal = viewModel
        binding.lifecycleOwner = this

        setNavigation()
        setBackPressed()
        initObserver()
    }

    private fun initObserver() {
        binding.vmGal?.sendFileList?.observe(this) {selFiles ->
            LogMgr.e(TAG, selFiles.size.toString())
            selFiles.forEach { StorageMgr.sendImg(this, it.uri!!) }
        }
    }

    private fun setNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navigator =
            KeepStateFragment(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)

        navController.navigatorProvider.addNavigator(navigator)

        navController.setGraph(R.navigation.nav_graph_gallery)
    }

    private fun setBackPressed() {
        // 뒤로 가기 버튼 동작을 처리하기 위한 onBackPressedDispatcher 설정
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // NavController를 사용하여 프래그먼트 스택에서 뒤로 이동
                if (!navController.popBackStack()) {
                    // 스택에 더 이상 프래그먼트가 없으면 Activity 종료
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.imgBack.setOnClickListener {
            if (!navController.popBackStack()) {
                finish()
            } else {
                navController.popBackStack(R.id.gallery_list_fragment, false, true)
            }
        }
    }
}