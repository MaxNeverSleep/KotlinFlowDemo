package com.starline.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.starline.coroutine.databinding.ActivityMainBinding
import com.starline.coroutine.model.Status
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * MainActivity
 *
 * @author jianyu.yang
 * @date 2022/4/12
 **/
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: CommentViewModel by lazy {
        ViewModelProvider(this)[CommentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.comments.collect { it ->
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressbarLoading.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        binding.progressbarLoading.visibility = View.GONE
                        binding.tvComment.text = it.data?.body
                    }
                    Status.ERROR -> {
                        binding.progressbarLoading.visibility = View.GONE
                        binding.tvComment.text = it.message
                    }
                }
            }
        }

        binding.btnGet.setOnClickListener {
            val postId = binding.etPostId.text.toString()
            viewModel.getComment(postId)
        }

        viewModel.getComment("1")
    }
}