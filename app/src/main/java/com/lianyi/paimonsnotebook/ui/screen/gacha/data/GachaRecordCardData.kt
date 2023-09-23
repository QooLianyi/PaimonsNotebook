package com.lianyi.paimonsnotebook.ui.screen.gacha.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.common.components.charts.pie_charts.data.PieChartData
import com.lianyi.paimonsnotebook.ui.screen.gacha.util.GachaRecordCardDisplayState

data class GachaRecordCardData(
    val data:List<PieChartData>,
    val title:String,
    val time:String,
    val star4Progress:Float,
    val star4ProgressCount:Int,
    val star5Progress:Float,
    val star5ProgressCount:Int,
    val star5Count:Int,
    val star4Count:Int,
    val totalCount:Int,
    val star5List:List<GachaRecordStar5Data>,
    val legend: List<ChartLegendData>,
    val uigfGachaType:String
){
    var cardDisplayState by mutableStateOf(GachaRecordCardDisplayState.None)
}