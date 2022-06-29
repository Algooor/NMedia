package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 0L,
            author = "Igor",
            content = "Встреча",
            published = "13.06.22"
        )
        viewModel.data.observe(this) { post ->
            binding.render(post)
        }


        binding.favorite.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.share.setOnClickListener {
            viewModel.onShareClicked()
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        authorName.text = post.author
        postsText.text = post.content
        date.text = post.published
        shareCount.text = post.reductionShare
        likesCount.text = post.reductionLike
        favorite.setImageResource(getLikeIconResId(post.likedByMe))
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_favorite_24
}


