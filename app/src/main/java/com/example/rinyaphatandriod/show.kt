package com.example.rinyaphatandriod

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class show : AppCompatActivity() {

    private lateinit var brand: TextView
    private lateinit var name: TextView
    private lateinit var serial: TextView
    private lateinit var amount: TextView
    private lateinit var price: TextView
    private lateinit var cpu: TextView
    private lateinit var ram: TextView
    private lateinit var rom: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_show)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nextbutton = findViewById<Button>(R.id.nextbutton)
        nextbutton.setOnClickListener {
            val intent = Intent(this, search::class.java)
            startActivity(intent)
        }
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        brand = findViewById<TextView>(R.id.brand)
        name = findViewById<TextView>(R.id.name)
        serial = findViewById<TextView>(R.id.serial)
        amount = findViewById<TextView>(R.id.amount)
        price = findViewById<TextView>(R.id.price)
        cpu = findViewById<TextView>(R.id.cpu)
        ram = findViewById<TextView>(R.id.ram)
        rom = findViewById<TextView>(R.id.rom)


        val product = intent.getStringExtra("PRODUCT") ?: ""
        if (product.isNotEmpty()) {
            searchProduct(product)
        }
    }

    private fun setImageView(imageURL: String) {
        imageView = findViewById(R.id.imageView)

        val url = "http://10.13.4.3:3000$imageURL"
        // Load image using Glide
        Glide.with(this)
            .load(url)
            .into(imageView)
    }

    private fun searchProduct(product: String) {
        val url = "http://10.13.4.3:3000/product/$product"
        Log.d("tag", url)
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = okHttpClient.newCall(request).execute()
        Log.d("tag", "x1")
        if (response.isSuccessful) {
            val data = JSONObject(response.body!!.string())
            Log.d("tag", "x2")
            if (data.length() > 0){
                Log.d("tag", "x3")
                val brands = data.getString("brandproduct")
                val names = data.getString("nameproduct")
                val serials = data.getString("serialnumber")
                val amounts = data.getString("amount")
                val prices = data.getString("price")
                val cpus = data.getString("cpu")
                val rams = data.getString("ram")
                val roms = data.getString("rom")
                val imageURL = data.getString("imageFile")

                brand.text = brands
                name.text = names
                serial.text = serials
                amount.text = amounts
                price.text = prices
                cpu.text = cpus
                ram.text = rams
                rom.text = roms
                setImageView(imageURL)
                Log.d("tag", "x4")
            }
        }
    }
}