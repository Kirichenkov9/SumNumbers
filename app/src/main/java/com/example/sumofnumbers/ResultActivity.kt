package com.example.sumofnumbers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : AppCompatActivity() {

    companion object {
        const val ACTION_INTENTSERVICE = "ACTION_MYINTENTSERVICE"
        const val EXTRA_KEY_FIRST = "firstNumber"
        const val EXTRA_KEY_SECOND = "secondNumber"
        const val EXTRA_KEY_RESULT = "result"
    }

    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val result = intent.getStringExtra(EXTRA_KEY_RESULT)
            subject.onNext(result.orEmpty())
        }
    }

    private val subject: PublishSubject<String?> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        progress_bar.visibility = View.VISIBLE
        title_txt.visibility = View.GONE

        if (savedInstanceState == null) {
            val service = Intent(this, IntentService::class.java)

            service.putExtra(EXTRA_KEY_FIRST, intent?.getDoubleExtra(EXTRA_KEY_FIRST, 0.0))
            service.putExtra(EXTRA_KEY_SECOND, intent?.getDoubleExtra(EXTRA_KEY_SECOND, 0.0))

            startService(service)
        }

        subject.observeOn(AndroidSchedulers.mainThread())
            .subscribe { answer ->
                if (answer.orEmpty().isNotEmpty()) {
                    result_txt.text = answer
                    progress_bar.visibility = View.GONE
                    title_txt.visibility = View.VISIBLE
                }
            }

        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(receiver, IntentFilter(ACTION_INTENTSERVICE))

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        LocalBroadcastManager
            .getInstance(this)
            .unregisterReceiver(receiver)


        stopService(Intent(this, IntentService::class.java))
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_KEY_RESULT, result_txt?.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val result = savedInstanceState.getString(EXTRA_KEY_RESULT)
        subject.onNext(result.orEmpty())
    }
}
