package com.jspstudio.community

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK

class CommunityApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
        NaverIdLoginSDK.initialize(this, this.getString(R.string.naver_client_id), this.getString(R.string.naver_client_secret), this.getString(R.string.app_name)) // 네이버 클라이언트 등록
    }
}