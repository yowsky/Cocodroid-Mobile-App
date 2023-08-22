package com.pdsk.cocodroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var startedButton:TextView
    private lateinit var contactTextView:TextView

    private fun initComponents(){
        startedButton = findViewById(R.id.startedTextView)
        contactTextView = findViewById(R.id.contactTextView)
    }

    private fun initListener(){
        startedButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        contactTextView.setOnClickListener{
            startActivity(Intent(this,ContactActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()
        initListener()
    }

}