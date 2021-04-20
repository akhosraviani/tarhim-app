package ir.co.tarhim.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.model.deceased.DeceasedDataModel
import ir.co.tarhim.model.deceased.like.LikeCommentDataModel
import ir.co.tarhim.model.deceased.like.LikeCommentRequest
import ir.co.tarhim.model.user.RegisterUser
import ir.co.tarhim.ui.repository.LoginRepository
import ir.co.tarhim.ui.repository.deceasad.DeceasedRepository
import ir.co.tarhim.utils.TarhimConfig

class DeceasedViewModel: ViewModel() {
    private var deceasedRepository: DeceasedRepository = DeceasedRepository()
     var ldLikeComment: LiveData<LikeCommentDataModel>

    init {
        ldLikeComment = deceasedRepository.mldLikeComment
    }

    fun requestLikeComment(dataRequest: LikeCommentRequest) {
        deceasedRepository.likeComments(dataRequest, Hawk.get(TarhimConfig.USER_NUMBER))
    }
}