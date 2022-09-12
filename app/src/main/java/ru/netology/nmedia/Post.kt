package ru.netology.nmedia

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var reductionLike: String = "999",
    var likes: Int = 999,
    val likedByMe: Boolean = false,
    var countShare: Int = 999,
    var reductionShare: String = "999",
    val video: String = "https://youtu.be/CgsDHdpqVn0"
)
