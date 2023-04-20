package com.lonewolf.pasco.notification

import android.app.Activity
import android.util.Log
import com.lonewolf.pasco.resources.Storage
import com.google.firebase.database.*
import com.lonewolf.pasco.resources.ShortCut_To
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Send {

    private lateinit var databaseReference : DatabaseReference
    private lateinit var storage : Storage

    fun notification(userId : String, header: String, message:String, tokenId : String, activity:Activity, ind: Int){
        databaseReference = FirebaseDatabase.getInstance().reference
        storage = Storage(activity)
        PushNotification(

            NotificationData(
                header, message,),
            tokenId
        ).also {

            sendNotification(it, header, message, ind, userId)
        }
    }

    private fun sendNotification(notification: PushNotification, header:String, message : String, ind : Int, userId: String) = CoroutineScope(
        Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotificattion(notification)
            if(response.isSuccessful){
                databaseReference.child("Identifiers").child("Notification_Id").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val numId = (Integer.parseInt(snapshot.value.toString())+1+ind).toString()
                        val hashMap = HashMap<String, String>()
                        hashMap.put("header", header)
                        hashMap.put("message", message)
                        hashMap.put("message_id", numId)
                        hashMap.put("created", ShortCut_To.currentDatewithTime)
                        databaseReference.child("Notifications").child(userId).child(numId).setValue(hashMap).addOnSuccessListener {
                            databaseReference.child("Identifiers").child("Notification_Id").setValue(numId)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

            }else{
                Log.e("Chat", response.errorBody().toString())
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}