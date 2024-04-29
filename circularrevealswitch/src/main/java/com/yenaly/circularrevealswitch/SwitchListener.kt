package com.yenaly.circularrevealswitch

/**
 * Interface SwitchListener provides methods to handle animation events.
 */
interface SwitchListener {
    /**
     * Method onAnimStart is called when the animation starts.
     * Override this method to provide custom behavior on the start of the animation.
     */
    fun onAnimStart() {}

    /**
     * Method onAnimEnd is called when the animation ends.
     * Override this method to provide custom behavior on the end of the animation.
     */
    fun onAnimEnd() {}

    /**
     * Method onAnimCancel is called when the animation is cancelled.
     * Override this method to provide custom behavior when the animation is cancelled.
     */
    fun onAnimCancel() {}
}