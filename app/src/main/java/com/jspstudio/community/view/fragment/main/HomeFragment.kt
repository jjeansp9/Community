package com.jspstudio.community.view.fragment.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.jspstudio.community.databinding.FragmentHomeBinding
import com.google.firebase.firestore.CollectionReference
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.view.activity.EditAccompanyActivity
import com.jspstudio.community.view.adapter.AccompanyAdapter
import com.jspstudio.community.view.custom.CustomSmoothScroller
import com.jspstudio.community.view.custom.GridSpaceItemDecoration
import com.jspstudio.community.viewmodel.AccompanyViewModel
import com.jspstudio.community.viewmodel.MainViewModel
import retrofit2.Response

class HomeFragment: BaseFragment<FragmentHomeBinding>("HomeFragment") {

    private lateinit var adapter : AccompanyAdapter
    private var isModify = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.vmMain = ViewModelProvider(mContext)[MainViewModel::class.java]
        binding.vmAccompany = ViewModelProvider(mContext)[AccompanyViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            isModify = true
        }
        LogMgr.e(TAG, result.resultCode.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vmMain?.setTitle("home")
        initData()
        initView()
        observe()
        onClick()
    }

    private fun initData() {
        binding.vmAccompany?.getBoard(mContext)
    }

    private fun initView() {
        adapter = AccompanyAdapter(mContext, onItemClick = {  })
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(GridSpaceItemDecoration(1, Util.fromDpToPx(12).toInt()))

        binding.swipe.setOnRefreshListener { initData() }
    }

    private fun observe() {
        binding.vmAccompany?.accompanyItem?.observe(mContext) { itemList ->
            val newList = ArrayList(itemList ?: emptyList())
            adapter.submitList(newList.reversed())
            if (isModify) {
                isModify = false
                binding.recycler.scrollToPosition(2)
                val smoothScroller = CustomSmoothScroller(binding.recycler.context, 250f)
                smoothScroller.targetPosition = 0
                binding.recycler.layoutManager?.startSmoothScroll(smoothScroller)
            }

        }

        binding.vmAccompany?.responseCode?.observe(mContext) {
            when(it) {
                ResponseCode.SUCCESS -> {
                    binding.swipe.isRefreshing = false
                }
            }
        }
    }

    private fun onClick() {
        binding.fabAdd.setOnClickListener {
            resultLauncher.launch(Intent(mContext, EditAccompanyActivity::class.java))
            //startActivity(Intent(mContext, EditAccompanyActivity::class.java))
        }
    }
}