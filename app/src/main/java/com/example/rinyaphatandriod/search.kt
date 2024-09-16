package com.example.rinyaphatandriod

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class search : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val addbutton = findViewById<Button>(R.id.addbutton)
        val nextbutton = findViewById<Button>(R.id.nextbutton)
        val editsearchproduct = findViewById<EditText>(R.id.editsearchproduct)


        addbutton.setOnClickListener {
            val intent = Intent(this, Add_infor::class.java)
            startActivity(intent)
        }
        nextbutton.setOnClickListener {
            val intent = Intent(this, show::class.java)
            startActivity(intent)
            intent.putExtra("PRODUCT", editsearchproduct.text.toString())
            startActivity(intent)
            if (editsearchproduct.text.toString() == "") {
                editsearchproduct.error = "กรุณากรอกคำค้นหา"
            }
        }
    }
}