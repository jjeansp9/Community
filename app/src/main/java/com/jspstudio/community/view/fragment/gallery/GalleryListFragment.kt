package com.jspstudio.community.view.fragment.gallery

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.common.IntentKey
import com.jspstudio.community.databinding.FragmentGalleryListBinding
import com.jspstudio.community.model.ImageData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.view.adapter.GalleryAdapter
import com.jspstudio.community.view.custom.GridSpaceItemDecoration
import com.jspstudio.community.viewmodel.GalleryViewModel
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GalleryListFragment : BaseFragment<FragmentGalleryListBinding>("GalleryListFragment") {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter : GalleryAdapter
    private val mList = mutableListOf<ImageData>()
    private val map = LinkedHashMap<String, ImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryListBinding.inflate(inflater, container, false)
        binding.vmGal = ViewModelProvider(mContext)[GalleryViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycler()
        initObserver()
    }

    private fun initObserver() {
        binding.vmGal?.fileList?.observe(mContext) {
            if (binding.vmGal?.getFile()?.size == 0) {
                if (it.isNotEmpty()) adapter.submitList(it)
                if (mList.size == 0) mList.addAll(it)
            } else {
                val item = ArrayList(it ?: emptyList())
                for (i in binding.vmGal?.getFile()!!.indices) {
                    item[binding.vmGal?.getFile()!![i].index] = binding.vmGal?.getFile()!![i]
                    item[binding.vmGal?.getFile()!![i].index].isCheck = true
                }
                if (it.isNotEmpty()) adapter.submitList(item)
            }

        }

        binding.vmGal?.selFiles?.observe(mContext) { it ->
            if (mList.size > 0 && it.isNotEmpty()) {
                val item = arrayListOf<ImageData>()
                item.addAll(mList)
                for (i in it.indices) {
                    item[binding.vmGal?.getFile()!![i].index] = it[i]
                }
                if (it.isNotEmpty()) adapter.submitList(item)
            }
        }
    }

    private fun setRecycler() {
        adapter = GalleryAdapter(mContext, onItemClick = { it, position ->
            if (it.isCheck) {
                binding.vmGal?.addFile(it)
            }
            else {
                binding.vmGal?.removeFile(it)
                binding.vmGal?._map!![it.name]!!.num = null
            }
            setCheckNum()

        }, onDetailClick = { it, position ->
            if (mList.size > 0) {
                val bundle = bundleOf(
                    IntentKey.GALLERY_DATA to mList,
                    "position" to position
                )
                findNavController().navigate(R.id.action_first_to_detail, bundle)
            }
        })
        binding.recycler.adapter = adapter
        binding.recycler.itemAnimator = null
        binding.recycler.addItemDecoration(GridSpaceItemDecoration(3, Util.fromDpToPx(1).toInt()))
    }

    private fun setCheckNum() {
        for (i in binding.vmGal?.getFile()!!.indices) {
            binding.vmGal?.getFile()!![i].num = i + 1
            binding.vmGal?._map?.replace(binding.vmGal?.getFile()!![i].name, binding.vmGal?.getFile()!![i])
            adapter.notifyItemChanged(binding.vmGal?.getFile()!![i].index, binding.vmGal?._map!![binding.vmGal?.getFile()!![i].name]!!.num)
        }
        binding.vmGal?.updateFiles(binding.vmGal?.getFile()!!)
    }
}