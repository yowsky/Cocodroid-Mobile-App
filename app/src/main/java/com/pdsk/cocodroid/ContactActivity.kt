package com.pdsk.cocodroid

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pdsk.cocodroid.models.AnggotaKelompok

class ContactActivity : AppCompatActivity() {

    private lateinit var anggotaKelompokContainer:LinearLayout;
    private lateinit var backButtonImageView: ImageView

    fun initComponents(){
        anggotaKelompokContainer = findViewById(R.id.namaAnggotaContainer)
        backButtonImageView = findViewById(R.id.backButtonImageView)
    }

    fun initListener(){
        backButtonImageView.setOnClickListener{
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        initComponents()
        initListener()

        addAnggotaKelompokTextView()
    }

    fun createAnggotaKelompokTextView(text:String):TextView{
        val textView:TextView = TextView(this)
        textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        textView.text = text
        textView.textSize = 16.toFloat()
        textView.setTextColor(resources.getColor(R.color.white,theme))
        textView.typeface = resources.getFont(R.font.montserrat_regular)

        return textView
    }

    fun addAnggotaKelompokTextView(){
        for(anggota in AnggotaKelompok.namaAnggota){
            anggotaKelompokContainer.addView(createAnggotaKelompokTextView(anggota))
        }
    }
}