package com.example.hybridbasic.ui

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.hybridbasic.ui.helper.GalleryHelper


class WebChromeClient(
    private val context: Context,
    private val galleryHelper: GalleryHelper,
) : WebChromeClient() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var legacyFilePathCallback: ValueCallback<Uri>? = null

    init {
        galleryHelper.listener = object : GalleryHelper.OnGalleryListener {
            override fun onSuccess(uri: Uri?) {
                @Suppress("NAME_SHADOWING") val uri = uri ?: return
                filePathCallback?.onReceiveValue(arrayOf(uri))
                legacyFilePathCallback?.onReceiveValue(uri)
                filePathCallback = null
                legacyFilePathCallback = null
            }

            override fun onFail() {
                clearFileChooserCallback()
            }
        }
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        Log.d(TAG, "onShowFileChooser called!!!")

        clearFileChooserCallback()
        this@WebChromeClient.filePathCallback = filePathCallback

        showPermission()
        return true
    }

    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        Log.d(TAG, "onJsConfirm called!!!")
        Log.d(TAG, "url : $url, message : $message")
        return super.onJsConfirm(view, url, message, result)
    }

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        Log.d(TAG, "onJsConfirm called!!!")
        Log.d(TAG, "url : $url, message : $message")
        return super.onJsAlert(view, url, message, result)
    }

    private fun clearFileChooserCallback() {
        filePathCallback?.onReceiveValue(null)
        legacyFilePathCallback?.onReceiveValue(null)
        filePathCallback = null
        legacyFilePathCallback = null
    }


    private fun showPermission() {
        galleryHelper.startImageCapture()
        clearFileChooserCallback()
    }

    companion object {
        private val UPLOAD_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.CAMERA
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }

        private val TAG = WebChromeClient::class.java.simpleName
    }
}
