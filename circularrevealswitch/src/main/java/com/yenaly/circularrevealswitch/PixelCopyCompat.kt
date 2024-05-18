package com.yenaly.circularrevealswitch

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.view.PixelCopy
import android.view.Surface
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi

object PixelCopyCompat {

    fun request(
        window: Window, srcRect: Rect?, dest: Bitmap,
        listener: PixelCopy.OnPixelCopyFinishedListener,
        listenerHandler: Handler,
    ) {
        when {
            Build.VERSION.SDK_INT >= 26 -> Api26Impl.request(
                window, srcRect, dest,
                listener, listenerHandler
            )

            else -> Api24Impl.request(
                window, dest, listener,
                listenerHandler
            )
        }
    }

    fun request(
        window: Window, dest: Bitmap,
        listener: PixelCopy.OnPixelCopyFinishedListener,
        listenerHandler: Handler,
    ) = request(window, null, dest, listener, listenerHandler)

    @RequiresApi(26)
    internal object Api26Impl {
        fun request(
            window: Window, srcRect: Rect?, dest: Bitmap,
            listener: PixelCopy.OnPixelCopyFinishedListener,
            listenerHandler: Handler,
        ) = PixelCopy.request(window, srcRect, dest, listener, listenerHandler)
    }

    internal object Api24Impl {
        fun request(
            window: Window, dest: Bitmap,
            listener: PixelCopy.OnPixelCopyFinishedListener,
            listenerHandler: Handler,
        ) {
            val insets = Rect()
            val surface = sourceForWindow(window, insets)
            PixelCopy.request(surface, dest, listener, listenerHandler)
        }

        @SuppressLint("PrivateApi")
        private fun sourceForWindow(source: Window?, outInsets: Rect): Surface {
            requireNotNull(source) { "source is null" }
            requireNotNull(source.peekDecorView()) { "Only able to copy windows with decor views" }
            var surface: Surface? = null
            val dv = source.peekDecorView()
            val root = dv?.let {
                View::class.java.getDeclaredField("mAttachInfo").apply {
                    isAccessible = true
                }[it]?.let { attachInfo ->
                    attachInfo.javaClass.getDeclaredField("mViewRootImpl").apply {
                        isAccessible = true
                    }[attachInfo]
                }
            } // as ViewRootImpl
            if (root != null) {
                surface = root.javaClass.getDeclaredField("mSurface").apply {
                    isAccessible = true
                }[root] as Surface
                val windowAttrs = root.javaClass.getDeclaredField("mWindowAttributes").apply {
                    isAccessible = true
                }[root] // as WindowManager.LayoutParams
                val surfaceInsets = windowAttrs.javaClass.getDeclaredField("surfaceInsets").apply {
                    isAccessible = true
                }[windowAttrs] as Rect
                // val width = root.javaClass.getDeclaredField("mWidth").apply {
                //     isAccessible = true
                // }[root] as Int
                // val height = root.javaClass.getDeclaredField("mHeight").apply {
                //     isAccessible = true
                // }[root] as Int
                val width = dv.rootView.width
                val height = dv.rootView.height
                outInsets.set(
                    surfaceInsets.left, surfaceInsets.top,
                    width + surfaceInsets.left, height + surfaceInsets.top
                )
            }
            require(surface != null && surface.isValid) { "Window doesn't have a backing surface!" }
            return surface
        }
    }
}