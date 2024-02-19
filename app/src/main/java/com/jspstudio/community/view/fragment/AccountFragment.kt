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
import com.jspstudio.community.viewmodel.AccountViewModel

class AccountFragment: BaseFragment<FragmentAccountBinding>("AccountFragment") {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.vmAccount = ViewModelProvider(this)[AccountViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogMgr.e(TAG, "profile : " + UserData.profile)
        Glide.with(mContext).load(UserData.profile).into(binding.imgProfile)
    }
}