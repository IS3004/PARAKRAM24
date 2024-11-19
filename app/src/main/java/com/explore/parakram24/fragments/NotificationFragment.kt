package com.explore.parakram24.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.explore.parakram24.NotificationData
import com.explore.parakram24.databinding.FragmentNotificationBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject
import java.util.Date


class NotificationFragment : Fragment() {

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAGdHrXl4:APA91bEnpzmsRtwdhxFZY5rIUkyS8p6uZ5UHQfNn4Fdhgnu9CJ9jMMtsw_jMMB9_CaAya1JHD46IZQ2BhS_LsezF41IGmdPODA-b8KVaaZuuYbHtlib9bMSo7NGOnRTWV2DIWTJGFZUO"
    private val contentType = "application/json"
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseMessaging.getInstance().subscribeToTopic("/topics/parakram")

        binding.btnSend.setOnClickListener {
            val body = binding.notificationBody.text ?: ""
            val title = binding.notificationTitle.text ?: ""
            if (body != "" && title != "") {
                val topic = "/topics/parakram" //topic has to match what the receiver subscribed to

                val notification = JSONObject()
                val notificationBody = JSONObject()

                try {
                    notificationBody.put("title", title)
                    notificationBody.put("body", body)   //Enter your notification message
                    notification.put("to", topic)
                    notification.put("data", notificationBody)
                    Log.e("TAG", notification.toString())
                } catch (e: JSONException) {
                    Log.e("TAG", "onCreate: " + e.message)
                }
                Firebase.database.reference.child("notifications").child(Date().toString().substring(4))
                    .setValue(
                        NotificationData(title = title.toString(), body = body.toString())
                    )
                sendNotification(notification)
            }
        }

    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")

            },
            Response.ErrorListener {
                //Toast.makeText(this@MainActivity, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)

    }

}