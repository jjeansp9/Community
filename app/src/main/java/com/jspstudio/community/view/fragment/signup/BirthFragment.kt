package com.jspstudio.community.view.fragment.signup

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.databinding.FragmentBirthBinding
import com.jspstudio.community.view.adapter.YearAdapter
import com.jspstudio.community.view.custom.CustomToast
import com.jspstudio.community.viewmodel.LoginViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BirthFragment : BaseFragment<FragmentBirthBinding>("BirthFragment") {
    private var param1: String? = null
    private var param2: String? = null

    private var selectedYear: String = ""

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
        binding = FragmentBirthBinding.inflate(inflater, container, false)
        binding.vmLogin = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSelBirth.setOnClickListener {
            showYearPicker()
        }
    }

    private fun showYearPicker() {
        val dialog = BottomSheetDialog(mContext)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_year_picker, null)
        val yearPickerRecyclerView = view.findViewById<RecyclerView>(R.id.yearPickerRecyclerView)

        val years = (1900..2012).map { it.toString() }
        val adapter = YearAdapter(years) { year ->
            selectedYear = year
            dialog.dismiss()
        }

        yearPickerRecyclerView.adapter = adapter
        dialog.setContentView(view)

//        val displayMetrics = DisplayMetrics()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val display = mContext.windowManager.currentWindowMetrics
//            // API 레벨 30에서는 다음과 같이 사용합니다.
//        } else {
//            @Suppress("DEPRECATION")
//            mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        }
//        val screenHeight = displayMetrics.heightPixels
//        val desiredMaxHeight = (screenHeight * 0.75).toInt()
//
//        // BottomSheetDialog의 최대 높이 설정
//        dialog.setOnShowListener { dialogInterface ->
//            val bottomSheetDialog = dialogInterface as BottomSheetDialog
//            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
//            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
//            bottomSheetBehavior.peekHeight = desiredMaxHeight // 원하는 최대 높이값 설정
//            // bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED // 필요한 경우 BottomSheet를 확장 상태로 설정
//        }
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        val displayMetrics = DisplayMetrics()
        val screenHeight = displayMetrics.heightPixels
        dialog.behavior.maxHeight = 500

        dialog.show()
    }
}