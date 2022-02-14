package com.lianyi.paimonsnotebook.lib.listener

import android.animation.Animator

//animation监听器
class AnimatorFinished(val block: () -> Unit): Animator.AnimatorListener{

    override fun onAnimationStart(p0: Animator?) {
    }

    override fun onAnimationEnd(p0: Animator?) {
        block()
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationRepeat(p0: Animator?) {
    }
}