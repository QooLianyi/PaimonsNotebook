package com.lianyi.paimonsnotebook.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.gacha.GachaHistoryListBean
import com.lianyi.paimonsnotebook.bean.gacha.GachaLogBean
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.databinding.FragmentWishBinding
import com.lianyi.paimonsnotebook.databinding.ItemWishHomeBinding
import com.lianyi.paimonsnotebook.databinding.PagerListBinding
import com.lianyi.paimonsnotebook.databinding.PopEditUrlBinding
import com.lianyi.paimonsnotebook.lib.adapter.PagerAdapter
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.ui.activity.LoadingGachaWishHistoryActivity
import com.lianyi.paimonsnotebook.util.*
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFRichTextString
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import kotlin.concurrent.thread

class WishFragment : BaseFragment(R.layout.fragment_wish){

    lateinit var bind:FragmentWishBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentWishBinding.bind(view)

        initView()
    }

    private fun refreshPage() {
        val pages = listOf(
            LayoutInflater.from(bind.root.context).inflate(R.layout.pager_list,null),
            LayoutInflater.from(bind.root.context).inflate(R.layout.pager_list,null),
            LayoutInflater.from(bind.root.context).inflate(R.layout.pager_list,null),
            LayoutInflater.from(bind.root.context).inflate(R.layout.pager_list,null)
        )
        val titles = listOf("总览","角色","武器","分析")

        bind.wishViewPager.adapter = PagerAdapter(pages,titles)
        bind.wishViewPager.offscreenPageLimit = 4
        bind.wishTabLayout.setupWithViewPager(bind.wishViewPager)

        val page = PagerListBinding.bind(pages.first())
        val gachaList = mutableListOf<List<GachaLogBean.ListBean>>()
        val history = GSON.fromJson(bind.root.context.getSharedPreferences(Constants.GSP_NAME+ mainUser!!.gameUid,Context.MODE_PRIVATE).getString(
            mainUser!!.gameUid,"")!!,GachaHistoryListBean::class.java)
        history.list.forEach {
            if(it.isNotEmpty()){
                gachaList+=it
            }
        }
        page.list.adapter = ReAdapter(gachaList,R.layout.item_wish_home){
            view: View, list: List<GachaLogBean.ListBean>, i: Int ->
            val item = ItemWishHomeBinding.bind(view)
            item.gachaName.text = GachaType.getNameByType(list.first().gacha_type.toInt())
            val star = list.groupBy { it.rank_type }
            val count = list.size.toFloat()
            val star3Count = star["3"]!!.size
            val star4Count = star["4"]!!.size
            val star5Count = star["5"]!!.size

            var star4WeaponCount = 0
            var star4CharacterCount = 0

            var star5WeaponCount = 0
            var star5CharacterCount = 0

            star["4"]?.forEach {
                when(it.item_type){
                    "武器"-> star4WeaponCount++
                    "角色"-> star4CharacterCount++
                }
            }

            star["5"]?.forEach {
                when(it.item_type){
                    "武器"-> star5WeaponCount++
                    "角色"-> star5CharacterCount++
                }
            }

            item.star3Count.text = star3Count.toString()
            item.star4Count.text = star4Count.toString()
            item.star5Count.text = star5Count.toString()

            item.gachaCount.text = count.toInt().toString()
            item.star3Percent.text =  Format.DECIMALS_FORMAT.format(star3Count / count * 100)
            item.star4Percent.text =  Format.DECIMALS_FORMAT.format(star4Count / count * 100)
            item.star5Percent.text =  Format.DECIMALS_FORMAT.format(star5Count / count * 100)

            item.avgGet5StarCount.text = Format.DECIMALS_FORMAT.format(count / star5Count)

            item.time.text = "${list.first().time} ~ ${list.last().time}"

            val historyBuilder = StringBuffer()
            historyBuilder.append("五星历史记录:")
            var gachaCount = 0
            list.reversed()
            list.forEach {
                when(it.rank_type){
                    "5"->{
                        historyBuilder.append("<font color='${Constants.colorList.random()}'>${it.name}[$gachaCount]&nbsp;&nbsp</font>")
                        gachaCount = 0
                    }
                    else -> gachaCount++
                }
            }

            item.history.text = Html.fromHtml(historyBuilder.toString(),Html.FROM_HTML_MODE_LEGACY)

            item.pie.apply {
                description.isEnabled = false
                isDrawHoleEnabled = false
                isRotationEnabled = false
                isClickable = false
                legend.isEnabled = false

                val pieDataEntry = mutableListOf<PieEntry>()
                val mColors = mutableListOf<Int>()

                val tags = mutableListOf<String>()
                if(star5CharacterCount>0) {
                    pieDataEntry += PieEntry(star5CharacterCount.toFloat(),"")
                    mColors += Constants.GACHA_HISTORY_5_STAR_CHARACTER_COLOR
                    tags+="五星角色"
                }
                if(star5WeaponCount>0) {
                    pieDataEntry += PieEntry(star5WeaponCount.toFloat(),"")
                    tags+="五星武器"
                    mColors+=Constants.GACHA_HISTORY_5_STAR_WEAPON_COLOR
                }
                if(star4CharacterCount>0) {
                    pieDataEntry += PieEntry(star4CharacterCount.toFloat(),"")
                    tags+="四星角色"
                    mColors += Constants.GACHA_HISTORY_4_STAR_CHARACTER_COLOR
                }
                if(star4WeaponCount>0){
                    pieDataEntry += PieEntry(star4WeaponCount.toFloat(),"")
                    tags+="四星武器"
                    mColors+=Constants.GACHA_HISTORY_4_STAR_WEAPON_COLOR
                }

                val pieDataSet = PieDataSet(pieDataEntry,"").apply {
                    colors = mColors

                    valueTextSize = 16f

                    setValueTextColors(mColors)
                    valueLineWidth = 2f
                    valueLinePart1OffsetPercentage = 100f

                    yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                    xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

                    valueLinePart1Length = 0.8f
                    valueLinePart2Length = 0.6f
                    isUsingSliceColorAsValueLineColor = true

                    selectionShift = 20f

                    var index = 0
                    valueFormatter = object :ValueFormatter(){
                        override fun getFormattedValue(value: Float): String {
                            if(index>=tags.size) index = 0
                            return tags[index++]
                        }
                    }
                }
                data = PieData(pieDataSet)
            }
        }
    }

    private fun initView() {
        bind.selectAccountSpan.setOnClickListener {
            bind.selectAccount.performClick()
        }

        bind.wishMenu.setOnClickListener {

        }

        val array = arrayOf("123456")
        bind.selectAccount.adapter = ArrayAdapter(bind.root.context,R.layout.item_text,array).apply {
            setDropDownViewResource(R.layout.spinner_drop_down_style)
        }

        bind.wishTabLayout.setupWithViewPager(bind.wishViewPager)


        if(bind.root.context.getSharedPreferences(Constants.GSP_NAME+ mainUser!!.gameUid,Context.MODE_PRIVATE).getString(
                mainUser!!.gameUid,"").isNullOrEmpty()){
            bind.noDataSpan.show()
            onNoData()
        }else{
            refreshPage()
        }

    }

    private fun onNoData() {
        bind.noDataSpan.show()
        bind.editUrl.setOnClickListener {
            val layout = PopEditUrlBinding.bind(LayoutInflater.from(bind.root.context).inflate(R.layout.pop_edit_url,null))
            val win = showAlertDialog(bind.root.context,layout.root)

            layout.confirm.setOnClickListener {
                val input = layout.input.text.toString().trim()
                checkUrl(input){
                    activity?.runOnUiThread {
                        if(it){
                            LoadingGachaWishHistoryActivity.logUrl = input
                            startActivityForResult(Intent(bind.root.context,LoadingGachaWishHistoryActivity::class.java),
                                ActivityRequestCode.GACHA_HISTORY)
                        }else{
                            "输入的URL不正确,请检查后再次输入吧!".show()
                        }
                    }
                }
                win.dismiss()
            }

            layout.cancel.setOnClickListener {
                win.dismiss()
            }
        }
    }

    private fun checkUrl(url:String,block:(Boolean)->Unit){
        if(url.takeLast(5)!="#/log") {
            block(false)
            return
        }
        Ok.get(MiHoYoApi.getGachaLogUrl(url, GachaType.PERMANENT,1,6,"0")){
            block(it.ok)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== ActivityResponseCode.OK&&requestCode==ActivityRequestCode.GACHA_HISTORY){
            thread {
                val sheetData = listOf<List<GachaLogBean.ListBean>>(
                    LoadingGachaWishHistoryActivity.newPlayerGachaHistory,
                    LoadingGachaWishHistoryActivity.permanentGachaHistory,
                    LoadingGachaWishHistoryActivity.characterGachaHistory,
                    LoadingGachaWishHistoryActivity.weaponGachaHistory
                )
                val table =XSSFWorkbook()
                val headerRowFontStyle = table.createFont()
                val headerRowsStyle = table.createCellStyle()
                headerRowFontStyle.fontHeightInPoints = 10.toShort()
                headerRowFontStyle.boldweight = XSSFFont.BOLDWEIGHT_BOLD
                headerRowsStyle.setFont(headerRowFontStyle)

                    sheetData.forEach {
                    if(it.isNotEmpty()){
                        val sheet = table.createSheet(GachaType.getNameByType(it.first().gacha_type.toInt()))

                        sheet.setColumnWidth(0,256*19+184)
                        sheet.setColumnWidth(1,256*16+184)
                        sheet.setColumnWidth(2,256*10+184)
                        sheet.setColumnWidth(3,256*5+184)
                        sheet.setColumnWidth(4,256*16+184)

                        val headerRow = sheet.createRow(0)
                        headerRow.heightInPoints = 22f
                        headerRow.createCell(0).apply {
                            setCellValue(XSSFRichTextString("时间").apply {
                                applyFont(headerRowFontStyle)
                            })
                        }
                        headerRow.createCell(1).apply {
                            setCellValue(XSSFRichTextString("名称").apply {
                                applyFont(headerRowFontStyle)
                            })
                        }
                        headerRow.createCell(2).apply {
                            setCellValue(XSSFRichTextString("物品类型").apply {
                                applyFont(headerRowFontStyle)
                            })
                        }
                        headerRow.createCell(3).apply {
                            setCellValue(XSSFRichTextString("星级").apply {
                                applyFont(headerRowFontStyle)
                            })
                        }
                        headerRow.createCell(4).apply {
                            setCellValue(XSSFRichTextString("祈愿类型").apply {
                                applyFont(headerRowFontStyle)
                            })
                        }

                        it.forEachIndexed {index, listBean ->
                            val row = sheet.createRow(index+1)
                            row.heightInPoints = 20f
                            row.createCell(0).setCellValue(listBean.time)
                            row.createCell(1).setCellValue(listBean.name)
                            row.createCell(2).setCellValue(listBean.item_type)
                            row.createCell(3).setCellValue(listBean.rank_type)
                            row.createCell(4).setCellValue(GachaType.getNameByType(listBean.gacha_type.toInt()))
                        }
                    }
                }

                FileUtil.writeTable(table,System.currentTimeMillis().toString())
                val history = GachaHistoryListBean(sheetData)
                val gsp = PaiMonsNoteBook.context.getSharedPreferences(Constants.GSP_NAME+"106981262",Context.MODE_PRIVATE)
                val edit = gsp.edit()
                edit.putString("106981262", GSON.toJson(history))
                edit.apply()
            }
        }
    }
}