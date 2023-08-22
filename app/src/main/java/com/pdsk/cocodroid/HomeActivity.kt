package com.pdsk.cocodroid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pdsk.cocodroid.models.TokoData
import com.pdsk.cocodroid.models.TokoModel
import com.pdsk.cocodroid.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class HomeActivity : AppCompatActivity() {

    private lateinit var listTokoLinearLayout:LinearLayout
    private lateinit var contactTextView:TextView
    private lateinit var errorGetTokoContainer:RelativeLayout
    private lateinit var searchLoader:RelativeLayout
    private lateinit var refreshTokoButton:TextView
    private lateinit var searchEditText:EditText
    private lateinit var getDataErrTextView: TextView
    private lateinit var searchLoaderTextView: TextView

    private fun initComponents(){
        listTokoLinearLayout = findViewById(R.id.listTokoLinearLayout)
        contactTextView = findViewById(R.id.contactTextView)
        errorGetTokoContainer = findViewById(R.id.errorGetToko)
        searchLoader = findViewById(R.id.loaderSearch)
        refreshTokoButton = findViewById(R.id.refreshData)
        searchEditText = findViewById(R.id.searchEditText)
        getDataErrTextView = findViewById(R.id.getDatErrorMsg)
        searchLoaderTextView = findViewById(R.id.searchLoaderTextView)
    }

    private fun initComponentsAttribute(){
        getDataErrTextView.text = resources.getString(R.string.data_toko_tidak_ditemukan)
        searchLoaderTextView.text = resources.getString(R.string.mendapatkan_data_toko)
    }

    private fun initListener(){
        contactTextView.setOnClickListener {
            startActivity(Intent(this,ContactActivity::class.java))
        }

        refreshTokoButton.setOnClickListener {
            getTokoAndSetView()
        }

        searchEditText.addTextChangedListener(object:TextWatcher{
            var isSearchToko:Boolean = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 1){
                    searchTokoAndSetView(s.toString())
                    isSearchToko = true
                }else if(count <= 1 && isSearchToko){
                    getTokoAndSetView()
                    isSearchToko = false
                }
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initComponents()
        initComponentsAttribute()
        initListener()

        // RETROFIT GET DATA TOKO
        getTokoAndSetView()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun createAndAddListToko(dataId:Int, namaToko:String,daerah:String,lokasi:String?,no_hp:String?){

        // CREATE AND CONFIGURE RELATIVE LAYOUT
        val relativeLayout = RelativeLayout(this)
        relativeLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)

        var relativeLayoutVerticalPad:Int = (8 * resources.displayMetrics.density).roundToInt()
        relativeLayout.setPadding(0,relativeLayoutVerticalPad,0,relativeLayoutVerticalPad)

        // CREATE AND CONFIGURE IMAGE VIEW
        val imageView = ImageView(this)
        imageView.id = View.generateViewId()
        imageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        imageView.setImageDrawable(resources.getDrawable(R.drawable.home_icon2,theme))
        imageView.maxWidth = (50 * resources.displayMetrics.density).roundToInt()
        imageView.adjustViewBounds = true

        // CREATE AND CONFIGURE LINEAR LAYOUT
        val linearLayout = LinearLayout(this)
        val linearLayoutParams:RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
        linearLayoutParams.addRule(RelativeLayout.END_OF,imageView.id)
        linearLayout.layoutParams = linearLayoutParams
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setPadding((10*resources.displayMetrics.density).roundToInt(),0,0,0)

        // ADD TEXT VIEW UNTUK NAMA TOKO DAN DAERAH KE LINEAR LAYOUT
        linearLayout.addView(listTokoTextView(namaToko,16.toFloat(),"bold"))
        linearLayout.addView(listTokoTextView(daerah,14.toFloat(),"regular"))

        // ADD IMAGE VIEW, LINEAR LAYOUT, RELATIVE LAYOUT KEDALAM CONTAINER listTokoLinearLayout
        relativeLayout.addView(imageView)
        relativeLayout.addView(linearLayout)

        // SET ON CLICK LISTENER
        val intent:Intent = Intent(this,TokoActivity::class.java)
        val bundle:Bundle = Bundle()
        bundle.putInt("idToko",dataId)
        bundle.putString("namaToko",namaToko)
        bundle.putString("daerah",daerah)
        bundle.putString("lokasi",lokasi)
        bundle.putString("no_hp",no_hp)
        intent.putExtras(bundle)

        relativeLayout.setOnClickListener {
            startActivity(intent)
        }

        listTokoLinearLayout.addView(relativeLayout)
    }

    fun listTokoTextView(text:String,size:Float,fontFamily:String):TextView{
        val textView = TextView(this)
        textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        textView.setTextColor(resources.getColor(R.color.white,theme))
        textView.text = text
        textView.textSize = size

        when(fontFamily){
            "regular" -> textView.typeface = resources.getFont(R.font.montserrat_regular)
            "bold" -> textView.typeface = resources.getFont(R.font.montserrat_bold)
            else -> textView.typeface = resources.getFont(R.font.montserrat_regular)
        }

        return textView
    }

    fun getTokoAndSetView(){
        searchLoader.visibility = View.VISIBLE
        errorGetTokoContainer.visibility = View.GONE
        listTokoLinearLayout.removeAllViews()

        ApiService.endpoint.getData().enqueue(object : Callback<TokoModel>{
            override fun onResponse(
                call: Call<TokoModel>,
                response: Response<TokoModel>
            ) {
                listTokoLinearLayout.removeAllViews()
                searchLoader.visibility = View.GONE
                if(response.isSuccessful){

                    val result: TokoModel = response.body()!!
                    val dataToko:ArrayList<TokoData> = result.data

                    // LOOPING UNTUK MEMBUAT DAN MENAMBAHKAN LIST TOKO
                    for(data in dataToko){
                        createAndAddListToko(data.id,data.nama_toko,data.daerah,data.lokasi,data.no_hp)
                    }
                }
            }

            override fun onFailure(call: Call<TokoModel>, t: Throwable) {
                searchLoader.visibility = View.GONE
                Log.d("TOKOMODEL","Error: " + t.toString())

                errorGetTokoContainer.visibility = View.VISIBLE
            }

        })
    }

    fun searchTokoAndSetView(query:String){
        searchLoader.visibility = View.VISIBLE
        errorGetTokoContainer.visibility = View.GONE
        listTokoLinearLayout.removeAllViews()

        ApiService.endpoint.searchData(query).enqueue(object: Callback<TokoModel>{
            override fun onResponse(call: Call<TokoModel>, response: Response<TokoModel>) {
                listTokoLinearLayout.removeAllViews()
                searchLoader.visibility = View.GONE

                if(response.isSuccessful){

                    val result: TokoModel = response.body()!!
                    for(res in result.data){
                        createAndAddListToko(res.id,res.nama_toko,res.daerah,res.lokasi,res.no_hp)
                    }

                }
            }

            override fun onFailure(call: Call<TokoModel>, t: Throwable) {
                Log.d("TOKOMODEL","Error: ${t.toString()}")

                searchLoader.visibility = View.GONE
                errorGetTokoContainer.visibility = View.VISIBLE
            }
        })
    }
}