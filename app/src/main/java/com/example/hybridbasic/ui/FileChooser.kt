package com.example.hybridbasic.ui

import android.content.Context
import android.content.Intent

class FileChooser(private val context: Context) {
    fun show() {
        val chooserIntent = createChooserIntent()
        context.startActivity(chooserIntent)
    }

    private fun createChooserIntent(): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            val mimeTypes = arrayOf("image/*", "application/pdf")
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        return Intent.createChooser(intent, "첨부파일 선택")
    }
}