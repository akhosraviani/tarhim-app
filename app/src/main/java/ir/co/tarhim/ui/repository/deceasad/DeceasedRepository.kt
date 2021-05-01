package ir.co.tarhim.ui.repository.deceasad

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.co.tarhim.model.ConfirmDataModel
import ir.co.tarhim.model.deceased.comment.ReplyCommentRequest
import ir.co.tarhim.model.deceased.like.LikeCommentDataModel
import ir.co.tarhim.model.deceased.like.LikeCommentRequest
import ir.co.tarhim.model.user.RegisterUser
import ir.co.tarhim.network.RequestClient
import ir.co.tarhim.ui.repository.LoginRepository
import retrofit2.Callback
import retrofit2.Response

class DeceasedRepository {

    val mldError = MutableLiveData<Throwable>()
    val mldLikeComment = MutableLiveData<LikeCommentDataModel>()
    val mldReplyComment = MutableLiveData<ConfirmDataModel>()


    fun likeComments(dataRequest: LikeCommentRequest, mobile: String) {
        RequestClient.makeRequest().requestLikeComment(dataRequest, mobile)
            .enqueue(object : Callback<LikeCommentDataModel> {
                override fun onFailure(call: retrofit2.Call<LikeCommentDataModel>, t: Throwable) {
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<LikeCommentDataModel>,
                    response: Response<LikeCommentDataModel>
                ) {
                    mldLikeComment.value =
                        LikeCommentDataModel(response.body()!!.message, response.body()!!.code,response.body()!!.userId,response.body()!!.likeCount,response.body()!!.liked)
                }
            })

    }

    fun replyComment(dataRequest: ReplyCommentRequest, mobile: String){
        RequestClient.makeRequest().requestReplyComment(dataRequest, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    mldError.value = t
                    Log.e("ReplyComment", "onFailure: "+t )
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldReplyComment.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i("ReplyComment", "im here in deceasedRepo= " + response.body())
                }
            })

    }
}