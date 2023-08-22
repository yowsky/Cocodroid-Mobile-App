package com.pdsk.cocodroid

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.pdsk.cocodroid.models.ProdukData
import com.pdsk.cocodroid.models.TokoData
import com.pdsk.cocodroid.utils.showAlert
import java.text.NumberFormat
import java.util.*

class ProdukActivity : AppCompatActivity() {

    private lateinit var produkNamaTextView:TextView
    private lateinit var produkHargaTextView:TextView
    private lateinit var produkDescriptionTextView:TextView
    private lateinit var produkManfaatTextView:TextView
    private lateinit var tokoNamaTextView: TextView
    private lateinit var tokoDaerahTextView: TextView
    private lateinit var backButton:ImageView
    private lateinit var contactButton:TextView
    private lateinit var telpBtnImageView:ImageView
    private lateinit var pesanBtnImageView:ImageView
    private lateinit var lokasiBtnImageView:ImageView

    private fun initComponents(){
        produkNamaTextView = findViewById(R.id.produkNama)
        produkHargaTextView = findViewById(R.id.produkHarga)
        produkDescriptionTextView = findViewById(R.id.produkDescription)
        produkManfaatTextView = findViewById(R.id.produkManfaat)
        tokoNamaTextView = findViewById(R.id.tokoHeaderNamaToko)
        tokoDaerahTextView = findViewById(R.id.tokoHeaderDaerah)
        backButton = findViewById(R.id.backButtonImageView)
        contactButton = findViewById(R.id.contactTextView)
        telpBtnImageView = findViewById(R.id.telpBtnImageView)
        pesanBtnImageView = findViewById(R.id.pesanBtnImageView)
        lokasiBtnImageView = findViewById(R.id.lokasiBtnImageView)
    }

    @SuppressLint("SetTextI18n")
    private fun initComponentsAttribute(){
        val dataToko = getTokoFromPrevActivity()!!
        val dataProduk = getProdukFromPrevActivity()!!

        tokoNamaTextView.text = dataToko.nama_toko
        if(tokoNamaTextView.text.length > 15) tokoNamaTextView.textSize = 14.toFloat()
        tokoDaerahTextView.text = dataToko.daerah

        produkNamaTextView.text = dataProduk.nama_produk
        produkHargaTextView.text ="Rp. " + NumberFormat.getInstance(Locale("in","ID")).format(dataProduk.harga)
        produkDescriptionTextView.text = if(dataProduk.description == null) "Deskripsi Produk:\n-" else "Deskripsi Produk:\n"+dataProduk.description
        produkManfaatTextView.text = if(dataProduk.manfaat == null) "Manfaat:\n-" else "Manfaat:\n"+dataProduk.manfaat
    }

    private fun initListener(){
        backButton.setOnClickListener {
            finish()
        }
        contactButton.setOnClickListener{
            startActivity(Intent(this,ContactActivity::class.java))
        }

        telpBtnImageView.setOnClickListener {
            val tokoNoHp:String? = getTokoFromPrevActivity()!!.no_hp

            if(tokoNoHp === null){
                showAlert(this,"Mohon maaf, nomor telepon toko tidak ditemukkan/terdaftar 😢")
            }else{
                val intent:Intent = Intent(Intent.ACTION_DIAL)

                intent.data = Uri.parse("tel:${tokoNoHp}")

                startActivity(intent)
            }
        }

        pesanBtnImageView.setOnClickListener {
            val tokoNoHp:String? = getTokoFromPrevActivity()!!.no_hp

            if(tokoNoHp === null){
                showAlert(this,"Mohon maaf, nomor telepon toko tidak ditemukkan/terdaftar 😢")
            }else{
                val intent:Intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${tokoNoHp}"))

                startActivity(intent)
            }
        }

        lokasiBtnImageView.setOnClickListener {
            val lokasiToko:String? = getTokoFromPrevActivity()!!.lokasi

            if(lokasiToko === null){
                showAlert(this,"Mohon maaf, lokasi toko tidak ditemukkan 😢")
            }else{
                val intent:Intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${lokasiToko}"))
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk)

        initComponents()
        initComponentsAttribute()
        initListener()
    }

    fun getTokoFromPrevActivity():TokoData?{
        val b:Bundle? = intent.extras
        if(b != null){
            return TokoData(b.getInt("idToko"),b.getString("namaToko")!!,b.getString("daerah")!!,b.getString("lokasi"),b.getString("no_hp"))
        }
        return null
    }

    fun getProdukFromPrevActivity():ProdukData?{
        val b:Bundle? = intent.extras
        if(b != null){
            return ProdukData(b.getInt("idProduk"),b.getInt("idToko"),b.getString("namaProduk")!!,b.getInt("hargaProduk"),b.getString("description"),b.getString("manfaat"))
        }
        return null
    }


}