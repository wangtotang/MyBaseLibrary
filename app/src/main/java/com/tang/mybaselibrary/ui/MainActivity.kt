package com.tang.mybaselibrary.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.tang.mybaselibrary.R
import com.tang.mybaselibrary.util.getFilePath
import com.tang.mybaselibrary.util.getUriFromFile
import com.tang.mybaselibrary.util.install
import com.tang.smartupdate.PatchUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = ProgressDialog(this)
//        btnUpdate.setOnClickListener {
//            updateApk()
//        }
    }

    private fun updateApk() {
        dialog.show()
        //路径
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        val oldApkPath: String? = applicationInfo.sourceDir
        val newApkFile = File(getFilePath(), "new.apk")
        // TODO 根据版本使用网络下载到指定路径
        val patchFile = File(getFilePath(), "patch.apk")
        Thread(Runnable {
            val code = PatchUtil.patch(oldApkPath!!, newApkFile.absolutePath, patchFile.absolutePath)
            runOnUiThread {
                dialog.dismiss()
                if (code == 0) {
                    //Toast.makeText(this, "打补丁成功", Toast.LENGTH_LONG).show()
                    install(getUriFromFile(newApkFile)!!)
                } else {
                    Toast.makeText(this, "打补丁失败", Toast.LENGTH_LONG).show()
                }
            }

        }).start()

    }

}