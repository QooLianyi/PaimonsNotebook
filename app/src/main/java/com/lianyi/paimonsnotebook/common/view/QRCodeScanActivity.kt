package com.lianyi.paimonsnotebook.common.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.google.zxing.Result
import com.king.camera.scan.AnalyzeResult
import com.king.camera.scan.CameraScan
import com.king.camera.scan.analyze.Analyzer
import com.king.zxing.BarcodeCameraScanActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.QRCodeAnalyzer
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.activity.setImmersionMode
import com.lianyi.paimonsnotebook.common.extension.intent.setResultCode
import com.lianyi.paimonsnotebook.common.util.activity_code.ActivityCode

/*
* 二维码扫描界面
* */
class QRCodeScanActivity : BarcodeCameraScanActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setImmersionMode()
    }

    override fun createAnalyzer(): Analyzer<Result> {
        // 初始化解码配置
        val decodeConfig = DecodeConfig().apply {
            hints =
                DecodeFormatManager.QR_CODE_HINTS //如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
            isFullAreaScan = false //设置是否全区域识别，默认false
            areaRectRatio = 0.8f //设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
            areaRectVerticalOffset = 0
            areaRectHorizontalOffset = 0 //设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数
        }

        // BarcodeCameraScanActivity默认使用的MultiFormatAnalyzer，这里可以改为使用QRCodeAnalyzer
        return QRCodeAnalyzer(decodeConfig)
    }

    override fun getLayoutId(): Int = R.layout.activity_qrcode_scan

    override fun onScanResultCallback(result: AnalyzeResult<Result>) {
        val intent = Intent().apply {
            putExtra(CameraScan.SCAN_RESULT, result.result.text)
            setResultCode(ActivityCode.QRCodeScanActivity)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}