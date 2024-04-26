package com.example.hybridbasic.ui

import android.content.Context
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import com.example.hybridbasic.R

class WebChromeClient(private val context:Context) : WebChromeClient() {
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        Toast.makeText(context, "파일 선택",Toast.LENGTH_SHORT).show()
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }

}