package com.example.vaibh.blogspot

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener
import android.text.TextUtils
import android.app.ProgressDialog
import android.widget.*


class SignUpActivity : AppCompatActivity() {

    lateinit var mSignUpBtn : Button
    lateinit var mExistingUser : TextView
    lateinit var mSignUpEmail : EditText
    lateinit var mSignUpPassword : EditText
    lateinit var mSignUpUsername : EditText
    lateinit var mPogressBar : ProgressDialog
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()
        mExistingUser = findViewById(R.id.signUpExstingUser)
        mSignUpBtn = findViewById(R.id.signUpButton)
        mSignUpEmail = findViewById(R.id.signUpEmail)
        mSignUpPassword = findViewById(R.id.signUpPassword)
        mSignUpUsername = findViewById(R.id.signUpName)

        mPogressBar = ProgressDialog(this)

        mExistingUser.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        mSignUpBtn.setOnClickListener {

            val name = mSignUpUsername.text.toString().trim()
            val email = mSignUpEmail.text.toString().trim()
            val password = mSignUpPassword.text.toString().trim()

            if(TextUtils.isEmpty(name)){
                mSignUpUsername.error = "Enter Username"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(email)){
                mSignUpEmail.error = "Enter Email ID"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                mSignUpPassword.error = "Enter Password"
                return@setOnClickListener
            }

            createUser(name, email, password)
        }
    }

    private fun createUser(name : String, email : String, password : String) {

        mPogressBar.setMessage("Please wait")
        mPogressBar.show()

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( this) { task ->
                    if (task.isSuccessful) {

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val uid = currentUser!!.uid

                        val userMap = HashMap<String, String>()
                        userMap["name"] = name

                        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(uid)

                        mDatabase.setValue(userMap).addOnCompleteListener( OnCompleteListener { task ->

                            if(task.isSuccessful){

                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()

                                mPogressBar.dismiss()
                            }
                        })



                    } else {

                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        mPogressBar.dismiss()
                    }
                }


    }
}
