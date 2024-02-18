package com.jspstudio.community.view.fragment.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.databinding.FragmentGenderBinding
import com.jspstudio.community.user.UserData
import com.jspstudio.community.view.custom.CustomToast
import com.jspstudio.community.viewmodel.LoginViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GenderFragment : BaseFragment<FragmentGenderBinding>("GenderFragment") {
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
        binding = FragmentGenderBinding.inflate(inflater, container, false)
        binding.vmLogin = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    public fun checkGender() : Boolean {
        if (binding.rbMale.isChecked) {
            UserData.gender = "male"
            return true
        } else if (binding.rbFemale.isChecked) {
            UserData.gender = "female"
            return true
        }
        return false
    }
}