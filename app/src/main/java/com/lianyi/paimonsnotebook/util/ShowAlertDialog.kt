package com.lianyi.paimonsnotebook.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter

private fun setAlertDialogTransparentBackground(win: AlertDialog){
    win.window?.setBackgroundDrawableResource(R.color.transparent)
    win.window?.decorView?.setPadding(10.dp.toInt(),0,10.dp.toInt(),0)
    win.window?.setLayout(
        PaiMonsNoteBook.context.resources.displayMetrics.widthPixels-20.dp.toInt(),
        ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun showAlertDialog(context: Context, layout: View): AlertDialog {
    val win = AlertDialog.Builder(context).setView(layout).create()

    setAlertDialogTransparentBackground(win)
    win.show()
    return win
}

fun showAlertDialog(context: Context, id:Int): AlertDialog {
    val layout = LayoutInflater.from(context).inflate(id,null)
    val win = AlertDialog.Builder(context).setView(layout).create()

    setAlertDialogTransparentBackground(win)
    win.show()
    return win
}

fun showConfirmAlertDialog(context: Context, title:String="提示", content:String="你确定吗", block: (isConfirm:Boolean) -> Unit){
    val layout = PopConfirmBinding.bind(LayoutInflater.from(context).inflate(R.layout.pop_confirm,null))
    val win = showAlertDialog(context,layout.root)

    layout.title.text = title
    layout.content.text = content

    layout.confirm.setOnClickListener {
        block(true)
        win.dismiss()
    }

    layout.cancel.setOnClickListener {
        block(false)
        win.dismiss()
    }
}

fun showSuccessInformationAlertDialog(context: Context, title:String="成功",message: String=""){
    val layout = PopSuccessBinding.bind(LayoutInflater.from(context).inflate(R.layout.pop_success,null))
    val win = AlertDialog.Builder(context).setView(layout.root).create()

    layout.close.setOnClickListener {
        win.dismiss()
    }

    layout.title.text = title
    layout.message.text = message

    setAlertDialogTransparentBackground(win)
    win.show()
}

fun showFailureAlertDialog(context: Context, title: String="失败", message:String=""): AlertDialog {
    val layout = PopFailureBinding.bind(LayoutInflater.from(context).inflate(R.layout.pop_failure,null))
    val win = AlertDialog.Builder(context).setView(layout.root).create()

    layout.title.text = title
    layout.message.text = message

    layout.close.setOnClickListener {
        win.dismiss()
    }
    setAlertDialogTransparentBackground(win)
    win.show()
    return win
}

fun showListAlertDialog(content: Context, list:List<String>, title: String, block: (String, Int) -> Unit){
    val layout = PopListBinding.bind(LayoutInflater.from(PaiMonsNoteBook.context).inflate(R.layout.pop_list,null))
    val win = AlertDialog.Builder(content).setView(layout.root).create()

    layout.list.adapter = ReAdapter(list, R.layout.item_text_black){
            view, s, position ->
        val item = ItemTextBlackBinding.bind(view)
        item.text.text = s
        item.root.setOnClickListener {
            block(s,position)
            win.dismiss()
        }
    }
    layout.close.setOnClickListener {
        win.dismiss()
    }
    layout.title.text = title
    setAlertDialogTransparentBackground(win)
    win.show()
}

fun showInputAlertDialog(content: Context, title: String, hint:String, block: (String) -> Unit){
    val layout = PopInputBinding.bind(LayoutInflater.from(content).inflate(R.layout.pop_input,null))
    val win = AlertDialog.Builder(content).setView(layout.root).create()

    layout.cancel.setOnClickListener {
        win.dismiss()
    }

    layout.confirm.setOnClickListener {
        block(layout.input.text.toString())
        win.dismiss()
    }

    layout.title.text = title
    layout.inputLayout.hint = hint

    setAlertDialogTransparentBackground(win)
    win.show()
}
