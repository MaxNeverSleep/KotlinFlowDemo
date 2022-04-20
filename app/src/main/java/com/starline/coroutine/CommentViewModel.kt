package com.starline.coroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starline.coroutine.model.CommentApiState
import com.starline.coroutine.model.CommentModel
import com.starline.coroutine.net.CommentApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 *
 * CommentViewModel
 *
 * @author jianyu.yang
 * @date 2022/4/12
 **/
class CommentViewModel : ViewModel() {

    private val client = CommentApiClient()

    val comments = MutableStateFlow<CommentApiState<CommentModel>>(CommentApiState.loading())

    fun getComment(postId: String) {

        //Network call will takes time, so we set the value to loading state
        comments.value = CommentApiState.loading()

        //Network call will takes time, so has to be run in the background thread.
        //use viewModelScope to limit the call available only when viewModel still alive.
        viewModelScope.launch {

            client.getComment(postId)
                //If any error occurs like 404 or unknown host, it will be catch in here, and we set the state to error, so the ui will show some info
                .catch {
                    comments.value = CommentApiState.error(it.message.toString())
                }

                //if api call is succeeded, set the state to success, and set data to data received from api
                .collect {
                    comments.value = it
                }
        }
    }

}