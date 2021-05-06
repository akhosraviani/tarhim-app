package ir.co.mazar.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import ir.co.mazar.model.ConfirmDataModel
import ir.co.mazar.model.deceased.comment.ReplyCommentRequest
import ir.co.mazar.model.deceased.like.LikeCommentDataModel
import ir.co.mazar.model.deceased.like.LikeCommentRequest
import ir.co.mazar.ui.repository.deceasad.DeceasedRepository
import ir.co.mazar.utils.TarhimConfig

class DeceasedViewModel: ViewModel() {
    private var deceasedRepository: DeceasedRepository = DeceasedRepository()
     var ldLikeComment: LiveData<LikeCommentDataModel>
     var ldReplayComment: LiveData<ConfirmDataModel>
     var ldDeletePhoto: LiveData<ConfirmDataModel>

    init {
        ldLikeComment = deceasedRepository.mldLikeComment
        ldReplayComment = deceasedRepository.mldReplyComment
        ldDeletePhoto = deceasedRepository.mldDeletePhoto
    }

    fun requestLikeComment(dataRequest: LikeCommentRequest) {
        deceasedRepository.likeComments(dataRequest, Hawk.get(TarhimConfig.USER_NUMBER))
    }

    fun requestReplyComment(body: ReplyCommentRequest) {
        deceasedRepository.replyComment(body ,Hawk.get(TarhimConfig.USER_NUMBER) )
    }

    fun deletePhotoFromGallery(deceasedid : Int,  path  : String) {
        Log.i("testTag2", "im in view model")
        deceasedRepository.deletePhoto(deceasedid , Hawk.get(TarhimConfig.USER_NUMBER) , path)
    }
}