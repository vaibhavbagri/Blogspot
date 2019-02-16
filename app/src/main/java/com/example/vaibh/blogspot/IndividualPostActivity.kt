package com.example.vaibh.blogspot

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.text.method.ScrollingMovementMethod



class IndividualPostActivity : AppCompatActivity() {

    lateinit var mPostTitle: TextView
    lateinit var mPostBody: TextView

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_BODY = "body"

        fun newIntent(context: Context, post: Posts): Intent {
            val detailIntent = Intent(context, IndividualPostActivity::class.java)

            detailIntent.putExtra(EXTRA_TITLE, post.title)
            detailIntent.putExtra(EXTRA_BODY, post.body)

            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_post)

        val title = intent.getStringExtra(EXTRA_TITLE)
        val body = intent.getStringExtra(EXTRA_BODY)

        mPostTitle = findViewById(R.id.textViewPostTitle)
        mPostBody = findViewById(R.id.textViewPostBody)
        mPostBody.movementMethod = ScrollingMovementMethod()

        mPostTitle.text = title
        mPostBody.text = body

    }
}
