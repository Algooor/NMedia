package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository {
    override val data = MutableLiveData(
        Post(
            id = 0L,
            author = "Igor",
            content = "Встреча",
            published = "13.06.22"
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data value не может быть нулевым"
        }
        val likedPost = currentPost.copy(
            likedByMe = !currentPost.likedByMe
        )
        likedPost.likes = countLikeByMe(likedPost.likedByMe, likedPost.likes)
        likedPost.reductionLike = reductionNumbers(likedPost.likes)
        data.value = likedPost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Data value не может быть нулевым"
        }
        currentPost.countShare += 1
        currentPost.reductionShare = reductionNumbers(currentPost.countShare)
        data.value = currentPost
    }

    private fun countLikeByMe(liked: Boolean, like: Int) =
        if (liked) like + 1 else like - 1

    private fun reductionNumbers(count: Int): String {
        return if (count in 0..999) count.toString()
        else {
            val stepCount = count.toString().length
            val answer: Int
            if (stepCount in 4..6) {
                answer = count / 10.pow(2)
                if (answer % 10 == 0) "${answer / 10}K"
                else "${answer / 10},${answer % 10}K"
            } else {
                answer = (count / 1000) / 10.pow(2)
                if (answer % 10 == 0) "${answer / 10}M"
                else "${answer / 10},${answer % 10}M"
            }
        }
    }

    private fun Int.pow(x: Int): Int = (2..x).fold(this) { r, _ -> r * this }
}