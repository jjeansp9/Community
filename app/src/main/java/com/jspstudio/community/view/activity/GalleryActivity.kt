package com.jspstudio.community.view.activity

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityChatBinding
import com.jspstudio.community.databinding.ActivityGalleryBinding
import com.jspstudio.community.databinding.ActivityMainBinding
import com.jspstudio.community.model.ImageData
import com.jspstudio.community.user.MyData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.view.adapter.ChatAdapter
import com.jspstudio.community.view.adapter.GalleryAdapter
import com.jspstudio.community.view.custom.GridSpaceItemDecoration
import com.jspstudio.community.view.fragment.KeepStateFragment
import com.jspstudio.community.viewmodel.GalleryViewModel
import com.jspstudio.community.viewmodel.MainViewModel

class GalleryActivity : BaseActivity<ActivityGalleryBinding>(R.layout.activity_gallery, "GalleryActivity") {

    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmGal = viewModel
        binding.lifecycleOwner= this

        setNavigation()
    }

    private fun setNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navigator = KeepStateFragment(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)

        navController.navigatorProvider.addNavigator(navigator)

        navController.setGraph(R.navigation.nav_graph_gallery)
    }
}