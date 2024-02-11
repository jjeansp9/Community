package com.jspstudio.community.viewmodel

import android.content.Context
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.sns.KakaoLoginMgr
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.view.util.Constant
import com.kakao.sdk.user.UserApiClient

class LoginViewModel(context : Context) : BaseViewModel("LoginViewModel") {

    val kakaoLoginMgr : KakaoLoginMgr = KakaoLoginMgr(context)

    fun kakaoLogin() {
        kakaoLoginMgr.startKakaoLogin {
            UserApiClient.instance.me { user, throwable ->
                if (it != null && it.isNotEmpty()) {
                    LogMgr.i(TAG, "카카오 고유ID : ${user?.id}")
                    LogMgr.i(TAG, "카카오 닉네임 : ${user?.kakaoAccount?.profile?.nickname}")
                    LogMgr.i(TAG, "카카오 이름 : ${user?.kakaoAccount?.name}")
                    LogMgr.i(TAG, "카카오 프로필사진 : $user.kakaoAccount!!.profile!!.profileImageUrl")
                    //user?.id?.let{snsUserData.snsId = it.toString()}
                    //user?.kakaoAccount?.name?.let {snsUserData.name = it}
                    //user?.kakaoAccount?.gender?.let{ snsUserData.sex = it.toString()}

                }
            }
        }
    }

    fun naverLogin() {

    }

    fun googleLogin() {

    }

    fun guestLogin() {

    }
}