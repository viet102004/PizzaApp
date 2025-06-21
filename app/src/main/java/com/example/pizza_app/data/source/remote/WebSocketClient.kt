package com.example.pizza_app.data.source.remote

import android.util.Log
import okhttp3.*
import org.json.JSONObject

object WebSocketClient {

    private lateinit var webSocket: WebSocket
    private val client = OkHttpClient()

    fun connect(onUpdate: (String) -> Unit) {
        val request = Request.Builder()
            .url("ws://https://related-burro-selected.ngrok-free.app/ws/update") // thay bằng IP thật
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val json = JSONObject(text)
                val entity = json.getString("entity")
                if (json.getString("action") == "update") {
                    onUpdate(entity)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Lỗi: ${t.message}")
            }
        })
    }
}
