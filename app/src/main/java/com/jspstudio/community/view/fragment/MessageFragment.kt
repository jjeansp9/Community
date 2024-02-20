package com.jspstudio.community.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.databinding.FragmentMessageBinding
import com.jspstudio.community.viewmodel.MainViewModel

class MessageFragment: BaseFragment<FragmentMessageBinding>("MessageFragment") {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater, container, false)
        binding.vmMain = ViewModelProvider(mContext)[MainViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vmMain?.setTitle("message")
    }
}