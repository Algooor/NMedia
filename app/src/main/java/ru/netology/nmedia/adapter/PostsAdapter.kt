package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding


internal class PostsAdapter(
    private val interactionListener: PostInteractionListener

) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: PostBinding,
        listener: PostInteractionListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post
        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }

                }
            }
        }

        init {
            binding.favorite.setOnClickListener { listener.onLikeClicked(post) }
            binding.share.setOnClickListener { listener.onShareClicked(post) }
            binding.options.setOnClickListener { listener.onRemoveClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                authorName.text = post.author
                postsText.text = post.content
                date.text = post.published
                shareCount.text = post.reductionShare
                likesCount.text = post.reductionLike
                favorite.setImageResource(getLikeIconResId(post.likedByMe))
                options.setOnClickListener { popupMenu.show() }
            }
        }

        @DrawableRes
        private fun getLikeIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_favorite_24
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }
}