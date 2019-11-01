package com.sungbin.sinch.test

import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.sinch.android.rtc.PushPair
import com.sinch.android.rtc.Sinch
import com.sinch.android.rtc.SinchClient
import com.sinch.android.rtc.calling.Call
import com.sinch.android.rtc.calling.CallClient
import com.sinch.android.rtc.calling.CallClientListener
import com.sinch.android.rtc.calling.CallListener
import com.sungbin.sinch.test.Sinch.APP_KEY
import com.sungbin.sinch.test.Sinch.APP_SECRET
import com.sungbin.sinch.test.Sinch.ENVIRONMENT

class CallActivity : AppCompatActivity() {

    private var call: Call? = null
    private var callState: TextView? = null
    private var sinchClient: SinchClient? = null
    private var button: Button? = null
    private var callerId: String? = null
    private var recipientId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.call)

        val intent = intent
        callerId = intent.getStringExtra("callerId")
        recipientId = intent.getStringExtra("recipientId")

        sinchClient = Sinch.getSinchClientBuilder()
            .context(this)
            .userId(callerId)
            .applicationKey(APP_KEY)
            .applicationSecret(APP_SECRET)
            .environmentHost(ENVIRONMENT)
            .build()

        sinchClient!!.setSupportCalling(true)
        sinchClient!!.startListeningOnActiveConnection()
        sinchClient!!.start()

        sinchClient!!.callClient.addCallClientListener(SinchCallClientListener())

        button = findViewById(R.id.button)
        callState = findViewById(R.id.callState)

        button!!.setOnClickListener {
            if (call == null) {
                if(!sinchClient!!.isStarted) sinchClient!!.start()
                if(sinchClient!!.isStarted) {
                    call = sinchClient!!.callClient.callUser(recipientId)
                    call!!.addCallListener(SinchCallListener())
                    button!!.text = "전화 끊기"
                }
            } else {
                call!!.hangup()
            }
        }
    }

    private inner class SinchCallListener : CallListener {
        override fun onCallEnded(endedCall: Call) {
            call = null
            button!!.text = "전화"
            callState!!.text = ""
            volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
        }

        override fun onCallEstablished(establishedCall: Call) {
            callState!!.text = "연결됨"
            volumeControlStream = AudioManager.STREAM_VOICE_CALL
        }

        override fun onCallProgressing(progressingCall: Call) {
            callState!!.text = "전화 수신중..."
        }

        override fun onShouldSendPushNotification(call: Call, pushPairs: List<PushPair>) {}
    }

    private inner class SinchCallClientListener : CallClientListener {
        override fun onIncomingCall(callClient: CallClient, incomingCall: Call) {
            call = incomingCall
            Toast.makeText(this@CallActivity, "전화가 걸려옵니다.", Toast.LENGTH_SHORT).show()
            call!!.answer()
            call!!.addCallListener(SinchCallListener())
            button!!.text = "전화 끊기"
        }
    }
}
