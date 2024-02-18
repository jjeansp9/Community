package com.jspstudio.community.view.fragment.signup

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
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

        val height: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = mContext.windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout()
            )
            val insetsHeight = insets.top + insets.bottom
            height = (windowMetrics.bounds.height() - insetsHeight) * 2 / 3
        } else {
            @Suppress("DEPRECATION")
            val displayMetrics = DisplayMetrics()
            mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
            height = displayMetrics.heightPixels * 2 / 3
        }
        dialog.behavior.maxHeight = height

        dialog.show()
    }
}