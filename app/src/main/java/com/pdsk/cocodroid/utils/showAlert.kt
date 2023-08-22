package com.pdsk.cocodroid.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun showAlert(context: Context, message:String){
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setPositiveButton("OK"){dialog,_ ->
        dialog.dismiss()
    }
    val alertDialog = builder.create()
    alertDialog.show()
}