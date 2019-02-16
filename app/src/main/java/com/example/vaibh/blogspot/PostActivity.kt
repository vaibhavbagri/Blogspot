package com.example.vaibh.blogspot

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class PostActivity : AppCompatActivity(){

    lateinit var mTitle : EditText
    lateinit var mBody : EditText
    lateinit var mPostButton : Button
    lateinit var mDatabase : DatabaseReference
    lateinit var mAuth : FirebaseAuth
    lateinit var mProgressBar : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        mTitle = findViewById(R.id.postTitle)
        mBody = findViewById(R.id.postBody)
        mPostButton = findViewById(R.id.postButton)
        mProgressBar = ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()
        val uid = mAuth.currentUser!!.uid



        var author  = ""

        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(uid)
        mDatabase.child("name").addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                author = p0.getValue(String::class.java).toString()
            }

        })

        mPostButton.setOnClickListener{


            val title = mTitle.text.toString().trim()
            val body = mBody.text.toString().trim()

            if(TextUtils.isEmpty(title)){
                mTitle.error = "Enter Username"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(body)){
                mBody.error = "Enter Email ID"
                return@setOnClickListener
            }

            createPost(uid, author, title, body)

        }
    }

    private fun createPost(uid: String, author: String, title: String, body: String) {

        mProgressBar.setMessage("Please wait")
        mProgressBar.show()

        mDatabase = FirebaseDatabase.getInstance().getReference("Posts")

        val key = mDatabase.push().key.toString()

        val userMap = HashMap<String, String>()
        userMap["uid"] = uid
        userMap["author"] = author
        userMap["title"] = title
        userMap["body"] = body
        userMap["postId"] = key

        mDatabase.child(key).setValue(userMap).addOnCompleteListener( OnCompleteListener { task ->

            if(task.isSuccessful){

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()

                mProgressBar.dismiss()
            }
        })

    }
}