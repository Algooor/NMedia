package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var reductionLike: String = "9,9K",
    var likes: Int = 0,
    var likedByMe: Boolean = false,
    var countShare: Int = 0,
    var reductionShare: String = "999"
)
