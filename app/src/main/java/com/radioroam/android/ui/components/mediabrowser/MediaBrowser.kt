package com.radioroam.android.ui.components.mediabrowser

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.radioroam.android.ui.service.PlaybackService

@Composable
fun rememberManagedMediaBrowser(
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle
): State<MediaBrowser?> {
    // Application context is used to prevent memory leaks
    val appContext = LocalContext.current.applicationContext
    val browserManager = remember { MediaBrowserManager.getInstance(appContext) }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> browserManager.initialize()
                Lifecycle.Event.ON_STOP -> browserManager.release()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return browserManager.browser
}

@Stable
internal class MediaBrowserManager private constructor(context: Context) : RememberObserver {
    private val appContext = context.applicationContext
    private var factory: ListenableFuture<MediaBrowser>? = null
    var browser = mutableStateOf<MediaBrowser?>(null)
        private set

    init {
        setupFactory()
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun setupFactory() {
        if (factory == null || factory?.isDone == true) {
            factory = MediaBrowser.Builder(
                appContext,
                SessionToken(appContext, ComponentName(appContext, PlaybackService::class.java))
            ).buildAsync()
        }
    }

    internal fun initialize() {
        setupFactory()
        factory?.addListener(
            { setupBrowser() },
            MoreExecutors.directExecutor()
        )
    }

    internal fun release() {
        factory?.let {
            MediaBrowser.releaseFuture(it)
            updateBrowserState()
        }
        factory = null
    }

    private fun setupBrowser() {
        updateBrowserState()
    }

    private fun updateBrowserState() {
        browser.value = factory?.let {
            if (it.isDone) it.get() else null
        } ?: run {
            null
        }
    }

    override fun onAbandoned() { release() }
    override fun onForgotten() { release() }
    override fun onRemembered() {}

    companion object {
        @Volatile
        private var instance: MediaBrowserManager? = null

        fun getInstance(context: Context): MediaBrowserManager {
            return instance ?: synchronized(this) {
                instance ?: MediaBrowserManager(context).also { instance = it }
            }
        }
    }
}