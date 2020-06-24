package com.tang.mybaselibrary.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

/**
 * Created by tanghongtu on 2020/6/24.
 */
fun Context.getFilePath(): String? {
    var filePath: String? = null
    if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        || !Environment.isExternalStorageRemovable()) {
        filePath = getExternalFilesDir(null)?.path
    } else {
        filePath = filesDir.path
    }
    return filePath
}

fun Context.getCachePath(): String? {
    var cachePath: String? = null
    if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        || !Environment.isExternalStorageRemovable()) {
        cachePath = externalCacheDir?.path
    } else {
        cachePath = cacheDir.path
    }
    return cachePath
}

fun Context.getUriFromFile(file: File): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(
            this,
            packageName + ".fileProvider",
            file
        )
    } else {
        Uri.fromFile(file)
    }
}
