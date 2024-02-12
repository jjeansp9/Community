package com.jspstudio.community.sns

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.jspstudio.community.R
import com.kakao.sdk.auth.model.OAuthToken

class GoogleLoginMgr(private val context: ComponentActivity) {

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    init {
        // resultLauncher 초기화를 생성자 또는 초기화 블록으로 이동
        resultGoogleLogin { token ->
            // 로그인 성공 후 처리할 작업
        }
    }

    fun startGoogleLogin(callback: (String) -> Unit) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        val signInIntent = googleSignInClient.signInIntent

        if (resultLauncher != null) resultLauncher?.launch(signInIntent)
    }

    private fun resultGoogleLogin(callback: (String) -> Unit) {
        resultLauncher = context.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google 로그인 성공
                        val account = task.getResult(ApiException::class.java)!!
                        callback(account.idToken!!)
                    } catch (e: ApiException) {
                        // Google 로그인 실패
                    }
                }
            }
        }
    }
}