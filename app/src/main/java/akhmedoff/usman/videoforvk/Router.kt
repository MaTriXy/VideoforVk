package akhmedoff.usman.videoforvk

import android.support.transition.*
import android.support.transition.TransitionSet.ORDERING_TOGETHER
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import android.support.v4.view.ViewCompat
import android.view.View


object Router {

    private const val FADE_DEFAULT_TIME = 300L
    private const val MOVE_DEFAULT_TIME = 500L

    fun hideFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction()
                .hide(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    fun replaceFragment(
            fragmentManager: FragmentManager,
            prevFragment: Fragment? = null,
            fragment: Fragment,
            addToBackStack: Boolean = false,
            fragmentTag: String?,
            sharedElement: View
    ) {
        val transaction = fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, fragment, fragmentTag)

        if (addToBackStack) {
            transaction.addToBackStack(fragmentTag)
        }

        val exitFade = Fade()
        prevFragment?.exitTransition = exitFade

        val enterTransitionSet = TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeTransform())
                .addTransition(ChangeClipBounds())
                .addTransition(ChangeImageTransform())
        enterTransitionSet.ordering = ORDERING_TOGETHER
        enterTransitionSet.duration = 375L

        val returnTransitionSet = TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeTransform())
                .addTransition(ChangeClipBounds())
                .addTransition(ChangeImageTransform())
        enterTransitionSet.ordering = ORDERING_TOGETHER
        enterTransitionSet.duration = 375L

        fragment.sharedElementEnterTransition = enterTransitionSet
        fragment.sharedElementReturnTransition = returnTransitionSet

        val enterFade = Explode()
        fragment.enterTransition = enterFade

        transaction.addSharedElement(
                sharedElement,
                ViewCompat.getTransitionName(sharedElement)
        )

        transaction.commit()
    }

    fun replaceFragment(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            addToBackStack: Boolean = false,
            fragmentTag: String?
    ) {
        val transaction = fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, fragment, fragmentTag)

        if (addToBackStack) {
            transaction.addToBackStack(fragmentTag)
        }

        transaction.setTransition(TRANSIT_FRAGMENT_OPEN)

        transaction.commit()
    }

}