package com.jspstudio.community.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.util.LogMgr

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
                val getData = profile[fieldName]
                if (getData.toString() == data) {
                    onResponse(ResponseCode.DUPLICATE_ERROR)
                    return@addSnapshotListener
                }
            }
            onResponse(ResponseCode.NOT_FOUND)
        }
    }
}