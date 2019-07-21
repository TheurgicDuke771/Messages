package com.example.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_register.setOnClickListener {
            val email = email_register.text.toString()
            val password = password_register.text.toString()

            Toast.makeText(applicationContext,"Email : $email Password : $password",Toast.LENGTH_SHORT).show()
        }

        alreadyHaveAcc.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
