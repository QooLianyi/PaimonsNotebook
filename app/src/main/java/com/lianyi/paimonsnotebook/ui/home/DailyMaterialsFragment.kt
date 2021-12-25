package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.DailyMaterial
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.adapter.PagerAdapter
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.ui.activity.CharacterDetailActivity
import com.lianyi.paimonsnotebook.ui.activity.WeaponDetailActivity
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class DailyMaterialsFragment : BaseFragment(R.layout.fragment_daily_materials) {

    lateinit var bind:FragmentDailyMaterialsBinding

    private val allCharacter = mutableListOf<CharacterBean>()
    private val characterList = mutableListOf<CharacterBean>()
    private var characterGroup = mutableListOf<Pair<String,List<CharacterBean>>>()

    private val allWeapon = mutableListOf<WeaponBean>()
    private val weaponList = mutableListOf<WeaponBean>()
    private var weaponGroup = mutableListOf<Pair<String,List<WeaponBean>>>()

    //判断星期是否发生改变
    private var isChange = true
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

        //加载角色和武器
        JSONArray(csp.getString(JsonCacheName.CHARACTER_LIST,"")!!).toList(allCharacter)
        JSONArray(wsp.getString(JsonCacheName.WEAPON_LIST,"")!!).toList(allWeapon)

        //星期选择
        val week = resources.getStringArray(R.array.week)
        val adapter = ArrayAdapter(bind.root.context,R.layout.item_text,week)
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_style)
        bind.weekSelect.adapter = adapter
        bind.weekSelect.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                isChange = true
                loadCharacter(pages[0])
                loadWeapon(pages[1])
                isChange = false
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        //进入时星期设置为当前的日期
        bind.weekSelect.setSelection(Format.getWeekByName(Format.TIME_WEEK.format(System.currentTimeMillis())))
        bind.dailyViewPager.adapter = PagerAdapter(pages, titles)
        bind.dailyViewPager.offscreenPageLimit = 3 //设置缓存页面
        bind.dailyTabLayout.setupWithViewPager(bind.dailyViewPager)
        bind.dailyTabLayout.tab {
            when(it){
                0->{
                    loadCharacter(pages[it])
                }
                1->{
                    loadWeapon(pages[it])
                }
            }
        }
        loadCharacter(pages[0])
        isChange = false
    }

    //加载角色
    private fun loadCharacter(view:View){

        if(isChange){
            val page = PagerDailyMaterialsBinding.bind(view)
            characterList.clear()

            allCharacter.forEach { character->
                DailyMaterial.getDropDayByKey(character.dailyMaterials.key).forEach {
                    if(it==bind.weekSelect.selectedItemPosition+1){
                        characterList+=character
                        return@forEach
                    }
                }
            }
            characterGroup.clear()
            characterList.groupBy { it.dailyMaterials.key }.toList().forEach {
                characterGroup.add(it)
            }

            if(page.list.adapter==null){
                page.pagerName.text = "天赋培养"
                page.list.adapter = ReAdapter(characterGroup,R.layout.item_entity_group){
                        view, pair, position ->
                    val item = ItemEntityGroupBinding.bind(view)
                    item.materialsName.text = pair.second.first().dailyMaterials.name
                    loadImage(item.dailyMaterial,pair.second.first().dailyMaterials.icon)

                    item.list.adapter = ReAdapter(pair.second,R.layout.item_entity){
                            view, characterBean, position ->
                        val characterItem = ItemEntityBinding.bind(view)
                        characterItem.starBackground.setImageResource(Star.getStarResourcesByStarNum(characterBean.star,false))
                        characterItem.type.setImageResource(Element.getImageResourceByType(characterBean.element))
                        characterItem.name.text = characterBean.name
                        loadImage(characterItem.icon,characterBean.icon)

                        characterItem.root.setOnClickListener {
                            CharacterDetailActivity.character = characterBean
                            goA<CharacterDetailActivity>()
                        }

                    }

                }
            }else{
                page.list.adapter?.notifyDataSetChanged()
            }
        }
    }

    //加载武器
    private fun loadWeapon(view: View){

        if(isChange){
            val page = PagerDailyMaterialsBinding.bind(view)

            weaponList.clear()
            allWeapon.forEach { weapon->
                DailyMaterial.getDropDayByKey(weapon.dailyMaterials.key).forEach {
                    if(it==bind.weekSelect.selectedItemPosition+1){
                        weaponList+=weapon
                        return@forEach
                    }
                }
            }
            weaponGroup.clear()
            weaponList.groupBy { it.dailyMaterials.key }.toList().forEach {
                weaponGroup.add(it)
            }

            if(page.list.adapter==null){
                page.pagerName.text = "武器突破"
                page.list.adapter = ReAdapter(weaponGroup,R.layout.item_entity_group){
                        view, pair, position ->
                    val item = ItemEntityGroupBinding.bind(view)
                    item.materialsName.text = pair.second.first().dailyMaterials.name
                    loadImage(item.dailyMaterial,pair.second.first().dailyMaterials.icon)

                    item.list.adapter = ReAdapter(pair.second,R.layout.item_entity){
                            view, weaponBean, position ->
                        val weaponItem = ItemEntityBinding.bind(view)
                        weaponItem.starBackground.setImageResource(Star.getStarResourcesByStarNum(weaponBean.star,false))
                        weaponItem.type.setImageResource(WeaponType.getResourceByType(weaponBean.weaponType))
                        weaponItem.name.text = weaponBean.name
                        loadImage(weaponItem.icon,weaponBean.icon)

                        weaponItem.root.setOnClickListener {
                            WeaponDetailActivity.weapon = weaponBean
                            goA<WeaponDetailActivity>()
                        }
                    }
                }
            }else{
                page.list.adapter?.notifyDataSetChanged()
            }
        }

    }

    //加载物品列表
//    private fun loadItemList(list:RecyclerView, kind:String, type:String){
//        val currentItemList = mutableListOf<BlackBoardBean.DataBean.ListBean>()
//        val blackBoard = GSON.fromJson(sp.getString(JsonCacheName.BLACK_BOARD,""),BlackBoardBean::class.java)
//        if(blackBoard !=null) {
//            blackBoard.data.list.forEach { bean ->
//                if (bean.kind == kind && bean.break_type == type) {
//                    bean.drop_day.forEach {
//                        if (it == "${bind.weekSelect.selectedItemPosition + 1}") {
//                            currentItemList += bean
//                        }
//                    }
//                }
//            }
//
//            //根据材料进行分组
//            val group = currentItemList.groupBy { it.contentInfos.last()}
//
//            activity?.runOnUiThread {
//                list.adapter = ReAdapter(group.toList(),R.layout.item_entity_group){
//                    view, pair, position ->
//                    val groupItem = ItemEntityGroupBinding.bind(view)
//                    groupItem.materialsName.text = pair.first.title.substring("「","」")
//
//                    //材料列表
//                    groupItem.materialsList.adapter = ReAdapter(pair.second.first().contentInfos,R.layout.item_material){
//                        view, contentInfosBean, _ ->
//                        val materialItem = ItemMaterialBinding.bind(view)
//                        loadImage(materialItem.icon,contentInfosBean.icon)
//                    }
//
//                    groupItem.list.adapter = ReAdapter(pair.second,R.layout.item_entity){
//                            view, listBean, _ ->
//                        val item = ItemEntityBinding.bind(view)
//                        loadImage(item.icon,listBean.img_url)
//                        item.name.text = listBean.title
//
//                        item.root.setOnClickListener {
//                            val layout = PopMaterialsDetailBinding.bind(layoutInflater.inflate(R.layout.pop_materials_detail,null))
//                            val win = AlertDialog.Builder(it.context)
//                                .setView(layout.root)
//                                .create()
//                            val materials = mutableListOf<BlackBoardBean.DataBean.ListBean.ContentInfosBean>()
//                            layout.name.text = listBean.title
//                            listBean.contentInfos.forEach {
//                                materials += it
//                            }
//                            //设置适配器
//                            layout.list.adapter = ReAdapter(materials,R.layout.item_material_with_text_background){
//                                    view, contentInfosBean, position ->
//                                val ma = ItemMaterialWithTextBackgroundBinding.bind(view)
//                                loadImage(ma.icon,contentInfosBean.icon)
//                                ma.name.text = contentInfosBean.title
//                            }
//
//                            //取消dialog自带的背景
//                            win.window?.setBackgroundDrawableResource(R.color.transparent)
//                            //设置左右两边的间隔
//                            win.window?.decorView?.setPadding(10.dp.toInt(),0,10.dp.toInt(),0)
//                            win.window?.setLayout(resources.displayMetrics.widthPixels-20.dp.toInt(),ViewGroup.LayoutParams.WRAP_CONTENT)
//                            win.show()
//
//                            layout.close.setOnClickListener {
//                                win.cancel()
//                            }
//                        }
//                    }
//
//                }
//
//            }
//        }else{
//            RefreshData.getBlackBoard {
//                activity?.runOnUiThread {
//                    getString(R.string.error_black_board_is_empty).showLong()
//                }
//            }
//        }
//    }

}