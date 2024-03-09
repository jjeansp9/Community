package com.jspstudio.community.firebase

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

object StorageMgr {

    private const val TAG = "StorageMgr"

    fun sendImg(context: Context, uri: Uri){

        val firebaseStorage = FirebaseStorage.getInstance() // storage 객체생성
        val profileRef: StorageReference = firebaseStorage.getReference("testImg/${uri.toString()}")

        profileRef.putFile(uri).addOnSuccessListener {
            profileRef.downloadUrl.addOnSuccessListener {

            }
        }
    }
}