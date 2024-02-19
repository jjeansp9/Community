package com.jspstudio.community.sns

import android.content.Context
import com.jspstudio.community.util.LogMgr
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaoLoginMgr(private val context : Context) {

    companion object { private const val TAG = "KakaoLoginMgr" }

    enum class KakaoLoginState { KAKAO_TALK_LOGIN, KAKAO_ACCOUNT_LOGIN }

    private lateinit var kakaoLoginState : KakaoLoginState
    private lateinit var kakaoLoginCallback : (OAuthToken?, Throwable?) -> Unit

    fun startKakaoLogin(updateSocialToken : (String) -> Unit) {
        kakaoLoginState = getKakaoLoginState()
        kakaoLoginCallback = getLoginCallback(updateSocialToken)
        when (kakaoLoginState) {
            KakaoLoginState.KAKAO_TALK_LOGIN -> onKakaoTalkLogin(updateSocialToken)
            KakaoLoginState.KAKAO_ACCOUNT_LOGIN -> onKakaoAccountLogin()
        }
    }
    private fun getKakaoLoginState() : KakaoLoginState =
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            KakaoLoginState.KAKAO_TALK_LOGIN
        }else{
            KakaoLoginState.KAKAO_ACCOUNT_LOGIN
        }
    private fun getLoginCallback(updateSocialToken: (String) -> Unit) : (OAuthToken?, Throwable?) -> Unit {
        return {token, error ->
            if(error != null) { //카카오계정 로그인 실패
                LogMgr.e(TAG, "login fail : $error.message")
            }else if(token != null){
                updateSocialToken(token.accessToken)
            }
        }
    }

    private fun onKakaoTalkLogin(updateSocialToken : (String) -> Unit) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)){
            UserApiClient.instance.loginWithKakaoTalk(context) {    token, error ->
                if(error != null) { //카카오톡 로그인 실패
                    LogMgr.e(TAG, "login fail kakaotalk : $error.message")
//                    if(error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                        return@loginWithKakaoTalk
//                    }
                    onKakaoAccountLogin()
                }else if(token != null) {
                    updateSocialToken(token.accessToken)
                }

            }
        }else{
            onKakaoAccountLogin()
        }

    }
    private fun onKakaoAccountLogin() {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
    }
}