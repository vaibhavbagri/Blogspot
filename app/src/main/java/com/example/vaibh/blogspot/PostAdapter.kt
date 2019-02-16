package com.example.vaibh.blogspot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class PostAdapter(val contextt: Context, val layoutResId: Int, val postList: List<Posts>)
    : ArrayAdapter<Posts>(contextt, layoutResId, postList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val mInflater: LayoutInflater = LayoutInflater.from(contextt)
        val view: View = mInflater.inflate(layoutResId, null)

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewAuthor = view.findViewById<TextView>(R.id.textViewAuthor)

        val post = postList[position]

        textViewTitle.text = post.title
        textViewAuthor.text = post.author

        return view
    }
}