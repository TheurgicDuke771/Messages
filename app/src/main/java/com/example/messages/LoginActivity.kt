package com.example.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            val email = email_login.text.toString()
            val password = password_login.text.toString()

            Toast.makeText(applicationContext, "Attempt login with email/pw: $email/***", Toast.LENGTH_SHORT).show()
        }

        backToRegister.setOnClickListener{
            finish()
        }
    }
}
