package com.tang.mybaselibrary.util

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Created by tanghongtu on 2020/6/24.
 */
fun Context.install(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.addCategory("android.intent.category.DEFAULT")
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}