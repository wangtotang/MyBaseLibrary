package com.tang.webapp.demo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import kotlinx.android.synthetic.main.dialog_bottom.*

/**
 * Created by tanghongtu on 2020/7/3.
 */

class BottomDialog : Dialog {

    constructor(context: Context) : this(context, R.style.BottomDialog)

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        setContentView(R.layout.dialog_bottom)
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        btnCancel.setOnClickListener {
            dismiss()
        }
        btnDial.setOnClickListener {
            dismiss()
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.data = Uri.parse("tel:"+ btnPhoneNumber.text)
            context.startActivity(intent)
        }
    }

    fun setPhoneNumber(phoneNumber: String) {
        btnPhoneNumber.text = phoneNumber
    }
}