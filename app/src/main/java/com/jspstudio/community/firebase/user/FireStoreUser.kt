package com.jspstudio.community.firebase.user

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jspstudio.community.firebase.user.field.DocUser
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.user.MyData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.util.UtilPref
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object FireStoreUser {

    private const val TAG = "FireStoreUser"

    fun getUserData(context: Context, onResponse: ((responseCode: Int) -> Unit)) {
        val firestore = FirebaseFirestore.getInstance()

        val userRef: CollectionReference = firestore.collection(DocUser.USER) // 컬렉션명

        userRef.addSnapshotListener { value, error ->
            val docChangeList : List<DocumentChange> = value!!.documentChanges
            for (documentChange : DocumentChange in docChangeList) {
                // 변경된 document의 데이터를 촬영한 스냅샷 얻어오기
                val snapshot : DocumentSnapshot = documentChange.document
                val profile : Map<String, String> = snapshot.data as Map<String, String>
                LogMgr.e(TAG, snapshot.data.toString())
                val id = profile[DocUser.ID]
                if (id.toString() == MyData.id) {
                    MyData.name = profile[DocUser.NAME]
                    MyData.gender = profile[DocUser.GENDER]
                    MyData.birth = profile[DocUser.BIRTH]
                    MyData.mbti = profile[DocUser.MBTI]
                    MyData.aboutMe = profile[DocUser.ABOUT_ME]
                    MyData.profile = profile[DocUser.PROFILE]
                    MyData.loginType = profile[DocUser.LOGIN_TYPE]
                    MyData.startDate = profile[DocUser.START_DATE]
                    UtilPref.setUserData(context)
                    onResponse(ResponseCode.SUCCESS)
                    return@addSnapshotListener
                }
            }
        }
    }

    fun addUser(context : Context, onResponse: ((responseCode: Int) -> Unit)) {
        val firestore = FirebaseFirestore.getInstance()

        val userRef: CollectionReference = firestore.collection(DocUser.USER) // 컬렉션명
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        val profile: MutableMap<String, String> = HashMap()

        profile[DocUser.NAME] = MyData.name.toString()
        profile[DocUser.GENDER] = MyData.gender.toString()
        profile[DocUser.BIRTH] = MyData.birth.toString()
        profile[DocUser.MBTI] = MyData.mbti.toString()
        profile[DocUser.ABOUT_ME] = ""
        profile[DocUser.PROFILE] = Util.getStr(MyData.profile.toString())
        profile[DocUser.LOGIN_TYPE] = MyData.loginType.toString()
        profile[DocUser.START_DATE] = date

        if (MyData.id == null || MyData.id!!.trim().isEmpty()) {
            val random = Random
            val length = random.nextInt(24)
            val stringBuilder = StringBuilder()
            val str = "qwertzuiopasdfghjklyxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"

            for (i in 0 until length) {
                val index = random.nextInt(str.length)
                val randomChar = str[index]
                stringBuilder.append(randomChar)
            }
            MyData.id = stringBuilder.toString()
        }
        profile[DocUser.ID] = MyData.id.toString()
        MyData.startDate = date

        //userRef.document(date + "_" + UserData.id.toString()).set(profile)
        userRef.document(MyData.id.toString()).set(profile)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    UtilPref.setUserData(context)
                    onResponse(ResponseCode.SUCCESS)

                } else {
                    onResponse(ResponseCode.FAIL)
                }
            }
    }

    fun deleteUser(userId: String, onResponse: ((responseCode: Int) -> Unit)) {
        val firestore = FirebaseFirestore.getInstance()

        // 컬렉션 참조 생성
        val userRef: CollectionReference = firestore.collection(DocUser.USER)

        // USER_ID를 기반으로 특정 문서 찾기
        // 이 예제에서는 문서 ID가 UserData.id를 포함하고 있다고 가정
        // 실제 문서 ID 구성 방식에 따라 적절히 조정 필요
        val documentId = "특정 조건에 맞는 문서 ID" // 예: sdf.format(Date()) + "_" + userId

        // 문서 삭제
        userRef.document(documentId).delete().addOnSuccessListener {
            // 성공적으로 문서가 삭제되었을 때의 처리
            onResponse(ResponseCode.SUCCESS)
        }.addOnFailureListener { e ->
            // 문서 삭제에 실패했을 때의 처리
            onResponse(ResponseCode.FAIL)
        }
    }
}