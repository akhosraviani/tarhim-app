package ir.co.mazar.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

class ManageKeyboard {
    public fun hidAndShowKey(activity:Activity){
        var inputManager=activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view=activity.currentFocus
        if(view==null) {
            view = View(activity)
        }
        inputManager.hideSoftInputFromWindow(view.windowToken,0)
    }
}