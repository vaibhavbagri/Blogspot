package com.example.vaibh.blogspot

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot

class MainActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mToolbar : Toolbar
    lateinit var mFloatButton : FloatingActionButton
    lateinit var mDatabase : DatabaseReference
    lateinit var mListView : ListView
    lateinit var uid : String
    lateinit var postList: MutableList<Posts>
    lateinit var sp : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mFloatButton = findViewById(R.id.floatingActionButton)
        mToolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = "Blogspot"
        mToolbar.setTitleTextColor(Color.WHITE)
        uid = mAuth.currentUser!!.uid
        mListView = findViewById(R.id.listView)
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts")
        sp = getSharedPreferences("login", Context.MODE_PRIVATE)
        postList = mutableListOf()

        mDatabase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    postList.clear()
                    for(h in dataSnapshot.children){
                        val post = h.getValue(Posts::class.java)
                        postList.add(post!!)
                    }

                    val adapter = PostAdapter(applicationContext, R.layout.row_items, postList)
                    mListView.adapter = adapter

                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }

        })

        val context = this
        mListView.setOnItemClickListener { _, _, position, _ ->
            val selectedPost = postList[position]
            val detailIntent = IndividualPostActivity.newIntent(context, selectedPost)
            startActivity(detailIntent)
        }

        mFloatButton.setOnClickListener{
            val postIntent = Intent(applicationContext, PostActivity::class.java)
            startActivity(postIntent)
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser == null){
            val intent = Intent(applicationContext , LoginActivity::class.java)
            startActivity(intent)
            finish()

        }else{

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.menuLogout){

            FirebaseAuth.getInstance().signOut()
            sp.edit().putBoolean("logged", false).apply()
            val startIntent = Intent (applicationContext, LoginActivity::class.java)
            startActivity(startIntent)
            finish()

        }

        if(item?.itemId == R.id.menuProfile){

//            Toast.makeText(applicationContext, "Profile", Toast.LENGTH_LONG).show()

            val startIntent = Intent (applicationContext, UserProfileActivity::class.java)
            startActivity(startIntent)
            finish()
        }

        return true

    }

}
