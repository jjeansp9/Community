package com.jspstudio.community.firebase.board.accompany

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jspstudio.community.firebase.board.accompany.field.FireStoreDBAccompany
import com.jspstudio.community.firebase.user.FireStoreUser
import com.jspstudio.community.firebase.user.field.FirestoreDBUser
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.user.UserData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import com.jspstudio.community.util.UtilPref
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FireStoreAccompany {
    private const val TAG = "FireStoreAccompany"

    fun getBoard(context: Context, onResponse: ((MutableList<AccompanyData>) -> Unit)) {

        val list : MutableList<AccompanyData> = mutableListOf()

        val firestore = FirebaseFirestore.getInstance()
        val userRef: CollectionReference = firestore.collection(FireStoreDBAccompany.ACCOMPANY) // 컬렉션명
        userRef.addSnapshotListener { value, error ->
            val docChangeList : List<DocumentChange> = value!!.documentChanges
            for (documentChange : DocumentChange in docChangeList) {
                // 변경된 document의 데이터를 촬영한 스냅샷 얻어오기
                val snapshot : DocumentSnapshot = documentChange.document
                val key : Map<String, String> = snapshot.data as Map<String, String>
                LogMgr.e(TAG, snapshot.data.toString())
                list.add(AccompanyData(
                    id = key[FireStoreDBAccompany.ID]!!,
                    name = key[FireStoreDBAccompany.NAME]!!,
                    gender = key[FireStoreDBAccompany.GENDER]!!,
                    birth = key[FireStoreDBAccompany.BIRTH]!!,
                    mbti = key[FireStoreDBAccompany.MBTI]!!,
                    profile = key[FireStoreDBAccompany.PROFILE]!!,
                    title = key[FireStoreDBAccompany.TITLE]!!,
                    insertDate = key[FireStoreDBAccompany.INSERT_DATE]!!,
                ))
            }
            if (docChangeList.size <= list.size) {
                onResponse(list)
            }
        }
    }

    fun addBoard(context : Context, item : AccompanyData, onResponse: ((responseCode: Int) -> Unit)) {
        val firestore = FirebaseFirestore.getInstance()

        val userRef: CollectionReference = firestore.collection(FireStoreDBAccompany.ACCOMPANY) // 컬렉션명
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.KOREA).format(Date())
        val key: MutableMap<String, String> = HashMap()

        key[FireStoreDBAccompany.ID] = UserData.id.toString()
        key[FireStoreDBAccompany.NAME] = UserData.name.toString()
        key[FireStoreDBAccompany.GENDER] = UserData.gender.toString()
        key[FireStoreDBAccompany.BIRTH] = UserData.birth.toString()
        key[FireStoreDBAccompany.MBTI] = UserData.mbti.toString()
        key[FireStoreDBAccompany.PROFILE] = Util.getStr(UserData.profile.toString())
        key[FireStoreDBAccompany.TITLE] = Util.getStr(item.title)
        key[FireStoreDBAccompany.INSERT_DATE] = date

        userRef.document(date + "_" + UserData.id.toString()).set(key)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResponse(ResponseCode.SUCCESS)

                } else {
                    onResponse(ResponseCode.FAIL)
                }
            }
    }
}