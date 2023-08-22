package com.pdsk.cocodroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pdsk.cocodroid.models.ProdukData
import com.pdsk.cocodroid.models.ProdukModel
import com.pdsk.cocodroid.models.TokoData
import com.pdsk.cocodroid.retrofit.ApiService
import com.pdsk.cocodroid.utils.showAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class TokoActivity:AppCompatActivity() {

    private lateinit var backButton:ImageView
    private lateinit var tokoHeaderNamaToko:TextView
    private lateinit var tokoHeaderDaerah:TextView
    private lateinit var produkItemContainer:LinearLayout
    private lateinit var contactButton:TextView
    private lateinit var getDataErrorTextView:TextView
    private lateinit var searchLoaderTextView:TextView
    private lateinit var searchLoaderContainer:LinearLayout
    private lateinit var getDataErrorContainer:LinearLayout
    private lateinit var getDataErrorRefreshBtn:TextView
    private lateinit var telpBtnImageView:ImageView
    private lateinit var pesanBtnImageView:ImageView
    private lateinit var lokasiBtnImageView:ImageView

    fun initComponents(){
        backButton = findViewById(R.id.backButtonImageView)
        tokoHeaderNamaToko = findViewById(R.id.tokoHeaderNamaToko)
        tokoHeaderDaerah = findViewById(R.id.tokoHeaderDaerah)
        produkItemContainer = findViewById(R.id.produkItemContainer)
        contactButton = findViewById(R.id.contactTextView)
        getDataErrorTextView = findViewById(R.id.getDatErrorMsg)
        searchLoaderTextView = findViewById(R.id.searchLoaderTextView)
        searchLoaderContainer = findViewById(R.id.searchLoaderContainer)
        getDataErrorContainer = findViewById(R.id.errorGetDataContainer)
        getDataErrorRefreshBtn = findViewById(R.id.refreshData)
        telpBtnImageView = findViewById(R.id.telpBtnImageView)
        pesanBtnImageView = findViewById(R.id.pesanBtnImageView)
        lokasiBtnImageView = findViewById(R.id.lokasiBtnImageView)
    }

    fun initComponentsAttribute(){
        getDataErrorTextView.text = resources.getString(R.string.data_produk_tidak_ditemukan)
        searchLoaderTextView.text = resources.getString(R.string.mendapatkan_data_produk)
    }

    fun initListener(){
        backButton.setOnClickListener{
            finish()
        }

        contactButton.setOnClickListener {
            startActivity(Intent(this,ContactActivity::class.java))
        }

        getDataErrorRefreshBtn.setOnClickListener {
            getProdukAndSetView()
        }

        telpBtnImageView.setOnClickListener {
            val tokoNoHp:String? = getTokoFromPrevActivity().no_hp

            if(tokoNoHp === null){
                showAlert(this,"Mohon maaf, nomor telepon toko tidak ditemukkan/terdaftar 😢")
            }else{
                val intent:Intent = Intent(Intent.ACTION_DIAL)

                intent.data = Uri.parse("tel:${tokoNoHp}")

                startActivity(intent)
            }
        }

        pesanBtnImageView.setOnClickListener {
            val tokoNoHp:String? = getTokoFromPrevActivity().no_hp

            if(tokoNoHp === null){
                showAlert(this,"Mohon maaf, nomor telepon toko tidak ditemukkan/terdaftar 😢")
            }else{
                val intent:Intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${tokoNoHp}"))

                startActivity(intent)
            }
        }

        lokasiBtnImageView.setOnClickListener {
            val lokasiToko:String? = getTokoFromPrevActivity().lokasi

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
        setContentView(R.layout.activity_toko)

        initComponents()
        initComponentsAttribute()
        initListener()

        setTokoHeaderView()

        getProdukAndSetView()
    }

    fun setTokoHeaderView(){
        val toko:TokoData = getTokoFromPrevActivity()
        tokoHeaderNamaToko.text = toko.nama_toko
        tokoHeaderDaerah.text = toko.daerah

        if(tokoHeaderNamaToko.text.length > 15) tokoHeaderNamaToko.textSize = 14.toFloat()
    }

    fun getTokoFromPrevActivity(): TokoData {
        val bundle:Bundle = intent.extras!!
        return TokoData(bundle.getInt("idToko"),bundle.getString("namaToko").toString(),bundle.getString("daerah").toString(),bundle.getString("lokasi"),bundle.getString("no_hp"))
    }

    @SuppressLint("SetTextI18n")
    fun createAndSetProdukItem(dataProduk:ProdukData){
        val produkItemView: RelativeLayout = layoutInflater.inflate(R.layout.produk_item,null) as RelativeLayout

        // SET VERTICAL MARGIN PER ITEM PRODUK
        val produkItemViewParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        produkItemViewParams.setMargins(0,0,0,(resources.displayMetrics.density * 25).toInt())
        produkItemView.layoutParams = produkItemViewParams;

        // SELECT VIEW PRODUK ITEM
        val produkItemNama: TextView = produkItemView.findViewById(R.id.produkItemNama)
        val produkItemHarga: TextView = produkItemView.findViewById(R.id.produkItemHarga)

        // SET VALUE TEXT DARI PRODUK ITEN
        produkItemNama.text = dataProduk.nama_produk
        produkItemHarga.text = "Rp. ${NumberFormat.getInstance(Locale("in","ID")).format(dataProduk.harga)}"

        // SET LISTENER ON CLICK UNTUK PRODUK ITEM
        val intent = Intent(this,ProdukActivity::class.java)
        val bundle = Bundle()
        val toko:TokoData = getTokoFromPrevActivity()

        bundle.putInt("idToko",toko.id)
        bundle.putString("namaToko",toko.nama_toko)
        bundle.putString("daerah",toko.daerah)
        bundle.putString("lokasi",toko.lokasi)
        bundle.putString("no_hp",toko.no_hp)
        bundle.putInt("idProduk",dataProduk.id)
        bundle.putString("namaProduk",dataProduk.nama_produk)
        bundle.putInt("hargaProduk",dataProduk.harga)
        bundle.putString("description",dataProduk.description)
        bundle.putString("manfaat",dataProduk.manfaat)

        intent.putExtras(bundle)

        produkItemView.setOnClickListener {
            startActivity(intent)
        }

        // TAMBAHKAN PRODUK ITEM KE VIEW CONTAINER/PEMBUNGKUS PRODUK ITEM
        produkItemContainer.addView(produkItemView)
    }

    fun getProdukAndSetView(){
        searchLoaderContainer.visibility = View.VISIBLE
        getDataErrorContainer.visibility = View.GONE

        val toko:TokoData = getTokoFromPrevActivity()
        ApiService.endpoint.getProdukByIdToko(toko.id).enqueue(object : Callback<ProdukModel>{
            override fun onResponse(call: Call<ProdukModel>, response: Response<ProdukModel>) {
                searchLoaderContainer.visibility = View.GONE

                if(response.isSuccessful){
                    val result:ProdukModel = response.body()!!

                    for(res in result.data){
                        createAndSetProdukItem(res)
                    }

                    /** MENGHILANGKAN LINE PEMISAH PADA PRODUK ITEM TERAKHIR
                    val lastProdukItem = produkItemContainer.getChildAt(produkItemContainer.childCount-1)
                    val lastProdukItemLineView = lastProdukItem.findViewById(R.id.produkItemLineView) as View
                    (lastProdukItemLineView.parent as LinearLayout).removeView(lastProdukItemLineView)
                    **/
                }
            }

            override fun onFailure(call: Call<ProdukModel>, t: Throwable) {
                searchLoaderContainer.visibility = View.GONE
                getDataErrorContainer.visibility = View.VISIBLE

                Log.d("PRODUKMODEL","Error: " + t.toString())
            }

        })
    }
}