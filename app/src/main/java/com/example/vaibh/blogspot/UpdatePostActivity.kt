package com.example.vaibh.blogspot

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*


class UpdatePostActivity : AppCompatActivity(){

    lateinit var mTitle : EditText
    lateinit var mBody : EditText
    lateinit var mUpdateButton : Button
    lateinit var mDatabase : DatabaseReference
    lateinit var mProgressBar : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_post)

        mTitle = findViewById(R.id.updateTitle)
        mBody = findViewById(R.id.updateBody)
        mUpdateButton = findViewById(R.id.updateButton)
        mProgressBar = ProgressDialog(this)

        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")
        val postId = intent.getStringExtra("postId")!!

        mTitle.setText(title, TextView.BufferType.EDITABLE)
        mBody.setText(body, TextView.BufferType.EDITABLE)


        mUpdateButton.setOnClickListener{


            val titlee = mTitle.text.toString().trim()
            val bodyy = mBody.text.toString().trim()

            if(TextUtils.isEmpty(title)){
                mTitle.error = "Enter Username"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(body)){
                mBody.error = "Enter Email ID"
                return@setOnClickListener
            }

            updatePost(titlee, bodyy, postId)

        }
    }

    private fun updatePost(title: String, body: String, postId: String) {

        mProgressBar.setMessage("Please wait")
        mProgressBar.show()

        mDatabase = FirebaseDatabase.getInstance().getReference("Posts").child(postId)

        mDatabase.child("title").setValue(title)
        mDatabase.child("body").setValue(body).addOnCompleteListener({ task ->

            if (task.isSuccessful) {

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()

                mProgressBar.dismiss()
            }

        })

    }
}