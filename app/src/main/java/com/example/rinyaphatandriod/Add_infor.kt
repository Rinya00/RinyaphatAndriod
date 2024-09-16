package com.example.rinyaphatandriod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File


class Add_infor : AppCompatActivity() {
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_infor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val nextbutton = findViewById<Button>(R.id.nextbutton)
        val backbutton = findViewById<Button>(R.id.backbutton)
        val editbrand = findViewById<EditText>(R.id.editbrand)
        val editname = findViewById<EditText>(R.id.editname)
        val editserial = findViewById<EditText>(R.id.editserial)
        val editamount = findViewById<EditText>(R.id.editamount)
        val editprice = findViewById<EditText>(R.id.editprice)
        val editcpu = findViewById<EditText>(R.id.editcpu)
        val editram = findViewById<EditText>(R.id.editram)
        val editrom = findViewById<EditText>(R.id.editrom)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val imagebutton = findViewById<Button>(R.id.imagebutton)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                imageUri = data?.data
                imageUri?.let {
                    imageView.setImageURI(it)
                    // Handle the image URI here if needed (e.g., uploading to a server)
                }
            }
        }

        nextbutton.setOnClickListener {
            if (editbrand.text.toString() == "") {
                editbrand.error = "กรุณากรอกแบรนด์สินค้า"
                return@setOnClickListener
            }
            if (editname.text.toString() == "") {
                editname.error = "กรุณากรอกชื่อรุ่น"
                return@setOnClickListener
            }
            if (editserial.text.toString() == "") {
                editserial.error = "กรุณากรอกซีเรียลนัมเบอร์"
                return@setOnClickListener
            }
            if (editamount.text.toString() == "") {
                editamount.error = "กรุณากรอกจำนวนคงเหลือ"
                return@setOnClickListener
            }
            if (editprice.text.toString() == "") {
                editprice.error = "กรุณากรอกราคา"
            }
            if (editcpu.text.toString() == "") {
                editcpu.error = "กรุณากรอกความเร็วซีพียู"
                return@setOnClickListener
            }
            if (editram.text.toString() == "") {
                editram.error = "กรุณากรอกความจุหน่วยความจำ"
                return@setOnClickListener
            }
            if (editrom.text.toString() == "") {
                editrom.error = "กรุณากรอกความจุฮาร์ดดิสก์"
                return@setOnClickListener
            }


            val url ="http://10.13.4.3:3000/product"
            Log.d("tag", url)
            val okHttpClient = okhttp3.OkHttpClient()
            Log.d("tag", "x1")
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("brandproduct", editbrand.text.toString())
                .addFormDataPart("nameproduct", editname.text.toString())
                .addFormDataPart("serialnumber", editserial.text.toString())
                .addFormDataPart("amount", editamount.text.toString())
                .addFormDataPart("price", editprice.text.toString())
                .addFormDataPart("cpu", editcpu.text.toString())
                .addFormDataPart("ram", editram.text.toString())
                .addFormDataPart("rom", editrom.text.toString())

            imageUri?.let {
                val file = File(it.path!!)
                val requestFile = file.asRequestBody("image/jpeg".toMediaType())
                builder.addFormDataPart("Image", file.name, requestFile)
            }
            val requestBody = builder.build()

            val request: Request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Log.d("tag", "x3")
                val obj = JSONObject(response.body!!.string())
                val status = obj.getString("status")
                val message = obj.getString("message")

                if (status == "true") {
                        Log.d("tag", "x4")
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

                        // Redirect to main page
                        val intent = Intent(this, search::class.java)
                        startActivity(intent)
                        finish()
                            } else {
                                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                            }
            }
            else {
                Toast.makeText(applicationContext, "เกิดข้อผิดพลาดในการเพิ่มข้อมูล", Toast.LENGTH_LONG).show()
            }
        }

        backbutton.setOnClickListener {
            val intent = Intent(this, search::class.java)
            startActivity(intent)
        }

        imagebutton.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }

    }
}



