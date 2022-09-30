package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.CurrentPostFragmentBinding
import ru.netology.nmedia.util.Counter
import ru.netology.nmedia.viewModel.PostViewModel


class CurrentPostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()
    private val args by navArgs<CurrentPostFragmentArgs>()

    private lateinit var currentPost: Post

    private val Fragment.packageManager
        get() = activity?.packageManager

     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return CurrentPostFragmentBinding.inflate(
            layoutInflater, container, false
        ).also { binding ->
            with(binding) {


                viewModel.data.observe(viewLifecycleOwner) { listPost ->
                    if (listPost.none { it.id == args.idCurrentPost }) {
                        return@observe
                    }
                    currentPost = listPost.firstOrNull {
                        it.id == args.idCurrentPost
                    } as Post
                    render(currentPost)
                }

                viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, postContent)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(
                        intent, getString(R.string.chooser_share_post)
                    )
                    startActivity(shareIntent)
                }


                viewModel.videoPlay.observe(viewLifecycleOwner) { videoUrl ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl.toString()))
                    if (packageManager?.let { intent.resolveActivity(it) } != null) {
                        startActivity(intent)
                    }
                }

                viewModel
                    .navigateToPostContentScreenEvent
                    .observe(viewLifecycleOwner) { initialContent ->
                        val direction =
                            CurrentPostFragmentDirections.currentPostFragmentToPostContentFragment(
                                initialContent, PostContentFragment.REQUEST_CURRENT_POST_KEY
                            )
                        findNavController().navigate(direction)
                    }

                setFragmentResultListener(
                    requestKey = PostContentFragment.REQUEST_CURRENT_POST_KEY
                ) { requestKey, bundle ->
                    if (requestKey != PostContentFragment.REQUEST_CURRENT_POST_KEY) return@setFragmentResultListener
                    val newPostContent = bundle.getString(
                        PostContentFragment.RESULT_KEY
                    ) ?: return@setFragmentResultListener
                    viewModel.onSaveButtonClicked(newPostContent)
                }

                favorite.setOnClickListener {
                    viewModel.onLikeClicked(currentPost)
                }

                share.setOnClickListener {
                    viewModel.onShareClicked(currentPost)
                }

                binding.playVideo.setOnClickListener {
                    viewModel.onVideoPlayClicked(currentPost)
                }
                binding.videoView.setOnClickListener {
                    viewModel.onVideoPlayClicked(currentPost)
                }

                val popupMenu by lazy {
                    context?.let {
                        PopupMenu(it, binding.options).apply {
                            inflate(R.menu.options_post)
                            setOnMenuItemClickListener { menuItem ->
                                when (menuItem.itemId) {
                                    R.id.remove -> {
                                        viewModel.onRemoveClicked(currentPost)
                                        findNavController().popBackStack()
                                        true
                                    }
                                    R.id.edit -> {
                                        viewModel.onEditClicked(currentPost)
                                        true
                                    }
                                    else -> false
                                }
                            }
                        }
                    }
                }
                binding.options.setOnClickListener {
                    popupMenu?.show()
                }
            }
        }.root
    }

    private fun CurrentPostFragmentBinding.render(post: Post) {
        authorName.text = post.author
        content.text = post.content
        date.text = post.published
        content.movementMethod = ScrollingMovementMethod()
        favorite.text = post.reductionLike
        favorite.isChecked = post.likedByMe
        share.text = post.reductionShare
        videoView.isVisible = post.video != null
    }
}





//package ru.netology.nmedia.ui
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import androidx.fragment.app.setFragmentResultListener
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
//import ru.netology.nmedia.Post
//import ru.netology.nmedia.R
//import ru.netology.nmedia.databinding.CurrentPostFragmentBinding
//import ru.netology.nmedia.viewModel.PostViewModel
//
//class CurrentPostFragment : Fragment() {
//
//    private val viewModel: PostViewModel by activityViewModels()
//    private val args by navArgs<CurrentPostFragmentArgs>()
//
//    private lateinit var currentPost: Post
//
//    private val Fragment.packageManager
//        get() = activity?.packageManager
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return CurrentPostFragmentBinding.inflate(
//            layoutInflater, container, false
//        ).also { binding ->
//            with(binding) {
//
//                viewModel.data.observe(viewLifecycleOwner) { listPost ->
//                    if (listPost.none { it.id == args.idCurrentPost }) {
//                        return@observe
//                    }
//                    currentPost = listPost.firstOrNull {
//                        it.id == args.idCurrentPost
//                    } as Post
//                    render(currentPost)
//                }
//                viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
//                    val intent = Intent().apply {
//                        action = Intent.ACTION_SEND
//                        putExtra(Intent.EXTRA_TEXT, postContent)
//                        type = "text/plain"
//                    }
//                    val shareIntent =
//                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                    startActivity(shareIntent)
//                }
//                viewModel.videoPlay.observe(viewLifecycleOwner) { videoUrl ->
//
////                    if (videoUrl.isNullOrBlank()) {
////                        // Toast.makeText(viewLifecycleOwner, R.string.video_not_found, Toast.LENGTH_SHORT).show()
////                        return@observe
////                    }
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl.toString()))
//                    if (packageManager?.let { intent.resolveActivity(it) } != null) {
//                        startActivity(intent)
//                    }
//                }
//                viewModel
//                    .navigateToPostContentScreenEvent
//                    .observe(viewLifecycleOwner) { initialContent ->
//                        val direction =
//                            CurrentPostFragmentDirections.currentPostFragmentToPostContentFragment(
//                                initialContent, PostContentFragment.REQUEST_CURRENT_POST_KEY
//                            )
//                        findNavController().navigate(direction)
//                    }
//                setFragmentResultListener(
//                    requestKey = PostContentFragment.REQUEST_CURRENT_POST_KEY
//                ) { requestKey, bundle ->
//                    if (requestKey != PostContentFragment.REQUEST_CURRENT_POST_KEY) return@setFragmentResultListener
//                    val newPostContent = bundle.getString(
//                        PostContentFragment.RESULT_KEY
//                    ) ?: return@setFragmentResultListener
//                    viewModel.onSaveButtonClicked(newPostContent)
//                }
//            }


