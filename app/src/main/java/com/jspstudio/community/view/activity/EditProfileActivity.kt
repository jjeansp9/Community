package com.jspstudio.community.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityEditProfileBinding
import com.jspstudio.community.viewmodel.MainViewModel

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(R.layout.activity_edit_profile, "EditProfileActivity") {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vmMain = viewModel
        binding.lifecycleOwner= this



    }
}