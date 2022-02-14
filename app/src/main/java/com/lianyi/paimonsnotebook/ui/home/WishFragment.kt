package com.lianyi.paimonsnotebook.ui.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.bean.gacha.UIGFExcelBean
import com.lianyi.paimonsnotebook.bean.gacha.UIGFJSON
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.adapter.PagerAdapter
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.excel.SheetHandler
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.ui.activity.LoadingGachaWishHistoryActivity
import com.lianyi.paimonsnotebook.util.*
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.openxml4j.opc.PackageAccess
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable
import org.apache.poi.xssf.eventusermodel.XSSFReader
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFRichTextString
import org.json.JSONArray
import org.xml.sax.InputSource
import org.xml.sax.helpers.XMLReaderFactory
import java.nio.charset.Charset
import kotlin.concurrent.thread

class WishFragment : BaseFragment(R.layout.fragment_wish) {
    lateinit var bind: FragmentWishBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentWishBinding.bind(view)

        initView()
        checkData()
    }

    private val gachaHistoryUidList = mutableListOf<String>()
    private val pages = mutableListOf<View>()
    private val gachaData = mutableListOf<UIGFExcelBean>()
    private val summarizePageData = mutableListOf<Pair<String,List<UIGFExcelBean>>>()
    private val characterPageData = mutableListOf<Pair<String,List<UIGFExcelBean>>>()
    private val weaponPageData = mutableListOf<Pair<String,List<UIGFExcelBean>>>()

    private fun initView() {
        val titles = listOf("总览", "角色", "武器")
        pages += LayoutInflater.from(bind.root.context).inflate(R.layout.pager_list, null)
        pages += LayoutInflater.from(bind.root.context).inflate(R.layout.pager_list, null)
        pages += LayoutInflater.from(bind.root.context).inflate(R.layout.pager_list, null)

        bind.wishViewPager.adapter = PagerAdapter(pages, titles)
        bind.wishViewPager.offscreenPageLimit = 4
        bind.wishTabLayout.setupWithViewPager(bind.wishViewPager)

        bind.selectAccountSpan.setOnClickListener {
            bind.selectAccount.performClick()
        }

        JSONArray(sp.getString(JsonCacheName.GACHA_HISTORY_ACCOUNT_LIST, "[]")).toList(
            gachaHistoryUidList
        )

        bind.selectAccount.adapter = ArrayAdapter(bind.root.context, R.layout.item_text, gachaHistoryUidList).apply {
            setDropDownViewResource(R.layout.spinner_drop_down_style)
        }

        bind.selectAccount.select { position, id ->
            gachaData.clear()
            PaiMonsNotebookDataBase.INSTANCE.getGachaHistoryForExcel(gachaHistoryUidList[bind.selectAccount.selectedItemPosition],gachaData)
            refreshList()
        }

        bind.wishMenu.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.wish_menu, popupMenu.menu)
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener {
                when (it) {
                    popupMenu.menu.getItem(0) -> {
//                        val gachaHistoryAccountList = mutableListOf<String>()
//                        JSONArray(
//                            sp.getString(
//                                JsonCacheName.GACHA_HISTORY_ACCOUNT_LIST,
//                                "[]"
//                            )
//                        ).toList(gachaHistoryAccountList)
//
//                        while (gachaHistoryAccountList.size != 1) {
//                            gachaHistoryAccountList.removeLast()
//                        }
//
//                        sp.edit().apply {
//                            putString(
//                                JsonCacheName.GACHA_HISTORY_ACCOUNT_LIST,
//                                GSON.toJson(gachaHistoryAccountList)
//                            )
//                            apply()
//                        }
                        "将要完成的功能".show()
                    }
                    popupMenu.menu.getItem(1) -> {
                        showUrlEditDialog()
                    }
                    popupMenu.menu.getItem(2) -> {
                        val layout = PopUigfOptionBinding.bind(layoutInflater.inflate(R.layout.pop_uigf_option,null))
                        val win = showAlertDialog(bind.root.context,layout.root)

                        layout.uigfVersion.text = PaiMonsNoteBook.UIGF_VERSION

                        layout.importExcel.setOnClickListener {
                            selectFile(ActivityRequestCode.DATA_MANAGER_UIGF_EXCEL)
                            win.dismiss()
                        }

                        layout.exportExcel.setOnClickListener {
                            exportUIGFExcel(gachaHistoryUidList[bind.selectAccount.selectedItemPosition])
                            win.dismiss()
                        }

                        layout.importJson.setOnClickListener {
                            selectFile(ActivityRequestCode.DATA_MANAGER_UIGF_JSON)
                            win.dismiss()
                        }

                        layout.exportJson.setOnClickListener {
                            exportUIGFJSON(gachaHistoryUidList[bind.selectAccount.selectedItemPosition])
                            win.dismiss()
                        }

                        layout.whatIsUigf.setOnClickListener {
                            val uri = Uri.parse(Constants.UIGF_URL)
                            val intent = Intent(Intent.ACTION_VIEW,uri)
                            startActivity(intent)
                            win.dismiss()
                        }

                        layout.close.setOnClickListener {
                            win.dismiss()
                        }
                    }
                }
                true
            }
        }
    }

    private fun checkData(){
        activity?.runOnUiThread {
            gachaHistoryUidList.clear()
            JSONArray(sp.getString(JsonCacheName.GACHA_HISTORY_ACCOUNT_LIST, "[]")).toList(
                gachaHistoryUidList
            )

            if (gachaHistoryUidList.size == 0) {
                bind.noDataSpan.show()
                onNoData()
            } else {
                bind.noDataSpan.gone()
                PaiMonsNotebookDataBase.INSTANCE.getGachaHistoryForExcel(gachaHistoryUidList.first(), gachaData)
                refreshUI()
            }
        }
    }

    private fun onNoData() {
        bind.noDataSpan.show()
        bind.editUrl.setOnClickListener {
            showUrlEditDialog()
        }

        bind.importExcel.setOnClickListener {
            selectFile(ActivityRequestCode.DATA_MANAGER_UIGF_EXCEL)
        }

        bind.importJson.setOnClickListener {
            selectFile(ActivityRequestCode.DATA_MANAGER_UIGF_JSON)
        }
    }

    private fun refreshUI() {
        activity?.runOnUiThread {
            refreshList()
            bind.selectAccount.adapter = ArrayAdapter(bind.root.context, R.layout.item_text, gachaHistoryUidList).apply {
                setDropDownViewResource(R.layout.spinner_drop_down_style)
            }
        }
    }

    private fun refreshList(){
        loadSummarizePage()
        loadCharacterPage()
        loadWeaponPage()
    }

    private fun loadSummarizePage() {
        val page = PagerListBinding.bind(pages.first())
        summarizePageData.clear()
        gachaData.groupBy { it.uigf_gacha_type }.toList().copy(summarizePageData)

        if(page.list.adapter==null){
            page.list.adapter = ReAdapter(
                summarizePageData,
                R.layout.item_wish_home
            ) { view: View, pair: Pair<String, List<UIGFExcelBean>>, i: Int ->
                val item = ItemWishHomeBinding.bind(view)
                item.gachaName.text = GachaType.getNameByType(pair.first.toInt())
                val star = pair.second.groupBy { it.rank_type }
                val count = pair.second.size.toFloat()
                val star3Count = if(star["3"]!=null) star["3"]!!.size else 0
                val star4Count = if(star["4"]!=null) star["4"]!!.size else 0
                val star5Count = if(star["5"]!=null) star["5"]!!.size else 0

                var star4WeaponCount = 0
                var star4CharacterCount = 0

                var star5WeaponCount = 0
                var star5CharacterCount = 0

                star["4"]?.forEach {
                    when (it.item_type) {
                        "武器" -> star4WeaponCount++
                        "角色" -> star4CharacterCount++
                    }
                }

                star["5"]?.forEach {
                    when (it.item_type) {
                        "武器" -> star5WeaponCount++
                        "角色" -> star5CharacterCount++
                    }
                }

                item.star3Count.text = star3Count.toString()
                item.star4Count.text = star4Count.toString()
                item.star5Count.text = star5Count.toString()

                item.gachaCount.text = count.toInt().toString()
                item.star3Percent.text = Format.DECIMALS_FORMAT.format(star3Count / count * 100)
                item.star4Percent.text = Format.DECIMALS_FORMAT.format(star4Count / count * 100)
                item.star5Percent.text = Format.DECIMALS_FORMAT.format(star5Count / count * 100)

                item.avgGet5StarCount.text = Format.DECIMALS_FORMAT.format(count / star5Count)

                item.time.text = "${pair.second.first().time} ~ ${pair.second.last().time}"

                val historyBuilder = StringBuffer()
                historyBuilder.append("五星历史记录:")
                var gachaCount = 0
                pair.second.reversed()
                pair.second.forEach {
                    gachaCount++
                    when (it.rank_type) {
                        "5" -> {
                            historyBuilder.append("<font color='${Constants.gachaColorList.random()}'>${it.name}[$gachaCount]&nbsp;&nbsp</font>")
                            gachaCount = 0
                        }
                    }
                }

                item.notGetOfCount.text = gachaCount.toString()

                item.history.text = Html.fromHtml(historyBuilder.toString(), Html.FROM_HTML_MODE_LEGACY)

                item.pie.apply {
                    description.isEnabled = false
                    isDrawHoleEnabled = false
                    isRotationEnabled = false
                    isClickable = false
                    isSelected = false
                    legend.isEnabled = false

                    animateY(500)

                    val pieDataEntry = mutableListOf<PieEntry>()
                    val mColors = mutableListOf<Int>()

                    val tags = mutableListOf<String>()
                    if (star5CharacterCount > 0) {
                        pieDataEntry += PieEntry(star5CharacterCount.toFloat(), "")
                        mColors += Constants.GACHA_HISTORY_5_STAR_CHARACTER_COLOR
                        tags += "五星角色"
                    }
                    if (star5WeaponCount > 0) {
                        pieDataEntry += PieEntry(star5WeaponCount.toFloat(), "")
                        tags += "五星武器"
                        mColors += Constants.GACHA_HISTORY_5_STAR_WEAPON_COLOR
                    }
                    if (star4CharacterCount > 0) {
                        pieDataEntry += PieEntry(star4CharacterCount.toFloat(), "")
                        tags += "四星角色"
                        mColors += Constants.GACHA_HISTORY_4_STAR_CHARACTER_COLOR
                    }
                    if (star4WeaponCount > 0) {
                        pieDataEntry += PieEntry(star4WeaponCount.toFloat(), "")
                        tags += "四星武器"
                        mColors += Constants.GACHA_HISTORY_4_STAR_WEAPON_COLOR
                    }

                    val pieDataSet = PieDataSet(pieDataEntry, "").apply {
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
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                if (index >= tags.size) index = 0
                                return tags[index++]
                            }
                        }
                    }
                    data = PieData(pieDataSet)
                }
            }
            setViewMarginBottomByNavigationBarHeight(page.list)
        }else{
            page.list.adapter?.notifyDataSetChanged()
        }
    }

    private fun loadCharacterPage() {
        val page = PagerListBinding.bind(pages[1])

        val itemList = gachaData.groupBy { it.item_type }

        if (itemList["角色"] != null) {
            characterPageData.clear()
            itemList["角色"]!!.groupBy { it.rank_type }.toList().sortedByDescending { it.first }.copy(characterPageData)

            if(page.list.adapter==null){
                page.list.adapter = ReAdapter(
                    characterPageData,
                    R.layout.item_wish_entity_group
                ) { view, pair, position ->
                    val item = ItemWishEntityGroupBinding.bind(view)
                    item.name.text = "${pair.first}星"

                    val character = pair.second.groupBy { it.name }.toList()
                    item.list.adapter = ReAdapter(
                        character.toList(),
                        R.layout.item_wish_entity
                    ) { view, pair, position ->
                        val entity = ItemWishEntityBinding.bind(view)
                        val character = CharacterBean.getCharacterByName(pair.first)
                        if (character != null) {
                            loadImage(entity.icon, character.icon)
                            entity.type.setImageResource(Element.getImageResourceByType(character.element))
                            entity.starBackground.setImageResource(
                                Star.getStarResourcesByStarNum(
                                    character.star,
                                    false
                                )
                            )
                        }
                        entity.name.text = pair.first
                        entity.count.text = pair.second.size.toString()
                    }
                }
                setViewMarginBottomByNavigationBarHeight(page.list)
            }else{
                page.list.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun loadWeaponPage() {
        val page = PagerListBinding.bind(pages[2])

        val itemList = gachaData.groupBy { it.item_type }

        if (itemList["武器"] != null) {

            weaponPageData.clear()
            itemList["武器"]!!.groupBy { it.rank_type }.toList().sortedByDescending { it.first }.copy(weaponPageData)

            if(page.list.adapter==null){
                page.list.adapter = ReAdapter(
                    weaponPageData,
                    R.layout.item_wish_entity_group
                ) { view, pair, position ->
                    val item = ItemWishEntityGroupBinding.bind(view)
                    item.name.text = "${pair.first}星"

                    val character = pair.second.groupBy { it.name }.toList()
                    item.list.adapter = ReAdapter(
                        character.toList(),
                        R.layout.item_wish_entity
                    ) { view, pair, position ->
                        val entity = ItemWishEntityBinding.bind(view)
                        val weapon = WeaponBean.getWeaponByName(pair.first)
                        if (weapon != null) {
                            loadImage(entity.icon, weapon.icon)
                            entity.type.setImageResource(WeaponType.getResourceByType(weapon.weaponType))
                            entity.starBackground.setImageResource(
                                Star.getStarResourcesByStarNum(
                                    weapon.star,
                                    false
                                )
                            )
                        }
                        entity.name.text = pair.first
                        entity.count.text = pair.second.size.toString()
                    }
                }
                setViewMarginBottomByNavigationBarHeight(page.list)
            }else{
                page.list.adapter?.notifyDataSetChanged()
            }
        }
    }

    //检查输入的URL是否能正常访问
    private fun checkUrl(url: String, block: (Boolean) -> Unit) {
        if (url.takeLast(5) != "#/log") {
            block(false)
            return
        }
        Ok.get(MiHoYoApi.getGachaLogUrl(url, GachaType.PERMANENT, 1, 6, "0")) {
            block(it.ok)
        }
    }

    //弹出输入url对话框
    private fun showUrlEditDialog() {
        val layout = PopEditUrlBinding.bind(
            LayoutInflater.from(bind.root.context).inflate(R.layout.pop_edit_url, null)
        )
        val win = showAlertDialog(bind.root.context, layout.root)

        layout.confirm.setOnClickListener {
            val input = layout.input.text.toString().trim()
            checkUrl(input) {
                activity?.runOnUiThread {
                    if (it) {
                        LoadingGachaWishHistoryActivity.logUrl = input
                        startActivityForResult(
                            Intent(bind.root.context, LoadingGachaWishHistoryActivity::class.java),
                            ActivityRequestCode.GACHA_HISTORY
                        )
                    } else {
                        "输入的URL不正确,请检查后再次输入吧!".showLong()
                    }
                }
            }
            win.dismiss()
        }

        layout.cancel.setOnClickListener {
            win.dismiss()
        }
    }

    //导出UIGF EXCEL
    private fun exportUIGFExcel(uid: String) {
        showLoading(activity!!)

        thread {
            val metaDataGroupBy = PaiMonsNotebookDataBase.INSTANCE.getGachaHistoryForExcel(uid)
                .groupBy { GachaType.getUIGFType(it.gacha_type) }.toList()
            val table = SXSSFWorkbook()

            val headerRowFont = table.createFont()
            headerRowFont.fontHeightInPoints = 12.toShort()

            //导出卡池数据
            val metaGroupColumnName = listOf("时间", "名称", "物品类型", "星级", "祈愿类型")
            metaDataGroupBy.forEach {
                if (it.second.isNotEmpty()) {
                    val sheet =
                        table.createSheet(GachaType.getNameByType(it.second.first().gacha_type.toInt()))

                    sheet.setColumnWidth(0, 256 * 19 + 184)
                    sheet.setColumnWidth(1, 256 * 16 + 184)
                    sheet.setColumnWidth(2, 256 * 10 + 184)
                    sheet.setColumnWidth(3, 256 * 5 + 184)
                    sheet.setColumnWidth(4, 256 * 16 + 184)

                    val headerRow = sheet.createRow(0)
                    headerRow.heightInPoints = 24f

                    metaGroupColumnName.forEachIndexed { index: Int, s: String ->
                        headerRow.createCell(index).apply {
                            setCellValue(s)
                        }
                    }

                    it.second.forEachIndexed { index, uigfBean ->
                        val row = sheet.createRow(index + 1)
                        row.heightInPoints = 20f
                        row.createCell(0).setCellValue(XSSFRichTextString(uigfBean.time))
                        row.createCell(1).setCellValue(uigfBean.name)
                        row.createCell(2).setCellValue(uigfBean.item_type)
                        row.createCell(3).setCellValue(uigfBean.rank_type)
                        row.createCell(4).setCellValue(GachaType.getNameByType(uigfBean.gacha_type.toInt()))
                    }
                }
            }

            //导出原始数据
            val sheet = table.createSheet("原始数据")
            val metaDataColumnName = listOf(
                PaiMonsNotebookDataBase.COLUMN_NAME_COUNT,
                PaiMonsNotebookDataBase.COLUMN_NAME_GACHA_TYPE,
                PaiMonsNotebookDataBase.COLUMN_NAME_ID,
                PaiMonsNotebookDataBase.COLUMN_NAME_ITEM_ID,
                PaiMonsNotebookDataBase.COLUMN_NAME_ITEM_TYPE,
                PaiMonsNotebookDataBase.COLUMN_NAME_LANG,
                PaiMonsNotebookDataBase.COLUMN_NAME_NAME,
                PaiMonsNotebookDataBase.COLUMN_NAME_RANK_TYPE,
                PaiMonsNotebookDataBase.COLUMN_NAME_TIME,
                PaiMonsNotebookDataBase.COLUMN_NAME_UID,
                PaiMonsNotebookDataBase.COLUMN_NAME_UIGF_GACHA_TYPE
            )

            sheet.setColumnWidth(0, 256 * 5 + 184)
            sheet.setColumnWidth(1, 256 * 10 + 184)
            sheet.setColumnWidth(2, 256 * 22 + 184)
            sheet.setColumnWidth(3, 256 * 6 + 184)
            sheet.setColumnWidth(4, 256 * 8 + 184)
            sheet.setColumnWidth(5, 256 * 8 + 184)
            sheet.setColumnWidth(6, 256 * 16 + 184)
            sheet.setColumnWidth(7, 256 * 8 + 184)
            sheet.setColumnWidth(8, 256 * 20 + 184)
            sheet.setColumnWidth(9, 256 * 12 + 184)
            sheet.setColumnWidth(10, 256 * 12 + 184)

            val headerRow = sheet.createRow(0)
            headerRow.heightInPoints = 24f

            metaDataColumnName.forEachIndexed { index: Int, s: String ->
                headerRow.createCell(index).apply {
                    setCellValue(XSSFRichTextString(s).apply {
                        applyFont(headerRowFont)
                    })
                }
            }

            val metaData = PaiMonsNotebookDataBase.INSTANCE.getGachaHistoryForExcel(uid)

            metaData.forEachIndexed { index, uigfBean ->
                val row = sheet.createRow(index + 1)
                row.heightInPoints = 20f
                row.createCell(0).setCellValue(uigfBean.count?:"1")
                row.createCell(1).setCellValue(uigfBean.gacha_type)
                row.createCell(2).setCellValue(uigfBean.id)
                row.createCell(3).setCellValue(uigfBean.item_id?:"")
                row.createCell(4).setCellValue(uigfBean.item_type)
                row.createCell(5).setCellValue(uigfBean.lang)
                row.createCell(6).setCellValue(uigfBean.name)
                row.createCell(7).setCellValue(uigfBean.rank_type)
                row.createCell(8).setCellValue(uigfBean.time)
                row.createCell(9).setCellValue(uigfBean.uid)
                row.createCell(10).setCellValue(uigfBean.uigf_gacha_type)
            }
            FileUtil.writeUIGFExcel(
                table,
                "${uid}_${System.currentTimeMillis()}"
            )
            activity?.runOnUiThread {
                dismissLoadingWindow()
                showSuccessInformationAlertDialog(bind.root.context, "导出成功")
                NotificationManager.sendNotification("导出到以下位置:","${PaiMonsNoteBook.context.getExternalFilesDir(null)?.absolutePath}/table")
            }
        }
    }

    //选择文件
    private fun selectFile(select:Int) {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"

            startActivityForResult(intent,select)
        } else {
            "导入UIGF EXCEL/JSON需要获得外部文件读取权限".showLong()
            ActivityCompat.requestPermissions(activity!!,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),select)
        }
    }

    //导入UIGF EXCEL
    private fun importUIGFExcel(uri: Uri) {
        showLoading(activity!!)
        thread {
            val file = FileUtil.uriToFile(uri)
            if (file != null) {
                val suffix = file.path.takeLast(4)
                if (suffix == "xlsx") {
                    try {
                        val opcPackage = OPCPackage.open(file, PackageAccess.READ)
                        val xssfReader = XSSFReader(opcPackage)
                        val sharedStringsTable = ReadOnlySharedStringsTable(opcPackage)
                        val stylesTable = xssfReader.stylesTable
                        val xmlReader = XMLReaderFactory.createXMLReader()
                        val sheetHandler = SheetHandler()
                        val xssfSheetXMLHandler =
                            XSSFSheetXMLHandler(
                                stylesTable,
                                sharedStringsTable,
                                sheetHandler,
                                false
                            )
                        xmlReader.contentHandler = xssfSheetXMLHandler

                        val sheetIterator = xssfReader.sheetsData as XSSFReader.SheetIterator
                        while (sheetIterator.hasNext()) {
                            val input = sheetIterator.next()
                            if (sheetIterator.sheetName == "原始数据") {
                                val inputSource = InputSource(input)
                                xmlReader.parse(inputSource)
                            }
                        }

                        //删除第一行 因为第一行为表头
                        sheetHandler.data.removeFirst()

                        if (sheetHandler.data.size>0){
                            val uid = sheetHandler.data.first().uid

                            checkUidExist(uid){
                                if(it){
                                    var itemId = PaiMonsNotebookDataBase.INSTANCE.getMinGachaHistoryId(uid)
                                    sheetHandler.data.forEach {
                                        if (it.id.isNullOrEmpty()) {
                                            it.id = (itemId++).toString()
                                        }
                                        PaiMonsNotebookDataBase.INSTANCE.insertDataForExcel(it,uid)
                                    }

                                    showSuccess()
                                    PaiMonsNotebookDataBase.INSTANCE.addGachaHistoryAccount(uid)
                                    checkData()
                                }
                            }
                        } else {
                            showFailure("导入失败", "祈愿表结构错误:没有原始数据表")
                        }
                    } catch (e: Exception) {
                        showFailure( "导入失败","文件异常:请用系统自带的文件管理器选择文件,并重试。\n(若一直出现该错误,你应该试试JSON导入)")
                    }
                } else {
                    showFailure(  "导入失败", "文件错误:选择的不是xlsx文件")
                }
            } else {
                showFailure( "导入失败", "空文件错误:请用系统自带的文件管理器选择文件")
            }
        }
    }

    //导入UIGF JSON
    private fun importUIGFJSON(uri: Uri){
        showLoading(activity!!)
        thread {
            val file = FileUtil.uriToFile(uri)
            if (file != null) {
                val suffix = file.path.takeLast(4)
                if (suffix == "json") {
                    try {
                        val text = file.readText(Charset.defaultCharset())
                        val uigfJson = GSON.fromJson(text,UIGFJSON::class.java)

                        with(uigfJson.info){
                            export_app.length
                            export_app_version.length
                            export_time.length
//                            export_timestamp.length
                            lang.length
                            uid.length
                        }
                        uigfJson.list.first().uigf_gacha_type.length

                        checkUidExist(uigfJson.info.uid){
                            thread {
                                if(it){
                                    var itemId = PaiMonsNotebookDataBase.INSTANCE.getMinGachaHistoryId(uigfJson.info.uid)

                                    uigfJson.list.forEach {
                                        if (it.id.isNullOrEmpty()) {
                                            it.id = (itemId++).toString()
                                        }
                                        PaiMonsNotebookDataBase.INSTANCE.insertDataForJson(it,uigfJson.info.uid,uigfJson.info.lang)
                                    }

                                    showSuccess()
                                    PaiMonsNotebookDataBase.INSTANCE.addGachaHistoryAccount(uigfJson.info.uid)
                                    checkData()
                                }
                            }
                        }
                    }catch (e:Exception){
                        showFailure(  "导入失败", "文件错误:选择的不是UIGF Json文件或文件被修改、损坏。")
                        e.printStackTrace()
                    }
                } else {
                    showFailure(  "导入失败", "文件错误:选择的不是Json文件")
                }
            } else {
                showFailure( "导入失败", "空文件错误:请用系统自带的文件管理器选择文件")
            }
        }
    }

    //导出UIGF JSON
    private fun exportUIGFJSON(uid:String){
        showLoading(activity!!)
        thread {
            val gachaDataList = PaiMonsNotebookDataBase.INSTANCE.getGachaHistoryForExcel(uid)
            val info = UIGFJSON.UIGFInfo(
                gachaDataList[bind.selectAccount.selectedItemPosition].uid,
                gachaDataList[bind.selectAccount.selectedItemPosition].lang,
                Format.TIME_FULL.format(System.currentTimeMillis()),
                (System.currentTimeMillis()/1000L).toString(),
                PaiMonsNoteBook.APP_NAME,
                PaiMonsNoteBook.VERSION_NAME,
                PaiMonsNoteBook.UIGF_VERSION
            )

            val uigfjson = UIGFJSON(info,PaiMonsNotebookDataBase.INSTANCE.getGachaHistoryForJson(uid))
            FileUtil.writeUIGFJSON(uigfjson)
            activity?.runOnUiThread {
                showSuccessInformationAlertDialog(bind.root.context, "导出成功")
                NotificationManager.sendNotification("导出到以下位置:","${PaiMonsNoteBook.context.getExternalFilesDir(null)?.absolutePath}/json")
            }
            dismissLoadingWindow()
        }
    }

    //检查导入的uid是否存在
    private fun checkUidExist(uid: String,block:(Boolean)-> Unit) {
        val isExists = PaiMonsNotebookDataBase.INSTANCE.checkGachaHistoryTableExists(uid)
        if (isExists) {
            activity?.runOnUiThread {
                dismissLoadingWindow()
                showConfirmAlertDialog( bind.root.context,"本地已有uid为 $uid 的祈愿数据","是要将新增或本地不存在的祈愿记录添加到原来的记录中吗?"){
                    showLoading(activity!!)
                    thread {
                        if (it) {
                            block(true)
                        } else {
                            activity?.runOnUiThread {
                                dismissLoadingWindow()
                            }
                            block(false)
                        }
                    }
                }
            }
        } else {
            block(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            ActivityRequestCode.DATA_MANAGER_UIGF_EXCEL->{
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data
                    importUIGFExcel(uri!!)
                } else {
                    "取消选择".show()
                }
            }
            ActivityRequestCode.DATA_MANAGER_UIGF_JSON->{
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data
                    importUIGFJSON(uri!!)
                } else {
                    "取消选择".show()
                }
            }
            ActivityRequestCode.GACHA_HISTORY->{
                if(resultCode==ActivityResponseCode.OK){
                    checkData()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectFile(requestCode)
            }
        }
    }

    private fun showFailure(title:String,message:String){
        activity?.runOnUiThread {
            dismissLoadingWindow()
            showFailureAlertDialog(bind.root.context, title, message)
        }
    }

    private fun showSuccess(){
        activity?.runOnUiThread {
            dismissLoadingWindow()
            showSuccessInformationAlertDialog(bind.root.context, "导入成功")
        }
    }

}