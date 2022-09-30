package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel


class FeedFragment : Fragment() {

   // private val viewModel by viewModels<PostViewModel>()
   private val viewModel: PostViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val post = Post(
            id = 0L,
            author = "Igor",
            content = "Встреча",
            published = "13.06.22",
        )

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

            if (post.video.isBlank()) {
               // Toast.makeText(viewLifecycleOwner, R.string.video_not_found, Toast.LENGTH_SHORT).show()
                return@observe
                    }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video)).apply {
                    post.video
                }
                startActivity(intent)
            }

             setFragmentResultListener(
                 requestKey = PostContentFragment.REQUEST_KEY
             ){requestKey, bundle ->
                 if(requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
                 val newPostContent = bundle.getString(PostContentFragment.RESULT_KEY) ?: return@setFragmentResultListener
                 viewModel.onSaveButtonClicked(newPostContent)
             }

            viewModel.navigateToPostContentScreenEvent.observe(this) { initialContent ->
                val direction = FeedFragmentDirections.toPostContentFragment(initialContent, PostContentFragment.REQUEST_KEY)
                findNavController().navigate(direction)
            }
        viewModel.navigateToCurrentPostScreenEvent.observe(this) { currentPost ->
            val direction =
                FeedFragmentDirections.toCurrentPostFragment(currentPost?.id ?: return@observe)
            findNavController().navigate(direction)
        }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

    }.root

    }









