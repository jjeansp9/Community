package com.jspstudio.community.firebase.board.accompany

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jspstudio.community.R
import com.jspstudio.community.firebase.board.accompany.field.DocAccompany
import com.jspstudio.community.firebase.chat.field.DocChat
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.model.UserData
import com.jspstudio.community.network.ResponseCode
import com.jspstudio.community.user.MyData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FireStoreAccompany {
    private const val TAG = "FireStoreAccompany"

    fun getAccompany(context: Context, onResponse: ((MutableList<AccompanyData>) -> Unit)) {

        val list : MutableList<AccompanyData> = mutableListOf()

        val firestore = FirebaseFirestore.getInstance()
        val userRef: CollectionReference = firestore.collection(DocAccompany.ACCOMPANY) // 컬렉션명
        userRef.addSnapshotListener { value, error ->
            val docChangeList : List<DocumentChange> = value!!.documentChanges
            for (documentChange : DocumentChange in docChangeList) {

                val snapshot : DocumentSnapshot = documentChange.document
                val key : Map<String, String> = snapshot.data as Map<String, String>

                LogMgr.e(TAG, snapshot.data.toString())
                val getItem = UserData(
                    id = key[DocChat.ID]!!,
                    name = key[DocChat.NAME]!!,
                    gender = key[DocChat.GENDER]!!,
                    birth = Util.calculateAgeFromYearOfBirth(key[DocChat.BIRTH]!!) + context.getString(R.string.age_sub),
                    mbti = key[DocChat.MBTI]!!,
                    profile = key[DocChat.PROFILE]!!,
                    aboutMe = if (key[DocChat.ABOUT_ME] == null) ""
                              else key[DocChat.ABOUT_ME]!!
                )
                list.add(AccompanyData(
                    user = getItem,
                    title = if (key[DocAccompany.TITLE] == null) ""
                            else key[DocAccompany.TITLE]!!,
                    content = if (key[DocAccompany.CONTENT] == null) ""
                              else key[DocAccompany.CONTENT]!!,
                    startDate = if (key[DocAccompany.START_DATE] == null) ""
                                else Util.formatDate(key[DocAccompany.START_DATE]!!, "yyyy-MM-dd", "yy.M.d")!!,
                    endDate = if (key[DocAccompany.END_DATE] == null) ""
                              else Util.formatDate(key[DocAccompany.END_DATE]!!, "yyyy-MM-dd", "yy.M.d")!!,
                    insertDate = key[DocAccompany.INSERT_DATE]!!,
                ))
            }
            if (docChangeList.size <= list.size) {
                onResponse(list)
            }
        }
    }

    fun addAccompany(context : Context, item : AccompanyData, onResponse: ((responseCode: Int) -> Unit)) {
        val firestore = FirebaseFirestore.getInstance()

        val userRef: CollectionReference = firestore.collection(DocAccompany.ACCOMPANY) // 컬렉션명
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.KOREA).format(Date())
        val key: MutableMap<String, String> = HashMap()

        key[DocAccompany.ID] = MyData.id.toString()
        key[DocAccompany.NAME] = MyData.name.toString()
        key[DocAccompany.GENDER] = MyData.gender.toString()
        key[DocAccompany.BIRTH] = MyData.birth.toString()
        key[DocAccompany.MBTI] = MyData.mbti.toString()
        key[DocAccompany.PROFILE] = Util.getStr(MyData.profile.toString())
        key[DocAccompany.TITLE] = Util.getStr(item.title)
        key[DocAccompany.CONTENT] = Util.getStr(item.content)
        key[DocAccompany.START_DATE] = Util.getStr(item.startDate)
        key[DocAccompany.END_DATE] = Util.getStr(item.endDate)
        key[DocAccompany.INSERT_DATE] = date

        userRef.document(date + "_" + MyData.id.toString()).set(key)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResponse(ResponseCode.SUCCESS)

                } else {
                    onResponse(ResponseCode.FAIL)
                }
            }
    }
}