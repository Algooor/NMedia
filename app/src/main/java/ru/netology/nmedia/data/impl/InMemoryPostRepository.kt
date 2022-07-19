package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POSTS_AMOUNT.toLong()

    private val posts
        get() = checkNotNull(data.value) {
            "Data value не может быть нулевым"
        }

    override val data = MutableLiveData(
        List(GENERATED_POSTS_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Igor",
                content = "Совершенно рандомное сообщение № $index",
                published = "30.06.22"
            )
        }
    )


    override fun like(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = countLikeByMe(it.likedByMe, it.likes),
                reductionLike = reductionNumbers(it.likes)
            )
        }

    }

    override fun share(postId: Long) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                countShare = it.countShare + 1,
                reductionShare = reductionNumbers(it.countShare)
            )
        }
    }

    override fun delete(postId: Long) {
        data.value = posts.filter { it.id != postId }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        data.value = listOf(
            post.copy(
                id = ++nextId)
        ) + posts
    }

    private companion object {
        const val GENERATED_POSTS_AMOUNT = 1000
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