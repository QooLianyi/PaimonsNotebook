package com.lianyi.paimonsnotebook.lib.excel

import com.lianyi.paimonsnotebook.bean.gacha.UIGFExcelBean
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler
import org.apache.poi.xssf.usermodel.XSSFComment

class SheetHandler:XSSFSheetXMLHandler.SheetContentsHandler {

    var uigfGachaBean = UIGFExcelBean()
    val MAX_COUNT = 1000
    val data = mutableListOf<UIGFExcelBean>()
    val allData = mutableListOf<UIGFExcelBean>()
    var count = 0


    override fun startRow(p0: Int) {
        if(p0>0){
            uigfGachaBean = UIGFExcelBean()
        }
    }

    override fun endRow(p0: Int) {
        data += uigfGachaBean
        count++
//        if(data.size==MAX_COUNT){
//            allData.addFromList(data)
//            data.clear()
//        }
    }

    override fun cell(p0: String?, p1: String?, p2: XSSFComment?) {
        when(p0?.take(1)){
            "A"-> uigfGachaBean.count = p1
            "B"->uigfGachaBean.gacha_type = p1
            "C"->uigfGachaBean.id = p1
            "D"->uigfGachaBean.item_id = p1
            "E"->uigfGachaBean.item_type = p1
            "F"->uigfGachaBean.lang = p1
            "G"->uigfGachaBean.name = p1
            "H"->uigfGachaBean.rank_type = p1
            "I"->uigfGachaBean.time = p1
            "J"->uigfGachaBean.uid = p1
            "K"->uigfGachaBean.uigf_gacha_type = p1
        }
    }

    override fun headerFooter(p0: String?, p1: Boolean, p2: String?) {

    }

}