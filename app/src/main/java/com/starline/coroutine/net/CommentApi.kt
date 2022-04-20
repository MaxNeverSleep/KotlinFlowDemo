package com.starline.coroutine.net

import com.starline.coroutine.model.CommentModel
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Comment Api
 *
 * @author jianyu.yang
 * @date 2022/4/12
 **/
interface CommentApi {

    @GET("comments/1/")
    suspend fun getComment(
        @Query("postId") postId: String,
    ): CommentModel

}