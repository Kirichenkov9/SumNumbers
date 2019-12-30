package com.example.sumofnumbers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculate_btn.setOnClickListener {
            if (checkFields()) {
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra(
                        ResultActivity.EXTRA_KEY_FIRST,
                        first_number.text.toString().toDouble()

                    )
                    putExtra(
                        ResultActivity.EXTRA_KEY_SECOND,
                        second_number.text.toString().toDouble()
                    )
                }
                startActivity(intent)
            }
        }
    }

    private fun checkFields(): Boolean {
        var cancel = true

        if (first_number.text.isNullOrBlank()) {
            first_number.error = getString(R.string.error_empty_field)
            first_number.requestFocus()
            cancel = false
        }

        if (second_number.text.isNullOrBlank()) {
            second_number.error = getString(R.string.error_empty_field)
            second_number.requestFocus()
            cancel = false
        }

        if (first_number.text.toString().toDoubleOrNull() == null) {
            first_number.error = getString(R.string.error_number_format)
            first_number.requestFocus()
            cancel = false
        }

        if (second_number.text.toString().toDoubleOrNull() == null) {
            second_number.error = getString(R.string.error_number_format)
            second_number.requestFocus()
            cancel = false
        }

        return cancel
    }
}
