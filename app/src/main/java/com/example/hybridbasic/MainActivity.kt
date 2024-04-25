package com.example.hybridbasic

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hybridbasic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() = with(binding) {
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
                    errorText.visibility = View.VISIBLE //on
                    webView.visibility = View.GONE  //off
                }
            }
            setWebContentsDebuggingEnabled(true)    //웹에서 devtools 사용설정
            loadUrl("https://www.fab365.net/")  //웹뷰에 표시할 웹사이트 주소
        }

        val webSettings: WebSettings = webView.settings
        webSettings.apply {
            javaScriptEnabled = true    // 웹페이지 자바스크립트 허용 여부
            useWideViewPort  = true // 화면 사이즈 맞추기 허용 여부
            domStorageEnabled  = true  //데이터 저장 DOM 스토리지
            setSupportZoom(false)   // 화면 줌 허용 여부
            textZoom = 100  // system 글꼴 크기에 의해 변하는 것 방지
            WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING   // 컨텐츠 사이즈 자동 맞추기

//            userAgentString="${userAgent}/my_blues_app"   // 에이전트 값 셋팅

        }
    }


    fun printToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()

    }
}