package com.jspstudio.community.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.firebase.FireStoreMgr
import com.jspstudio.community.firebase.user.FireStoreUser
import com.jspstudio.community.firebase.user.field.FirestoreDBUser
import com.jspstudio.community.user.UserData
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.sns.GoogleLoginMgr
import com.jspstudio.community.sns.KakaoLoginMgr
import com.jspstudio.community.sns.NaverLoginMgr
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.view.util.Constant
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginViewModel() : BaseViewModel("LoginViewModel") {

    fun kakaoLogin(context: Context, kakaoLoginMgr: KakaoLoginMgr) {
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

                    val id = user?.id.toString()
                    val name = user?.kakaoAccount?.profile?.nickname.toString()
                    val progileImg = user?.kakaoAccount!!.profile!!.profileImageUrl.toString()

                    UserData.id = id
                    //UserData.name = name
                    UserData.profile = progileImg
                    UserData.loginType = Constant.LOGIN_TYPE_KAKAO
                    requestLogin(context)
                }
            }
        }
    }

    fun naverLogin(context: Context, naverLoginMgr : NaverLoginMgr) {
        naverLoginMgr.startNaverLogin {
            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(result: NidProfileResponse) {
                    val id = result.profile?.id!!
                    val name = result.profile?.name.toString()
                    val email = result.profile?.email.toString()
                    val progileImg = result.profile?.profileImage.toString()
                    LogMgr.e(TAG, "naver id: " + id)
                    LogMgr.e(TAG, "naver name: " + name)
                    LogMgr.e(TAG, "naver email: " + email)
                    if (progileImg != null) LogMgr.e(TAG, "naver profil url: " + progileImg)

                    UserData.id = id
                    //UserData.name = name
                    UserData.profile = progileImg
                    UserData.loginType = Constant.LOGIN_TYPE_NAVER
                    requestLogin(context)
                }
                override fun onError(errorCode: Int, message: String) {}
                override fun onFailure(httpStatus: Int, message: String) {}
            })
        }
    }

    fun googleLogin(googleLoginMgr: GoogleLoginMgr, auth: FirebaseAuth) {
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
    }

    fun guestLogin() {
        UserData.loginType = Constant.LOGIN_TYPE_NORMAL
        _resultCode.value = ResponseCode.NOT_FOUND
    }

    private val _resultCode = MutableLiveData<Int>()
    val resultCode : LiveData<Int> = _resultCode

    private fun requestLogin(context: Context) {
        FireStoreMgr.checkData(FirestoreDBUser.USER, FirestoreDBUser.USER_ID, UserData.id.toString()) {
            when(it) {
                ResponseCode.DUPLICATE_ERROR -> {
                    FireStoreUser.getUserData(context) {
                        if (it == ResponseCode.SUCCESS) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                _resultCode.value = ResponseCode.SUCCESS
                            }, 1000)
                        }
                    }
                }
                ResponseCode.NOT_FOUND -> {
                    _resultCode.value = ResponseCode.NOT_FOUND
                }
            }
        }
    }

    var _name = MutableLiveData<String>()
    var name : LiveData<String> = _name

    fun etName(s: String){ _name.value = s.ifEmpty { "" } }
}