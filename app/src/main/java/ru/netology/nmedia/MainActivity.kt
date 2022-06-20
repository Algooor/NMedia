package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

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
        binding.render(post)
        binding.favorite.setOnClickListener {
            post.likedByMe = !post.likedByMe
            binding.favorite.setImageResource(getLikeIconResId(post.likedByMe))
            post.likes = countLikeByMe(post.likedByMe, post.likes)
            post.reductionLike = reductionNumbers(post.likes)
            binding.render(post)
        }

        binding.share.setOnClickListener {
            post.countShare += 1
            post.reductionShare = reductionNumbers(post.countShare)
            binding.render(post)
        }
    }

    private fun countLikeByMe(liked: Boolean, like: Int) =
        if (liked) like + 1 else like - 1

    private fun ActivityMainBinding.render(post: Post) {
        authorName.text = post.author
        postsText.text = post.content
        date.text = post.published
        shareCount.text = post.reductionShare
        likesCount.text = post.reductionLike
        favorite.setImageResource(getLikeIconResId(post.likedByMe))
    }

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

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_favorite_24
}


