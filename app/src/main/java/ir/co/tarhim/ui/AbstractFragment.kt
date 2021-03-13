package ir.co.tarhim.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation


abstract class AbstractFragment :
    Fragment() {

    protected abstract val layoutId: Int
    private lateinit var navController: NavController
    protected lateinit var activity: AbstractActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = (super.getActivity() as AbstractActivity)
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(layoutId, container, false)
    }

    open fun navigate(direction: NavDirections?) {
        if (direction != null) {
            navController.navigate(direction)
        }
    }

    open fun display(function: () -> Unit) {
        activity.display(function)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}