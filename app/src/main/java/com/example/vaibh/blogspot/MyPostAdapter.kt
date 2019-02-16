package com.example.vaibh.blogspot

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class MyPostAdapter(val contextt: Context, val layoutResId: Int, val postList: List<Posts>)
    : ArrayAdapter<Posts>(contextt, layoutResId, postList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val mInflater: LayoutInflater = LayoutInflater.from(contextt)
        val view: View = mInflater.inflate(layoutResId, null)

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewAuthor = view.findViewById<TextView>(R.id.textViewAuthor)
        val postUpdateButton = view.findViewById<Button>(R.id.postUpdateButton)
        val postDeleteButton = view.findViewById<Button>(R.id.postDeleteButton)

        val post = postList[position]

        textViewTitle.text = post.title
        textViewAuthor.text = post.author

        postUpdateButton.setOnClickListener{

            updatePost(post)

        }

        postDeleteButton.setOnClickListener{
            deletePost(post)
        }

        return view
    }

    private fun updatePost(post: Posts) {

        val intent = Intent(contextt, UpdatePostActivity::class.java)
        intent.putExtra("title", post.title)
        intent.putExtra("body", post.body)
        intent.putExtra("postId", post.postId)
        contextt.startActivity(intent)

    }


    private fun deletePost(post: Posts) {
        val mDatabase = FirebaseDatabase.getInstance().getReference("Posts")
        mDatabase.child(post.postId).removeValue()
        Toast.makeText(contextt,"Deleted !",Toast.LENGTH_LONG).show()
    }
}