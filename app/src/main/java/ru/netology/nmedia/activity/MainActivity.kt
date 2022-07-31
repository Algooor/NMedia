package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.launch
import androidx.activity.viewModels
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.util.showKeyboard
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
            published = "13.06.22",
        )

        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }


//        binding.cancelButton.setOnClickListener {
//            with(binding.contentEditText) {
//                viewModel.onEditCancelClicked()
//                val content = binding.contentEditText.text.toString()
//                viewModel.onSaveButtonClicked(content)
//            }
//        }

//        viewModel.currentPost.observe(this) { currentPost ->
//            with(binding.contentEditText) {
//                val content = currentPost?.content
//                setText(content)
//                if (content != null) {
//                    binding.contentGroup.visibility = View.VISIBLE
//                    requestFocus()
//                    showKeyboard()
//                } else {
//                    binding.contentGroup.visibility = View.GONE
//                    clearFocus()
//                    hideKeyboard()
//                }
//            }
//        }
        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent =
                Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        viewModel.videoPlay.observe(this) {

            if (post.video.isNullOrBlank()) {
                Toast.makeText(this@MainActivity, R.string.video_not_found, Toast.LENGTH_SHORT).show()
                return@observe
                    }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video)).apply {
                    post.video
                }
                startActivity(intent)
            }


            val postContentActivityLauncher = registerForActivityResult(
                PostContentActivity.ResultContract
            ) { postContent ->
                postContent ?: return@registerForActivityResult
                viewModel.onSaveButtonClicked(postContent)
            }
            viewModel.navigateToPostContentScreenEvent.observe(this) {
                postContentActivityLauncher.launch()
            }

        }

    }








