package com.tang.andfixdemo

import android.app.Application
import com.alipay.euler.andfix.patch.PatchManager
import java.io.File

/**
 * CREATE BY Tanghongtu 2020/6/27
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //Initialize PatchManager,
        val patchManager = PatchManager(this)
        patchManager.init("1.0");//current version

        //Load patch,
        patchManager.loadPatch()

        //Add patch,
        val file = File(getExternalFilesDir(null), "fix.apatch")
        if (file.exists()) {
            patchManager.addPatch(file.absolutePath)
        }
    }

}