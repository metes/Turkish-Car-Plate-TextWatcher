package com.test.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.turkishcarplatetextwatcher.PlateTextWatcher

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.myEditText)
        val button = findViewById<Button>(R.id.myButton)

        val carPlateWatcher = PlateTextWatcher(editText)

        button.setOnClickListener {
            val validationResult = if (carPlateWatcher.isPlateValid) {
                "It is valid"
            } else {
                "It is not valid"
            }
            Toast.makeText(this, validationResult, Toast.LENGTH_SHORT).show()
        }

    }
}