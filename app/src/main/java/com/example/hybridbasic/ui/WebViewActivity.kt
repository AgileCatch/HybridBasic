package com.example.hybridbasic.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hybridbasic.databinding.ActivityMainBinding

class WebViewActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val fileChooser by lazy { FileChooser(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initWebView()
        initWebSetting()
    }

    private fun initWebView() = with(binding) {
        webView.apply {
            webViewClient = object : WebViewClient() {  // 클릭 시 새창 안뜨게 페이지 탬색처리
                override fun onReceivedError(   // 오류 발생 시
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    val errorMessage = when (error?.errorCode) {
                        ERROR_AUTHENTICATION -> "서버에서 사용자 인증 실패"
                        ERROR_BAD_URL -> "잘못된 URL"
                        ERROR_CONNECT -> "서버로 연결 실패"
                        ERROR_FAILED_SSL_HANDSHAKE -> "SSL handshake 수행 실패"
                        ERROR_FILE -> "일반 파일 오류"
                        ERROR_FILE_NOT_FOUND -> "파일을 찾을 수 없습니다"
                        ERROR_HOST_LOOKUP -> "서버 또는 프록시 호스트 이름 조회 실패"
                        ERROR_IO -> "서버에서 읽거나 서버로 쓰기 실패"
                        ERROR_PROXY_AUTHENTICATION -> "프록시에서 사용자 인증 실패"
                        ERROR_REDIRECT_LOOP -> "너무 많은 리디렉션"
                        ERROR_TIMEOUT -> "연결 시간 초과"
                        ERROR_TOO_MANY_REQUESTS -> "페이지 로드중 너무 많은 요청 발생"
                        ERROR_UNKNOWN -> "일반 오류"
                        ERROR_UNSUPPORTED_AUTH_SCHEME -> "지원되지 않는 인증 체계"
                        ERROR_UNSUPPORTED_SCHEME -> "URI가 지원되지 않는 방식"
                        else -> "알 수 없는 오류 발생"
                    }
                    printToast(errorMessage)
                    super.onReceivedError(view, request, error)
                }
            }
            setWebContentsDebuggingEnabled(true)    //웹에서 devtools 사용설정
            clearCache(true)

            loadUrl("https://www.fab365.net/")  //웹뷰에 표시할 웹사이트 주소

            webChromeClient=object :WebChromeClient(){
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    fileChooser.show()
                    return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
                }


            }
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebSetting() = with(binding) {
        val webSettings: WebSettings = webView.settings

        webSettings.apply {
            javaScriptEnabled = true    // 웹페이지 자바스크립트 허용 여부
            setSupportMultipleWindows(true) //멀티윈도우를 지원할지 여부
            javaScriptCanOpenWindowsAutomatically = true
            loadWithOverviewMode = true //컨텐츠가 웹뷰보다 클때 스크린 크기에 맞추기
            useWideViewPort = true  // 화면 사이즈 맞추기 허용 여부
            setSupportZoom(false)    // 화면 줌 허용 여부
            domStorageEnabled= true     //DOM 로컬 스토리지 사용여부

            allowUniversalAccessFromFileURLs = true
            databaseEnabled = true  //database storage API 사용 여부
            allowFileAccess = true  //파일 액세스 허용 여부
            allowContentAccess =true    //Content URL 에 접근 사용 여부
            textZoom = 100  // system 글꼴 크기에 의해 변하는 것 방지
            builtInZoomControls = true  // 줌 아이콘
            displayZoomControls = false

            WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING   // 컨텐츠 사이즈 자동 맞추기
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            cacheMode = WebSettings.LOAD_DEFAULT

            //        userAgentString="${userAgent}/my_blues_app"   // 에이전트 값 셋팅
            //        userAgentString = userAgentString + " " + AppConst.AGENT_STR

        }

    }

    //나중에 유틸로 분리하기
    fun printToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }

}