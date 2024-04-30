package com.example.hybridbasic.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Message
import android.util.Log
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hybridbasic.ui.helper.FileChooserHelper
import com.example.hybridbasic.ui.helper.GalleryHelper
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class WebChromeClient(
    private val context: Context,
    private val galleryHelper: GalleryHelper,
) : WebChromeClient() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var legacyFilePathCallback: ValueCallback<Uri>? = null
//    private val fileChooserHelper = FileChooserHelper(context)

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

//    override fun onCreateWindow(
//        view: WebView?,
//        isDialog: Boolean,
//        isUserGesture: Boolean,
//        resultMsg: Message?
//    ): Boolean {
//        return webViewController.createWindow(view, resultMsg)
//    }
//
//    override fun onCloseWindow(window: WebView?) {
//        Log.d(TAG, "onCloseWindow called!!!")
//        webViewController.closeWindow()
//    }


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
        TedPermission.create()
            //setPermissions 키워드로 권한 설정확인
            .setPermissions(*UPLOAD_PERMISSIONS)
            .setDeniedMessage("사진과 카메라의 기능이 필요한 서비스입니다.")
            .setGotoSettingButtonText("권한 설정하기")
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    //이미 권한이 있거나 사용자가 권한을 허용했을 때 호출
//                    galleryHelper.startImageCapture()
                    galleryHelper.getImage()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    //요청이 거부 되었을 때 호출
                    clearFileChooserCallback()
                }
            }).check()
    }


    companion object {
        private val UPLOAD_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                )
            } else {
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }

        private val TAG = WebChromeClient::class.java.simpleName
        const val PERMISSION_REQUEST = 123
    }


    private fun printToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


}
