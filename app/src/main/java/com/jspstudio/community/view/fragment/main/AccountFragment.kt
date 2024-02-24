package com.jspstudio.community.view.fragment.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.databinding.FragmentAccountBinding
import com.jspstudio.community.user.UserData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.view.activity.EditProfileActivity
import com.jspstudio.community.viewmodel.MainViewModel

class AccountFragment: BaseFragment<FragmentAccountBinding>("AccountFragment") {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.vmMain = ViewModelProvider(mContext)[MainViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }
        }
    // todo ui 구축
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogMgr.e(TAG, "profile : " + UserData.profile)

        binding.vmMain?.setTitle("account")
        binding.vmMain?.setUserInfo()

        onClick()
    }

    private fun onClick() {
        binding.tvUpdateProfile.setOnClickListener { startUpdateActivity() }
    }

    private fun startUpdateActivity() {
        val intent = Intent(mContext, EditProfileActivity::class.java)
        resultLauncher.launch(intent)
    }
}