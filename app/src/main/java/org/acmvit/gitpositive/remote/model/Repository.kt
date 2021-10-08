package org.acmvit.gitpositive.remote.model

data class Repository(
    val name:String,
    val html_url:String,
    val description:String,
    val language:String,
    val stars:Int,
    val forks_count:Int
)