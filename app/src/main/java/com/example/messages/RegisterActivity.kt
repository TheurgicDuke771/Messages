package com.example.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
                    saveUserToFirebaseDatabase(email)
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

    private fun saveUserToFirebaseDatabase(email: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, userName_register.text.toString(), email)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Finally we saved the user to Firebase Database")
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to set value to database: ${it.message}")
            }
    }
}

class User(val uid: String, val username: String, val email: String)
