package com.tang.mybaselibrary.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.tang.mybaselibrary.ActivityExt
import com.tang.mybaselibrary.FloatingVideoService
import com.tang.mybaselibrary.R
import com.tang.mybaselibrary.util.getFilePath
import com.tang.mybaselibrary.util.getUriFromFile
import com.tang.mybaselibrary.util.install
import com.tang.smartupdate.PatchUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var dialog: ProgressDialog
    private lateinit var contentParent: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = ProgressDialog(this)
//        btnUpdate.setOnClickListener {
//            updateApk()
//        }
        //只要在setContentView之后往contentView添加内容即可
        contentParent = findViewById(android.R.id.content)
        val textView = TextView(this)
        textView.text = "Hello World!"
        ActivityExt(this).addContentView(textView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        btn_play.setOnClickListener {
            startFloatingVideoService()
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: ")
    }

    //没有用的方法
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.d(TAG, "onSaveInstanceState: ")
    }

    fun startFloatingVideoService() {
        if (FloatingVideoService.isStarted) {
            return
        }
        if (!canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT)
            startActivityForResult(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                ), 1
            )
        } else {
            startService(Intent(this@MainActivity, FloatingVideoService::class.java))
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!canDrawOverlays(this)) {
            Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
            startService(Intent(this@MainActivity, FloatingVideoService::class.java))
        }
    }

    fun canDrawOverlays(context: Context): Boolean {
        return when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> true
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                Settings.canDrawOverlays(context)
            }
            else -> {
                @RequiresApi(Build.VERSION_CODES.M)
                if (Settings.canDrawOverlays(context)) return true
                try {
                    val mgr =
                        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                            ?: return false
                    //getSystemService might return null
                    val viewToAdd = View(context)
                    val params = WindowManager.LayoutParams(
                        0,
                        0,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSPARENT
                    )
                    viewToAdd.layoutParams = params
                    mgr.addView(viewToAdd, params)
                    mgr.removeView(viewToAdd)
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                false
            }
        }
    }

}