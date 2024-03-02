package com.jspstudio.community.firebase.chat

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jspstudio.community.R
import com.jspstudio.community.firebase.chat.field.DocChat
import com.jspstudio.community.model.MessageData
import com.jspstudio.community.model.UserData
import com.jspstudio.community.user.MyData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.Util
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object FireStoreChat {
    private const val TAG = "FireStoreChat"

    fun getMsgList(context: Context, onResponse: ((MutableList<MessageData>) -> Unit)) {
        val list : MutableList<MessageData> = mutableListOf()
        val firestore = FirebaseFirestore.getInstance()
        val userRef: CollectionReference = firestore.collection(DocChat.CHAT).document(MyData.id.toString()).collection(DocChat.MSG_LIST) // 컬렉션명

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
                    birth = Util.calculateAgeFromYearOfBirth(key[DocChat.BIRTH]!!.replace("[^\\d]".toRegex(), "")) + context.getString(R.string.age_sub),
                    mbti = key[DocChat.MBTI]!!,
                    profile = key[DocChat.PROFILE]!!,
                    aboutMe = key[DocChat.ABOUT_ME] ?: ""
                )
                var getDate = if (key[DocChat.MSG_INSERT_DATE] == null) ""
                              else key[DocChat.MSG_INSERT_DATE]!!

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
                val targetDate = dateFormat.parse(getDate)

                val calendarToday = Calendar.getInstance()
                val calendarTarget = Calendar.getInstance().apply { time = targetDate!! }

                // 오늘 날짜에서 시간, 분, 초를 제거
                calendarToday.set(Calendar.HOUR_OF_DAY, 0)
                calendarToday.set(Calendar.MINUTE, 0)
                calendarToday.set(Calendar.SECOND, 0)
                calendarToday.set(Calendar.MILLISECOND, 0)

                // 날짜 차이 계산
                val diff = calendarToday.timeInMillis - calendarTarget.timeInMillis
                val daysDiff = (diff / (24 * 60 * 60 * 1000)).toInt()

                when (daysDiff) {
                    0 -> {
                        //"오늘"
                        getDate = Util.formatDate(getDate, "yyyy-MM-dd HH:mm:ss.SSS", "a h:mm")!!
                    }
                    1 -> {
                        //"어제"
                        getDate = context.getString(R.string.yesterday)
                    }
                    2 -> {
                        // SimpleDateFormat을 사용하여 날짜 파싱
                        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
                        val date = originalFormat.parse(getDate)

                        val monthDayFormat = SimpleDateFormat("M d", Locale.getDefault())
                        val monthDay = monthDayFormat.format(date).split(" ")
                        val month = monthDay[0]
                        val day = monthDay[1]

                        getDate = context.getString(R.string.year_month, month, day)
                    }
                }

                val messageData = MessageData(
                    user = getItem,
                    message = key[DocChat.MESSAGE]!!,
                    msgInsertDate = getDate
                )

                // 리스트 내 id가 일치하는 요소의 인덱스 찾기
                val index = list.indexOfFirst { it.user.id == getItem.id }

                if (index != -1) {
                    // id가 일치하는 요소가 존재하면 업데이트
                    list[index] = messageData
                } else {
                    // 존재하지 않으면 추가
                    list.add(messageData)
                }
            }
            if (docChangeList.size <= list.size) {
                onResponse(list)
            }
        }
    }

    fun getMsg(context: Context, user: UserData, onResponse: ((MutableList<MessageData>) -> Unit)) {
        val list : MutableList<MessageData> = mutableListOf()

        val firestore = FirebaseFirestore.getInstance()
        val userRef: CollectionReference = firestore.collection(DocChat.CHAT).document(MyData.id.toString()).collection(user.id!!) // 컬렉션명
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
                    birth = Util.calculateAgeFromYearOfBirth(key[DocChat.BIRTH]!!.replace("[^\\d]".toRegex(), "")) + context.getString(R.string.age_sub),
                    mbti = key[DocChat.MBTI]!!,
                    profile = key[DocChat.PROFILE]!!,
                    aboutMe = if (key[DocChat.ABOUT_ME] == null) ""
                              else key[DocChat.ABOUT_ME]!!
                )
                list.add(MessageData(
                    user = getItem,
                    message = key[DocChat.MESSAGE]!!,
                    msgInsertDate = if (key[DocChat.MSG_INSERT_DATE] == null) ""
                                    else Util.formatDate(key[DocChat.MSG_INSERT_DATE]!!, "yyyy-MM-dd HH:mm:ss.SSS", "a h:mm")!!
                ))
            }
            if (docChangeList.size <= list.size) {
                onResponse(list)
            }
        }
    }

    fun sendMsg(context : Context, user: UserData, msg : String) {
        val firestore = FirebaseFirestore.getInstance()

        val userRef: CollectionReference = firestore.collection(DocChat.CHAT) // 컬렉션명
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.KOREA).format(Date())
        val date2 = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
        val key: MutableMap<String, String> = HashMap()
        val userKey: MutableMap<String, String> = HashMap()

        key[DocChat.ID] = MyData.id.toString()
        key[DocChat.NAME] = MyData.name.toString()
        key[DocChat.GENDER] = MyData.gender.toString()
        key[DocChat.BIRTH] = MyData.birth.toString()
        key[DocChat.MBTI] = MyData.mbti.toString()
        key[DocChat.PROFILE] = Util.getStr(MyData.profile.toString())
        key[DocChat.MESSAGE] = Util.getStr(msg)
        key[DocChat.MSG_INSERT_DATE] = date

        userKey[DocChat.ID] = user.id
        userKey[DocChat.NAME] = user.name
        userKey[DocChat.GENDER] = user.gender
        userKey[DocChat.BIRTH] = user.birth
        userKey[DocChat.MBTI] = user.mbti
        userKey[DocChat.PROFILE] = Util.getStr(user.profile)
        userKey[DocChat.MESSAGE] = Util.getStr(msg)
        userKey[DocChat.MSG_INSERT_DATE] = date

        userRef.document(MyData.id.toString()).collection(DocChat.MSG_LIST).document(user.id).set(userKey)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userRef.document(user.id).collection(DocChat.MSG_LIST).document(MyData.id.toString()).set(key)
                }
            }

        userRef.document(MyData.id.toString()).collection(user.id).document(date + "_" + MyData.id.toString()).set(key)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userRef.document(user.id).collection(MyData.id.toString()).document(date + "_" + MyData.id.toString()).set(key)
                }
            }
    }
}