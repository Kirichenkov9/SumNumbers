package com.example.sumofnumbers

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.sumofnumbers.ResultActivity.Companion.ACTION_INTENTSERVICE
import com.example.sumofnumbers.ResultActivity.Companion.EXTRA_KEY_FIRST
import com.example.sumofnumbers.ResultActivity.Companion.EXTRA_KEY_RESULT
import com.example.sumofnumbers.ResultActivity.Companion.EXTRA_KEY_SECOND

class IntentService : IntentService("service") {

    override fun onHandleIntent(intent: Intent?) {

        intent?.let {
            val sum = sum(
                it.getDoubleExtra(EXTRA_KEY_FIRST, 0.0),
                it.getDoubleExtra(EXTRA_KEY_SECOND, 0.0)
            )

            val responseIntent = Intent()
            responseIntent.action = ACTION_INTENTSERVICE
            responseIntent.putExtra(EXTRA_KEY_RESULT, sum.toString())

            LocalBroadcastManager
                .getInstance(this)
                .sendBroadcast(responseIntent.setAction(ACTION_INTENTSERVICE))

        }
    }

    private fun sum(number1: Double, number2: Double) = number1 + number2
}
