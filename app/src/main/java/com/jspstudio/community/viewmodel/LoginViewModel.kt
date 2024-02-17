package com.jspstudio.community.viewmodel

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jspstudio.community.base.BaseViewModel
import com.jspstudio.community.user.UserData
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.sns.GoogleLoginMgr
import com.jspstudio.community.sns.KakaoLoginMgr
import com.jspstudio.community.sns.NaverLoginMgr
import com.jspstudio.community.util.LogMgr
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginViewModel() : BaseViewModel("LoginViewModel") {

    fun kakaoLogin(kakaoLoginMgr: KakaoLoginMgr) {
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
                    UserData.profileImg = progileImg
                    UserData.loginType = "Kakao"
                    requestLogin()
                }
            }
        }
    }

    fun naverLogin(naverLoginMgr : NaverLoginMgr) {
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
                    UserData.profileImg = progileImg
                    UserData.loginType = "Naver"
                    requestLogin()
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
        _resultCode.value = ResponseCode.NOT_FOUND
    }

    private val _resultCode = MutableLiveData<Int>()
    val resultCode : LiveData<Int> = _resultCode

    private fun requestLogin() {
        // 파이어베이스를 이용하여 회원정보 저장

        // firebase db에 저장하기 위해 Map Collection으로 묶어서 저장
        val firestore = FirebaseFirestore.getInstance()

        val userRef: CollectionReference = firestore.collection("user") // 컬렉션명 : user

        userRef.addSnapshotListener { value, error ->
            val docChangeList : List<DocumentChange> = value!!.documentChanges
            for (documentChange : DocumentChange in docChangeList) {
                // 변경된 document의 데이터를 촬영한 스냅샷 얻어오기
                val snapshot : DocumentSnapshot = documentChange.document
                val profile : Map<String, String> = snapshot.data as Map<String, String>
                LogMgr.e(TAG, snapshot.data.toString())
                val id = profile["id"]
                if (id.toString() == UserData.id.toString()) { // db에 저장된 id가 있는경우 메인화면으로
                    _resultCode.value = ResponseCode.SUCCESS
                    return@addSnapshotListener
                }
            }

            _resultCode.value = ResponseCode.NOT_FOUND // 없는경우 회원가입으로
        }

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
//
//        val profile: MutableMap<String, String> = HashMap()
//        profile["name"] = UserData.name.toString()
//        profile["id"] = UserData.id.toString()
//        profile["profile"] = ""
//        profile["loginType"] = UserData.loginType.toString()
//        profile["start_time"] = sdf.format(Date())
//
//        userRef.document(sdf.format(Date()) + "_" + UserData.id.toString()).set(profile)
    }

    var _name = MutableLiveData<String>()
    var name : LiveData<String> = _name

    fun etName(s: String){
        _name.value = s.ifEmpty { "" }
    }
}