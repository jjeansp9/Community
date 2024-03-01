package com.jspstudio.community.firebase.chat

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jspstudio.community.R
import com.jspstudio.community.firebase.board.accompany.field.DocAccompany
import com.jspstudio.community.firebase.chat.field.DocChat
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.model.MessageData
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.user.UserData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FireStoreChat {
    private const val TAG = "FireStoreChat"

    fun getMsgList(context: Context, userId: String, onResponse: ((MutableList<MessageData>) -> Unit)) {

        val list : MutableList<MessageData> = mutableListOf()

        val firestore = FirebaseFirestore.getInstance()
        val userRef: CollectionReference = firestore.collection(DocChat.CHAT).document(UserData.id.toString()).collection(userId) // 컬렉션명
        userRef.addSnapshotListener { value, error ->
            val docChangeList : List<DocumentChange> = value!!.documentChanges
            for (documentChange : DocumentChange in docChangeList) {

                val snapshot : DocumentSnapshot = documentChange.document
                val key : Map<String, String> = snapshot.data as Map<String, String>

                LogMgr.e(TAG, snapshot.data.toString())
                list.add(MessageData(
                    id = key[DocChat.ID]!!,
                    name = key[DocChat.NAME]!!,
                    gender = key[DocChat.GENDER]!!,
                    birth = Util.calculateAgeFromYearOfBirth(key[DocChat.BIRTH]!!) + context.getString(R.string.age_sub),
                    mbti = key[DocChat.MBTI]!!,
                    profile = key[DocChat.PROFILE]!!,
                    message = key[DocChat.MESSAGE]!!,
                    msgInsertDate = if (key[DocChat.MSG_INSERT_DATE] == null) ""
                    else Util.formatDate(key[DocChat.MSG_INSERT_DATE]!!, "yyyy-MM-dd", "yy.M.d")!!
                ))
            }
            if (docChangeList.size <= list.size) {
                onResponse(list)
            }
        }
    }

    fun getMsg(context: Context, userId: String, onResponse: ((MutableList<MessageData>) -> Unit)) {

        val list : MutableList<MessageData> = mutableListOf()

        val firestore = FirebaseFirestore.getInstance()
        val userRef: CollectionReference = firestore.collection(DocChat.CHAT).document(UserData.id.toString()).collection(userId) // 컬렉션명
        userRef.addSnapshotListener { value, error ->
            val docChangeList : List<DocumentChange> = value!!.documentChanges
            for (documentChange : DocumentChange in docChangeList) {

                val snapshot : DocumentSnapshot = documentChange.document
                val key : Map<String, String> = snapshot.data as Map<String, String>

                LogMgr.e(TAG, snapshot.data.toString())
                list.add(MessageData(
                    id = key[DocChat.ID]!!,
                    name = key[DocChat.NAME]!!,
                    gender = key[DocChat.GENDER]!!,
                    birth = Util.calculateAgeFromYearOfBirth(key[DocChat.BIRTH]!!) + context.getString(R.string.age_sub),
                    mbti = key[DocChat.MBTI]!!,
                    profile = key[DocChat.PROFILE]!!,
                    message = key[DocChat.MESSAGE]!!,
                    msgInsertDate = if (key[DocChat.MSG_INSERT_DATE] == null) ""
                                    else Util.formatDate(key[DocChat.MSG_INSERT_DATE]!!, "yyyy-MM-dd", "yy.M.d")!!
                ))
            }
            if (docChangeList.size <= list.size) {
                onResponse(list)
            }
        }
    }

    fun sendMsg(context : Context, userId: String, msg : String, onResponse: ((responseCode: Int) -> Unit)) {
        val firestore = FirebaseFirestore.getInstance()

        val userRef: CollectionReference = firestore.collection(DocChat.CHAT) // 컬렉션명
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.KOREA).format(Date())
        val date2 = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
        val key: MutableMap<String, String> = HashMap()

        key[DocChat.ID] = UserData.id.toString()
        key[DocChat.NAME] = UserData.name.toString()
        key[DocChat.GENDER] = UserData.gender.toString()
        key[DocChat.BIRTH] = UserData.birth.toString()
        key[DocChat.MBTI] = UserData.mbti.toString()
        key[DocChat.PROFILE] = Util.getStr(UserData.profile.toString())
        key[DocChat.MESSAGE] = Util.getStr(msg)
        key[DocChat.MSG_INSERT_DATE] = date

        userRef.document(UserData.id.toString()).collection(userId).document(date + "_" + UserData.id.toString()).set(key)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userRef.document(userId).collection(UserData.id.toString()).document(date + "_" + UserData.id.toString()).set(key)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onResponse(ResponseCode.SUCCESS)

                            } else {
                                onResponse(ResponseCode.FAIL)
                            }
                        }

                } else {
                    onResponse(ResponseCode.FAIL)
                }
            }
    }
}