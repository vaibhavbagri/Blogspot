package com.example.vaibh.blogspot


class Posts (val author: String, val title: String, val body: String, val uid: String, val postId : String){

    constructor(): this("", "", "", "", ""){}

}
