package ir.co.tarhim.ui.repository.deceasad

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.co.tarhim.model.ConfirmDataModel
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
}