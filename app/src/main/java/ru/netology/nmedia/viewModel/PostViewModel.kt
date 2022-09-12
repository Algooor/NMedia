package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.impl.SharedPrefsPostRepository
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository = FilePostRepository(application)
    val data by repository::data

    val currentPost = MutableLiveData<Post?>(null)
    val navigateToPostContentScreenEvent = SingleLiveEvent<String>()
    val sharePostContent = SingleLiveEvent<String>()
    val videoPlay = SingleLiveEvent<Unit>()

    fun onSaveButtonClicked(content: String) {

        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Игорь",
            content = content,
            published = "Сегодня",
            video = "https://youtu.be/CgsDHdpqVn0"

        )
        repository.save(post)
        currentPost.value = null
    }

    fun onEditCancelClicked() {
        currentPost.value?.let {
            currentPost.value = currentPost.value
        }
    }

    override fun onLikeClicked(post: Post) = repository.like(post.id)

    override fun onShareClicked(post: Post){
        sharePostContent.value = post.content
    }
    override fun onRemoveClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreenEvent.value = post.content
    }

    override fun onVideoPlayClicked(post: Post) {
        videoPlay.call()
    }

    fun onAddClicked() {
        navigateToPostContentScreenEvent.call()
    }


}