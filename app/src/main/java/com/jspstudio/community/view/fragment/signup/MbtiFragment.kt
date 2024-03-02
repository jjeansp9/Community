package com.jspstudio.community.view.fragment.signup

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.databinding.FragmentMbtiBinding
import com.jspstudio.community.user.MyData
import com.jspstudio.community.view.adapter.BottomSeetListAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MbtiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MbtiFragment : BaseFragment<FragmentMbtiBinding>("MbtiFragment") {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        binding = FragmentMbtiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSelMbti.setOnClickListener {
            showMbtiPicker()
        }
    }

    private fun setTvDrawable(res : Int) {
        binding.tvSelMbti.setCompoundDrawablesWithIntrinsicBounds(
            0, // 왼쪽 드로어블 리소스 ID
            0, // 상단 드로어블 리소스 ID
            res, // 오른쪽(끝) 드로어블 리소스 ID
            0 // 하단 드로어블 리소스 ID
        )
    }

    public fun showMbtiPicker() {

        val dialog = BottomSheetDialog(mContext)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet_list, null)
        val yearPickerRecyclerView = view.findViewById<RecyclerView>(R.id.yearPickerRecyclerView)

        val mbtiType = listOf(
            "INTJ", "INTP", "ENTJ", "ENTP",
            "INFJ", "INFP", "ENFJ", "ENFP",
            "ISTJ", "ISFJ", "ESTJ", "ESFJ",
            "ISTP", "ISFP", "ESTP", "ESFP").sorted()
        val adapter = BottomSeetListAdapter(mContext, mbtiType) { mbti ->
            binding.tvSelMbti.text = mbti
            MyData.mbti = mbti
            dialog.dismiss()
        }
        dialog.setOnShowListener {
            binding.tvSelMbti.setBackgroundResource(R.drawable.bg_stroke_focus)
            setTvDrawable(R.drawable.ic_arrow_up)
        }
        dialog.setOnDismissListener {
            binding.tvSelMbti.setBackgroundResource(R.drawable.bg_stroke_default)
            setTvDrawable(R.drawable.ic_arrow_down)
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