package com.example.hybridbasic.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
object DateFormatUtils {

    /**
     * Dau
     */
    fun yyyyMMddFormat(time: Long): String {
        val sdf = SimpleDateFormat("yyyyMMdd")
        val date = Date().apply {
            this.time = time
        }
        return sdf.format(date)
    }

    /**
     * Mau
     */
    fun yyyyMMFormat(time: Long): String {
        val sdf = SimpleDateFormat("yyyyMM")
        val date = Date().apply {
            this.time = time
        }
        return sdf.format(date)
    }

    /**
     * Wau
     */
    fun weekFormat(time: Long): String {
        val sdf = SimpleDateFormat("w")
        val date = Date().apply {
            this.time = time
        }
        return sdf.format(date)
    }
}