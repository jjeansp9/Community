package com.jspstudio.community.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.user.UserData
import com.jspstudio.community.util.LogMgr
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FireStoreMgr {
    private const val TAG = "FireStoreMgr"

    // 해당 컬렉션 안에 해당 필드의 같은 data가 있는지 check
    fun checkData(collectionName : String, fieldName: String, data: String, onResponse: ((responseCode: Int) -> Unit)) {
        val firestore = FirebaseFirestore.getInstance()

        val userRef: CollectionReference = firestore.collection(collectionName) // 컬렉션명

        userRef.addSnapshotListener { value, error ->
            val docChangeList : List<DocumentChange> = value!!.documentChanges
            for (documentChange : DocumentChange in docChangeList) {
                // 변경된 document의 데이터를 촬영한 스냅샷 얻어오기
                val snapshot : DocumentSnapshot = documentChange.document
                val profile : Map<String, String> = snapshot.data as Map<String, String>
                LogMgr.e(TAG, snapshot.data.toString())
                val id = profile[fieldName]
                if (id.toString() == data) {
                    onResponse(ResponseCode.DUPLICATE_ERROR)
                    return@addSnapshotListener
                }

                LogMgr.e(TAG, id + "," + data)
            }
            onResponse(ResponseCode.NOT_FOUND)
        }
    }

    fun addUser(onResponse: ((responseCode: Int) -> Unit)) {
        val firestore = FirebaseFirestore.getInstance()

        val userRef: CollectionReference = firestore.collection(FirebaseDBName.USER) // 컬렉션명

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

        val profile: MutableMap<String, String> = HashMap()
        profile[FirebaseDBName.USER_ID] = UserData.id.toString()
        profile[FirebaseDBName.USER_NAME] = UserData.name.toString()
        profile[FirebaseDBName.USER_GENDER] = UserData.gender.toString()
        profile[FirebaseDBName.USER_BIRTH] = UserData.birth.toString()
        profile[FirebaseDBName.USER_MBTI] = UserData.mbti.toString()
        profile[FirebaseDBName.USER_PROFILE] = ""
        profile[FirebaseDBName.USER_LOGIN_TYPE] = UserData.loginType.toString()
        profile[FirebaseDBName.USER_START_DATE] = sdf.format(Date())

        UserData.startDate = sdf.format(Date())

        userRef.document(sdf.format(Date()) + "_" + UserData.id.toString()).set(profile)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResponse(ResponseCode.SUCCESS)
            } else {
                onResponse(ResponseCode.FAIL)
            }
        }
    }

    fun deleteUserById(userId: String, onResponse: ((responseCode: Int) -> Unit)) {
        val firestore = FirebaseFirestore.getInstance()

        // 컬렉션 참조 생성
        val userRef: CollectionReference = firestore.collection(FirebaseDBName.USER)

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