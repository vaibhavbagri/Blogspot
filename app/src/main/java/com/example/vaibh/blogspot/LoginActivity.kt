package com.example.vaibh.blogspot

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    lateinit var mLoginBtn : Button
    lateinit var mCreateUser : TextView
    lateinit var mLoginEmail : EditText
    lateinit var mLoginPassword : EditText
    lateinit var mAuth: FirebaseAuth
    lateinit var mPogressBar : ProgressDialog
    lateinit var sp : SharedPreferences

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        mCreateUser = findViewById(R.id.loginNewUser)
        mLoginBtn = findViewById(R.id.loginButton)
        mLoginEmail = findViewById(R.id.loginEmail)
        mLoginPassword = findViewById(R.id.loginPassword)

        sp = getSharedPreferences("login", Context.MODE_PRIVATE)
        if(sp.getBoolean("logged",false)){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mPogressBar = ProgressDialog(this)

        mCreateUser.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        mLoginBtn.setOnClickListener{

            val email = mLoginEmail.text.toString().trim()
            val password = mLoginPassword.text.toString().trim()

            if(TextUtils.isEmpty(email)){
                mLoginEmail.error = "Enter Email ID"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                mLoginPassword.error = "Enter Password"
                return@setOnClickListener
            }
            sp.edit().putBoolean("logged", true).apply()
            loginUser(email, password)


        }
    }

    private fun loginUser(email: String, password: String) {

        mPogressBar.setMessage("Please wait")
        mPogressBar.show()

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        mPogressBar.dismiss()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()


                    } else {

                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }

                    mPogressBar.dismiss()
                }


    }
}