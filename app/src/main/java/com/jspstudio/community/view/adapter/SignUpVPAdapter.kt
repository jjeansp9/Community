package com.jspstudio.community.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jspstudio.community.view.fragment.signup.GenderAndBirthFragment
import com.jspstudio.community.view.fragment.signup.MbtiFragment
import com.jspstudio.community.view.fragment.signup.NameFragment


class SignUpVPAdapter(fragment: FragmentActivity?) : FragmentStateAdapter(fragment!!) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NameFragment()
            1 -> GenderAndBirthFragment()
            else -> MbtiFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}