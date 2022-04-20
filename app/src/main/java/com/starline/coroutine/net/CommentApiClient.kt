package com.starline.coroutine.net

import com.starline.coroutine.model.CommentApiState
import com.starline.coroutine.model.CommentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * CommentApiClient
 *
 * @author jianyu.yang
 * @date 2022/4/12
 **/
class CommentApiClient {

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    private var mApi: CommentApi? = null

    init {
        val clientBuilder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        val okHttpClient = clientBuilder.build()
        val retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mApi = retrofit.create(CommentApi::class.java)
    }

    suspend fun getComment(postId: String): Flow<CommentApiState<CommentModel>> {
        return flow {

            //get comment data from api
            val comment = mApi?.getComment(postId)

            //emit data wrapped in state class [CommentApiState]
            emit(CommentApiState.success(comment))

        }.flowOn(Dispatchers.IO)
    }

}