package com.lianyi.paimonsnotebook.ui.home

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.BlackBoardBean
import com.lianyi.paimonsnotebook.config.Format
import com.lianyi.paimonsnotebook.config.JsonCacheName
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.ui.RefreshData
import com.lianyi.paimonsnotebook.util.*
import me.jessyan.autosize.internal.CustomAdapt
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class DailyMaterialsFragment : Fragment(R.layout.fragment_daily_materials),CustomAdapt {

    lateinit var bind:FragmentDailyMaterialsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentDailyMaterialsBinding.bind(view)
        val pages = listOf(
            layoutInflater.inflate(R.layout.pager_daily_materials,null),
            layoutInflater.inflate(R.layout.pager_daily_materials,null)
        )

        val titles = listOf("天赋培养","武器突破")

        bind.weekSelectSpan.setOnClickListener {
            bind.weekSelect.performClick()
        }

        //星期选择
        val week = resources.getStringArray(R.array.week)
        val adapter = ArrayAdapter(bind.root.context,R.layout.item_text,week)
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_style)
        bind.weekSelect.adapter = adapter
        bind.weekSelect.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                 loadCharacter(pages[0],true)
                 loadWeapon(pages[1],true)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        //进入时星期设置为当前的日期
        bind.weekSelect.setSelection(Format.getWeekByName(Format.TIME_WEEK.format(System.currentTimeMillis())))
        bind.dailyViewPager.adapter = PagerAdapter(pages, titles)
        bind.dailyTabLayout.setupWithViewPager(bind.dailyViewPager)
        bind.dailyTabLayout.tab {
            when(it){
                0->{
                    loadCharacter(pages[it],false)
                }
                1->{
                    loadWeapon(pages[it],false)
                }
            }
        }
        loadCharacter(pages[0],false)
    }

    private fun loadCharacter(view:View,dataChange:Boolean){
        val page = PagerDailyMaterialsBinding.bind(view)
        page.pagerName.text = "天赋培养"
        if(page.list.adapter == null||dataChange){
            loadItemList(page.list,"2","2")
        }
    }

    private fun loadWeapon(view: View,dataChange:Boolean){
        val page = PagerDailyMaterialsBinding.bind(view)
        page.pagerName.text = "武器突破"
        if(page.list.adapter == null||dataChange){
            loadItemList(page.list,"2","1")
        }
    }

    private fun loadItemList(list:RecyclerView, kind:String, type:String){
        val currentItemList = mutableListOf<BlackBoardBean.DataBean.ListBean>()
        val blackBoard = GSON.fromJson(sp.getString(JsonCacheName.BLACK_BOARD,""),BlackBoardBean::class.java)
        if(blackBoard !=null) {
            blackBoard.data.list.forEach { bean ->
                if (bean.kind == kind && bean.break_type == type) {
                    bean.drop_day.forEach {
                        if (it == "${bind.weekSelect.selectedItemPosition + 1}") {
                            currentItemList += bean
                        }
                    }
                }
            }
            //根据"0"来排序 与米游社WIKI保持一致
            currentItemList.sortBy { it.sort.split(":")[1].split(",").first().toInt() }
            activity?.runOnUiThread {
                list.adapter = ReAdapter(currentItemList,R.layout.item_entity){
                        view, listBean, position ->
                    val item = ItemEntityBinding.bind(view)
                    loadImage(item.icon,listBean.img_url)
                    item.name.text = listBean.title

                    item.root.setOnClickListener {
                        val layout = PopMaterialsDetailBinding.bind(layoutInflater.inflate(R.layout.pop_materials_detail,null))
                        val win = AlertDialog.Builder(it.context)
                            .setView(layout.root)
                            .create()
                        val materials = mutableListOf<BlackBoardBean.DataBean.ListBean.ContentInfosBean>()
                        layout.name.text = listBean.title
                        listBean.contentInfos.forEach {
                            materials += it
                        }
                        layout.list.adapter = ReAdapter(materials,R.layout.item_materials_with_text_background){
                            view, contentInfosBean, position ->
                            val ma = ItemMaterialsWithTextBackgroundBinding.bind(view)
                            loadImage(ma.icon,contentInfosBean.icon)
                            ma.name.text = contentInfosBean.title
                        }
                        win.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        win.show()
                        layout.close.setOnClickListener {
                            win.cancel()
                        }
                    }
                }
            }
        }else{
            RefreshData.getBlackBoard {
                activity?.runOnUiThread {
                    getString(R.string.error_black_board_is_empty).showLong()
                }
            }
        }
    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 730f
    }

}