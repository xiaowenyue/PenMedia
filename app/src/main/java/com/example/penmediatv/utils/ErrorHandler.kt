package com.example.penmediatv.utils
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.penmediatv.R
import java.io.IOException

object ErrorHandler {
    fun handleFailure(t: Throwable, context: Context, className: String) {
        if (t is IOException) {
            Log.e(className, "网络错误或服务器不可达: ${t.message}")
        } else {
            Log.e(className, "未知错误: ${t.message}")
        }
        showDialog(context, R.layout.dialog_network_dismiss)
    }

    fun handleUnsuccessfulResponse(context: Context, className: String) {
        Log.e(className, "服务器返回非成功状态码")
        showDialog(context, R.layout.dialog_network_disconnect)
    }

    private fun showDialog(context: Context, layoutRes: Int) {
        val dialog = Dialog(context)
        dialog.setContentView(layoutRes)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 2000)
    }
}

