package com.github.jing332.tts_server_android.ui.custom.widget

import android.app.Dialog
import android.content.Context
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.databinding.DialogWaitBinding

class WaitDialog(context: Context) : Dialog(context) {
    private val binding = DialogWaitBinding.inflate(layoutInflater)

    init {
        setCanceledOnTouchOutside(false)
        setContentView(binding.root)

        window?.let {
            it.setWindowAnimations(R.style.dialogFadeStyle)
            it.attributes.alpha = 0.5F
        }

    }
}