package com.jspstudio.community.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.databinding.FragmentAccountBinding
import com.jspstudio.community.user.UserData
import com.jspstudio.community.util.LogMgr
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
    // todo : 프로필 화면 ui 잘해보자
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogMgr.e(TAG, "profile : " + UserData.profile)
        binding.vmMain?.setTitle("account")
        binding.vmMain?.setUserInfo()
    }
}