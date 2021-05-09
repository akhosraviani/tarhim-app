package ir.co.mazar.ui.repository.deceasad

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.co.mazar.model.ConfirmDataModel
import ir.co.mazar.model.deceased.comment.ReplyCommentRequest
import ir.co.mazar.model.deceased.like.LikeCommentDataModel
import ir.co.mazar.model.deceased.like.LikeCommentRequest
import ir.co.mazar.network.RequestClient
import retrofit2.Callback
import retrofit2.Response

class DeceasedRepository {

    val mldError = MutableLiveData<Throwable>()
    val mldLikeComment = MutableLiveData<LikeCommentDataModel>()
    val mldReplyComment = MutableLiveData<ConfirmDataModel>()
    val mldDeletePhoto = MutableLiveData<ConfirmDataModel>()


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

    fun deletePhoto(deceasedid : Int, mobile: String , path : String) {
        RequestClient.makeRequest().requestDeletePhotoFromGallery(deceasedid, mobile, path)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(
                    call: retrofit2.Call<ConfirmDataModel>,
                    t: Throwable
                ) {
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    Log.i("testTag2", "im here delete=" + response.body().toString())
                    Log.i("testTag2", "im here delete=")
                    mldDeletePhoto.value = ConfirmDataModel(response.body()!!.message, response.body()!!.code)

                }
            })

    }
    }
