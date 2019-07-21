package com.example.messages

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_register.setOnClickListener {
            performRegister()
        }

        alreadyHaveAcc.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun performRegister(){
        val email = email_register.text.toString()
        val password = password_register.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(baseContext, "Please enter Email/Password",
                Toast.LENGTH_SHORT).show()
            return
        }

        //Firebase Authentication to create a user with email and password
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Successfully created user with uid ${task.result?.user?.uid}",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
