package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.materials.DailyMaterial
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
    private var characterGroup = mutableListOf<Pair<String, List<Pair<String, List<CharacterBean>>>>>()

    private val allWeapon = mutableListOf<WeaponBean>()
    private val weaponList = mutableListOf<WeaponBean>()
    private var weaponGroup = mutableListOf<Pair<String, List<Pair<String, List<WeaponBean>>>>>()

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
        bind.weekSelect.adapter = ArrayAdapter(bind.root.context,R.layout.item_text,week).apply {
            setDropDownViewResource(R.layout.spinner_drop_down_style)
        }
        bind.weekSelect.dropDownVerticalOffset = 28.dp.toInt()
        bind.weekSelect.dropDownHorizontalOffset = -8.dp.toInt()
        bind.weekSelect.select { _: Int, _: Long ->
            isChange = true
            loadCharacter(pages[0])
            loadWeapon(pages[1])
            isChange = false
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
            //根据材料的地区进行第一次分组 分出地区
            characterList.groupBy { it.dailyMaterials.area }.toList().forEach { area->
                //根据地区的每个角色的每日材料的KEY继续分出每种材料
                characterGroup.add(area.first to area.second.groupBy { it.dailyMaterials.key }.toList())
            }

            if(page.list.adapter==null){
                page.pagerName.text = "天赋培养"
                page.list.adapter = ReAdapter(characterGroup,R.layout.item_materials_group){
                        view, pair, position ->
                    val materialsItem = ItemMaterialsGroupBinding.bind(view)
                    materialsItem.name.text = Area.getNameByArea(pair.first)

                    materialsItem.list.adapter = ReAdapter(pair.second,R.layout.item_entity_group){
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
            weaponList.groupBy { it.dailyMaterials.area }.toList().forEach { area->
                weaponGroup.add(area.first to area.second.groupBy { it.dailyMaterials.key }.toList())
            }

            if(page.list.adapter==null){
                page.pagerName.text = "武器突破"

                page.list.adapter = ReAdapter(weaponGroup,R.layout.item_materials_group){
                    view, pair, position ->
                    val materialItems = ItemMaterialsGroupBinding.bind(view)
                    materialItems.name.text = Area.getNameByArea(pair.first)

                    materialItems.list.adapter = ReAdapter(pair.second,R.layout.item_entity_group){
                        view, pair, position ->
                        val entityItem = ItemEntityGroupBinding.bind(view)
                        entityItem.materialsName.text = pair.second.first().dailyMaterials.name
                        loadImage(entityItem.dailyMaterial,pair.second.first().dailyMaterials.icon)

                        entityItem.list.adapter = ReAdapter(pair.second,R.layout.item_entity){
                                view: View, weaponBean: WeaponBean, i: Int ->
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
                }
            }else{
                page.list.adapter?.notifyDataSetChanged()
            }
        }
    }
}