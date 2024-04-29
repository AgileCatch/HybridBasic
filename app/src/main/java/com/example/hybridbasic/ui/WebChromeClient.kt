package com.example.hybridbasic.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
    private val activity: WebViewActivity
) : WebChromeClient() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var legacyFilePathCallback: ValueCallback<Uri>? = null
    private val fileChooserHelper = FileChooserHelper(context)

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
        TedPermission.create()
            .setPermissions(*UPLOAD_PERMISSIONS)
            .setDeniedMessage("사진과 카메라의 기능 사용을 위해서는 다음 권한에 대한 동의가 필요합니다.")
            .setGotoSettingButtonText("권한 설정하기")
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    galleryHelper.startImageCapture()
                    galleryHelper.getImage()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    clearFileChooserCallback()
                }
            }).check()
    }

    private fun checkPermission() {
        // 권한 없을경우, 있을경우 분기처리
        if (ContextCompat.checkSelfPermission(
                context as WebViewActivity,
                arrayOf(UPLOAD_PERMISSIONS).toString()
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    arrayOf(UPLOAD_PERMISSIONS).toString()
                )
            ) {
                //권한 없을때 사용자가 권한을 거부 한 적이 있을 때 설명 필요
                Log.d(TAG, "권한없음. 메세지 띄움")
//                printToast("사진과 카메라의 기능 사용을 위해서는 다음 권한에 대한 동의가 필요합니다.")
//                clearFileChooserCallback()
//                fileChooserHelper.show()
            } else {
                Log.d(TAG, "권한없음. 메세지 안띄움")
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(arrayOf(UPLOAD_PERMISSIONS).toString()),
                    PERMISSION_REQUEST
                )
//                clearFileChooserCallback()
            }
        } else {
            Log.d(TAG, "권한 있음. permission연동")
            printToast("권한이 허용되었습니다.")
            galleryHelper.startImageCapture()
            galleryHelper.getImage()
        }
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
        const val PERMISSION_REQUEST = 123
    }


    private fun printToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


}
