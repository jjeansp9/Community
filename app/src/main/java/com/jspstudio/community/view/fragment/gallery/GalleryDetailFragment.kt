package com.jspstudio.community.view.fragment.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.common.IntentKey
import com.jspstudio.community.databinding.FragmentGalleryDetailBinding
import com.jspstudio.community.model.ImageData
import com.jspstudio.community.view.adapter.PhotoViewPagerAdapter
import com.jspstudio.community.viewmodel.GalleryViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GalleryDetailFragment : BaseFragment<FragmentGalleryDetailBinding>("GalleryDetailFragment") {
    private var param1: String? = null
    private var param2: String? = null
    private var mList = mutableListOf<ImageData>()
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            mList = it.getParcelableArrayList<ImageData>(IntentKey.GALLERY_DATA)!!
            position = it.getInt("position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryDetailBinding.inflate(inflater, container, false)
        binding.vmGal = ViewModelProvider(mContext)[GalleryViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private lateinit var adapter : PhotoViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //if (mImageList != null && mImageList.size > 0) tvPage.setText((position + 1).toString() + " / " + mImageList.size)
        adapter = PhotoViewPagerAdapter(mList)

        binding.viewPager.setAdapter(adapter)
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(index: Int) {
                position = index
                val item: ImageData = mList.get(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.viewPager.setCurrentItem(position)
    }
}