package com.jspstudio.community.sns

import android.content.Context
import com.jspstudio.community.util.LogMgr
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.NaverIdLoginSDK

class NaverLoginMgr(private val context: Context) {
    companion object {
        private const val TAG = "NaverLoginMgr"
    }
    lateinit var oAuthLoginCallback : OAuthLoginCallback
        private set

    fun naverSetOAuthLoginCallback(updateSocialToken : (String) -> Unit) {
        oAuthLoginCallback = object : OAuthLoginCallback{
            override fun onError(errorCode: Int, message: String) {
                LogMgr.e(TAG, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                LogMgr.e(TAG, message)
            }

            override fun onSuccess() {
                updateSocialToken(NaverIdLoginSDK.getAccessToken()!!)
                // 네이버 로그인 API 호출 성공 시 유저 정보를 가져온다

            }

        }
    }

    fun startNaverLogin(updateSocialToken: (String) -> Unit) {
        naverSetOAuthLoginCallback { updateSocialToken(it)}
        NaverIdLoginSDK.authenticate(context, oAuthLoginCallback)

    }
}