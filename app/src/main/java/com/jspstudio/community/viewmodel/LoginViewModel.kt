package com.jspstudio.community.viewmodel

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.Firebase
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.sns.GoogleLoginMgr
import com.jspstudio.community.sns.KakaoLoginMgr
import com.jspstudio.community.sns.NaverLoginMgr
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.view.util.Constant
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kotlinx.coroutines.withContext

class LoginViewModel(context : ComponentActivity) : BaseViewModel("LoginViewModel") {

    val kakaoLoginMgr : KakaoLoginMgr = KakaoLoginMgr(context)
    val naverLoginMgr : NaverLoginMgr = NaverLoginMgr(context)
    val googleLoginMgr : GoogleLoginMgr = GoogleLoginMgr(context)

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
        naverLoginMgr.startNaverLogin {
            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(result: NidProfileResponse) {
                    val id = result.profile?.id!!
                    val name = result.profile?.name.toString()
                    val email = result.profile?.email.toString()
                }
                override fun onError(errorCode: Int, message: String) {}
                override fun onFailure(httpStatus: Int, message: String) {}
            })
        }
    }

    fun googleLogin(auth: FirebaseAuth) {
//        googleLoginMgr.startGoogleLogin { idToken ->
//            // Handle the ID token and sign in with Firebase Auth
//            val credential = GoogleAuthProvider.getCredential(idToken, null)
//            auth.signInWithCredential(credential)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        // Google login successful
//                    } else {
//                        // Google login failed
//                    }
//                }
//        }
    }

    fun emailLogin(auth: FirebaseAuth, email: String, password: String, context: Activity) {

        // ActionCodeSettings 구성
        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://community-5d51f.firebaseapp.com/finishSignUp?cartId=1234"
            // This must be true
            handleCodeInApp = true
            //setIOSBundleId("com.example.ios")
            setAndroidPackageName(
                "com.jspstudio.community",
                true, // installIfNotAvailable
                "12", // minimumVersion
            )
        }

        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    LogMgr.d("Auth", "Email sent.")
                } else {
                    LogMgr.e("Auth", "Failed to send email: " + task.exception)
                }
            }

//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(context) { task ->
//                if (task.isSuccessful) {
//                    // 회원가입 성공
//                    LogMgr.d("Auth", "createUserWithEmail:success")
//                    val user = auth.currentUser
//                    user?.sendEmailVerification()
//                        ?.addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                LogMgr.d("Auth", "Email sent.")
//                            }
//                        }
//                } else {
//                    // 회원가입 실패
//                    LogMgr.w("Auth", "createUserWithEmail:failure" + task.exception)
//                }
//            }
    }

    fun guestLogin() {

    }
}