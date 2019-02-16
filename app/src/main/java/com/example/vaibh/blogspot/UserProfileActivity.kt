package com.example.vaibh.blogspot

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.support.v7.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserProfileActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var postList: MutableList<Posts>
    lateinit var mListView: ListView
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts")
        val uid = mAuth.currentUser!!.uid
        postList = mutableListOf()
        mListView = findViewById(R.id.myListView)
        mToolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(mToolbar)
        mDatabase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    postList.clear()
                    for (h in dataSnapshot.children) {
                        val post = h.getValue(Posts::class.java)
                        if (post!!.uid == uid)
                            postList.add(post)
                    }

                    val adapter = MyPostAdapter(applicationContext, R.layout.profile_row_items, postList)
                    mListView.adapter = adapter

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }

        })

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        mToolbar.setNavigationOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        })


        val context = this
        mListView.setOnItemClickListener { _, _, position, _ ->
            val selectedPost = postList[position]
            val detailIntent = IndividualPostActivity.newIntent(context, selectedPost)
            startActivity(detailIntent)
        }

    }
}