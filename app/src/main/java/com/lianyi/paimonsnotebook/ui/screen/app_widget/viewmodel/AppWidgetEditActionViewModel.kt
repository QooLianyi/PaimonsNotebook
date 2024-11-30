package com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.data.popup.PopupWindowPositionProvider
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetComponentType
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetDataType
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.extension.value.dpToPx
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.AppWidgetEditData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.AppWidgetTransformData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.binding.AppWidgetBindingService
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.binding.AppWidgetFieldBindingData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.config.AppWidgetEditValueConfigData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.util.enums.ComponentAlignType
import com.lianyi.paimonsnotebook.ui.screen.app_widget.util.enums.ComponentSelectType
import com.lianyi.paimonsnotebook.ui.theme.Error
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.Primary_3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//桌面组件编辑操作vm
class AppWidgetEditActionViewModel(
    scope: CoroutineScope
) {

    //组件数量上限
    private val componentMaxCount = 30

    //无效值
    private val invalidValue = -999f
    val invalidStringValue = "-"

    //字符最大数量
    private val textMaxLength = 255

    //选中的组件
    var selectedComponents = mutableStateListOf<AppWidgetEditData.Component>()
        private set

    //当前编辑的数据
    var currentEditData by mutableStateOf(AppWidgetEditData())
        private set

    //选择模式
    var currentSelectionMode by mutableStateOf(ComponentSelectType.SingleSelection)

    //是否显示添加组件的弹窗
    var showAddComponentPopupWindow by mutableStateOf(false)
        private set

    //启用对齐操作按钮
    var enableAlignButton by mutableStateOf(false)
        private set

    var enableAlignButtonBothAxis by mutableStateOf(false)
        private set

    //启用历史记录更改至下一条
    var enableHistoryNext by mutableStateOf(false)
        private set

    //启用历史记录更改至上一条
    var enableHistoryPrev by mutableStateOf(false)
        private set

    lateinit var popupPositionProvider: PopupWindowPositionProvider
        private set

    var inputTextShowValue by mutableStateOf(invalidStringValue)
        private set

    var showTextConfig by mutableStateOf(false)
        private set

    var showImageConfig by mutableStateOf(false)
        private set

    var showProgressBarConfig by mutableStateOf(false)
        private set

    var showAppWidgetBindingPanel by mutableStateOf(false)
        private set

    val appWidgetBindingService = AppWidgetBindingService()

    lateinit var fieldBindingData: AppWidgetFieldBindingData
        private set

    //当前历史记录的索引
    private var currentHistoryIndex = 0

    //每次操作后都会存储上一次的的数据,用于撤回操作
    private val historyList = mutableListOf<AppWidgetEditData>()

    private val baseDensity = 720f

    val transformValueConfigMap = mapOf(
        "X" to createEditValueConfigData(
            min = 0f,
            max = 9999f,
            setProperty = { component -> component.baseComponent::x::set },
            getProperty = { it.baseComponent.x }),
        "Y" to createEditValueConfigData(
            min = 0f,
            max = 9999f,
            setProperty = { component -> component.baseComponent::y::set },
            getProperty = { it.baseComponent.y }),
        "W" to createEditValueConfigData(
            min = .5f,
            max = 9999f,
            setProperty = { component -> component.baseComponent::width::set },
            getProperty = { it.baseComponent.width }),
        "H" to createEditValueConfigData(
            min = .5f,
            max = 9999f,
            setProperty = { component -> component.baseComponent::height::set },
            getProperty = { it.baseComponent.height }),
        "R" to createEditValueConfigData(
            min = -360f,
            max = 360f,
            setProperty = { component -> component.baseComponent::rotate::set },
            getProperty = { it.baseComponent.rotate }),
    )

    val textValueConfigMap = mapOf(
        "字号" to createEditValueConfigData(
            min = 0f,
            max = 100f,
            setProperty = { component ->
                component.text?.let { it::textSize::set }
                    ?: { _: Float -> "对象异常,数值设置失败".errorNotify(false) }
            },
            getProperty = { it.text?.textSize ?: invalidValue }),
        "间距" to AppWidgetEditValueConfigData(
            min = 0f,
            max = 100f,
            onValueChange = { component ->
                component.text?.let { it::textSpacer::set }
                    ?: { _: Float -> "对象异常,数值设置失败".errorNotify(false) }
            },
            getProperty = { it.text?.textSpacer ?: invalidValue })
    )

    val historyActionButtons = listOf(
        "撤销" to R.drawable.ic_arrow_undo_1,
        "复原" to R.drawable.ic_arrow_redo_1
    )

    val componentActionButtons = listOf(
        Triple("添加", R.drawable.ic_add, Primary),
        Triple("复制", R.drawable.ic_copy_outline, Primary_3),
        Triple("删除", R.drawable.ic_delete, Error)
    )

    //可添加的组件
    val addableComponent = listOf(
        "文本" to AppWidgetComponentType.Text,
        "图片" to AppWidgetComponentType.Image,
        "进度条" to AppWidgetComponentType.ProgressBar,
        "线条" to AppWidgetComponentType.Line,
        "矩形" to AppWidgetComponentType.Rectangle,
        "三角形" to AppWidgetComponentType.Triangle,
        "圆形" to AppWidgetComponentType.Circle,
    )

    //对齐的组件
    val alignButtons = listOf(
        R.drawable.ic_align_top_outline to ComponentAlignType.Top,
        R.drawable.ic_align_center_vertical_outline to ComponentAlignType.CenterVertical,
        R.drawable.ic_align_bottom_outline to ComponentAlignType.Bottom,
        R.drawable.ic_align_left_outline to ComponentAlignType.Start,
        R.drawable.ic_align_center_horizontal_outline to ComponentAlignType.CenterHorizontal,
        R.drawable.ic_align_right_outline to ComponentAlignType.End,
        R.drawable.ic_align_both_horizontal_outline to ComponentAlignType.BothHorizontal,
        R.drawable.ic_align_both_vertical_outline to ComponentAlignType.BothVertical
    )

    val textStyleButtons = listOf(
        R.drawable.ic_text_bold,
        R.drawable.ic_text_italic_1,
        R.drawable.ic_text_underline_1,
        R.drawable.ic_text_strike_through_1
    )

    init {
        scope.launch {
            snapshotFlow { selectedComponents.size }.collect {
                enableAlignButton = it >= 2
                enableAlignButtonBothAxis = it >= 3

                if (it == 1) {
                    val firstComponentType = selectedComponents.first().type

                    showTextConfig = firstComponentType == AppWidgetComponentType.Text
                    showImageConfig = firstComponentType == AppWidgetComponentType.Image
                    showProgressBarConfig = firstComponentType == AppWidgetComponentType.ProgressBar
                }

                dismissBindingFieldPanel()
            }
        }
    }

    private fun createEditValueConfigData(
        min: Float,
        max: Float,
        getProperty: (AppWidgetEditData.Component) -> Float,
        setProperty: (AppWidgetEditData.Component) -> (Float) -> Unit
    ) = AppWidgetEditValueConfigData(
        min = min,
        max = max,
        onValueChange = setProperty,
        getProperty = getProperty
    )

    fun showBindingFieldPanel() {
        showAppWidgetBindingPanel = true
    }

    fun dismissBindingFieldPanel() {
        showAppWidgetBindingPanel = false
    }

    fun showAddComponentPopupWindow() {
        showAddComponentPopupWindow = true
    }

    fun dismissAddComponentPopupWindow() {
        showAddComponentPopupWindow = false
    }

    fun onClickComponent(component: AppWidgetEditData.Component) {
        when (currentSelectionMode) {
            //只保留当前选择的组件
            ComponentSelectType.SingleSelection -> {
                selectedComponents.forEach { it.isSelected = false }

                selectedComponents.clear()
                selectedComponents += component

                component.isSelected = true
            }

            //如果已存在这个组件就移除,否则添加至选择的组件
            ComponentSelectType.MultipleSelection -> {
                val index = selectedComponents.indexOf(component)
                if (index == -1) {
                    selectedComponents += component
                    component.isSelected = true
                } else {
                    selectedComponents.removeAt(index)
                    component.isSelected = false
                }
            }

            else -> {

            }
        }

        dismissBindingFieldPanel()
    }

    //添加组件
    fun onAddComponent(type: AppWidgetComponentType) {
        recordHistory()

        val components = currentEditData.components

        if (components.size >= componentMaxCount) {
            "组件数量已到达最大值,无法继续添加".errorNotify(false)
            return
        }

        components += when (type) {
            AppWidgetComponentType.Text -> {
                AppWidgetEditData.Component(
                    type = type,
                    text = AppWidgetEditData.Component.Text()
                )
            }

            AppWidgetComponentType.Image -> {
                AppWidgetEditData.Component(
                    type = type,
                    image = AppWidgetEditData.Component.Image()
                )
            }

            AppWidgetComponentType.ProgressBar -> {
                AppWidgetEditData.Component(
                    type = type,
                    progressBar = AppWidgetEditData.Component.ProgressBar()
                )
            }

            AppWidgetComponentType.Line -> {
                AppWidgetEditData.Component(type = type, line = AppWidgetEditData.Component.Line())
            }

            AppWidgetComponentType.Rectangle -> {
                AppWidgetEditData.Component(
                    type = type,
                    rectangle = AppWidgetEditData.Component.Rectangle()
                )
            }

            AppWidgetComponentType.Triangle -> {
                AppWidgetEditData.Component(
                    type = type,
                    triangle = AppWidgetEditData.Component.Triangle()
                )
            }

            AppWidgetComponentType.Circle -> {
                AppWidgetEditData.Component(
                    type = type,
                    circle = AppWidgetEditData.Component.Circle()
                )
            }

            else -> {
                AppWidgetEditData.Component(type = type)
            }
        }.apply {
            showBorder = true
        }

        dismissAddComponentPopupWindow()

        println("type = ${type}")

        println(components.toList())
    }

    //删除组件
    fun onDeleteComponent() {
        if (selectedComponents.isEmpty()) return
        recordHistory()

        currentEditData.components.removeAll(selectedComponents)
        selectedComponents.clear()
    }

    //复制组件
    fun onCopyComponent() {
        if (selectedComponents.isEmpty()) return
        recordHistory()

        val exceedLimit =
            currentEditData.components.size + selectedComponents.size > componentMaxCount

        if (exceedLimit) {
            "复制选中的组件后将会超过数量限制,请减少选择的个数后再次尝试复制".warnNotify(false)
            return
        }

        currentEditData.components += selectedComponents.toList()
    }

    //当对齐组件时
    fun onAlignComponent(type: ComponentAlignType) {
        val actionComponents = getSelectedUnLockedComponents()
        if (actionComponents.size <= 1) {
            "必须选择两个及以上的未锁定组件才能够使用对齐".warnNotify(false)
            return
        }

        val set = setOf(ComponentAlignType.BothVertical, ComponentAlignType.BothHorizontal)

        if (set.contains(type) && actionComponents.size <= 2) {
            "必须选择三个及以上的未锁定组件才能够使用垂直/水平对齐".warnNotify(false)
            return
        }

        recordHistory()

        val transform = getComponentsTransformData(actionComponents)

        val sortedXComponents = actionComponents.sortedBy { it.baseComponent.x }
        val sortedYComponents = actionComponents.sortedBy { it.baseComponent.y }

        when (type) {
            //取最小的x来对齐
            ComponentAlignType.Start -> {
                actionComponents.forEach { it.baseComponent.x = transform.minX }
            }

            //取最大的x与最小的x求平均值来获取中心值
            ComponentAlignType.CenterHorizontal -> {
                val lastBase = sortedXComponents.last().baseComponent
                val center = (transform.minX + transform.maxX + lastBase.width.localDpToPx()) / 2

                actionComponents.forEach {
                    val halfWidth = it.baseComponent.width.localDpToPx() / 2
                    it.baseComponent.x = center - halfWidth
                }
            }

            ComponentAlignType.CenterVertical -> {
                val lastBase = sortedYComponents.last().baseComponent
                val center = (transform.minY + transform.maxY + lastBase.height.localDpToPx()) / 2

                actionComponents.forEach {
                    val halfHeight = it.baseComponent.height.localDpToPx() / 2
                    it.baseComponent.y = center - halfHeight
                }
            }

            ComponentAlignType.End -> {
                val lastBase = sortedXComponents.last().baseComponent

                val endX = lastBase.x + lastBase.width.localDpToPx()

                actionComponents.forEach {
                    it.baseComponent.x = endX - it.baseComponent.width.localDpToPx()
                }
            }

            ComponentAlignType.Top -> {
                actionComponents.forEach { it.baseComponent.y = transform.minY }
            }

            ComponentAlignType.Bottom -> {
                val lastBase = sortedYComponents.last().baseComponent

                val endY = lastBase.y + lastBase.height.localDpToPx()

                actionComponents.forEach {
                    it.baseComponent.y = endY - it.baseComponent.height.localDpToPx()
                }
            }

            ComponentAlignType.BothHorizontal -> {
                val count = sortedXComponents.size - 1

                val lastBase = sortedXComponents.last().baseComponent

                val totalWidth = transform.maxX + lastBase.width.localDpToPx() - transform.minX
                val spacer = (totalWidth - transform.sumWidth.localDpToPx()) / count

                var offsetX = transform.minX

                sortedXComponents.forEach {
                    val base = it.baseComponent
                    base.x = offsetX
                    offsetX += spacer + base.width.localDpToPx()
                }
            }

            ComponentAlignType.BothVertical -> {
                val count = sortedYComponents.size - 1

                val lastBase = sortedYComponents.last().baseComponent

                val totalWidth = transform.maxY + lastBase.height.localDpToPx() - transform.minY
                val spacer = (totalWidth - transform.sumHeight.localDpToPx()) / count

                var offsetX = transform.minY

                sortedYComponents.forEach {
                    val base = it.baseComponent
                    base.y = offsetX
                    offsetX += spacer + base.height.localDpToPx()
                }
            }
        }
    }

    fun onClickActionButton(
        resId: Int,
        size: IntSize,
        offset: Offset
    ) = when (resId) {
        R.drawable.ic_delete -> onDeleteComponent()
        R.drawable.ic_add -> {

            popupPositionProvider = PopupWindowPositionProvider(
                contentOffset = offset,
                itemSize = size,
                itemSpace = 3.dp,
                alignBottom = true
            )

            println("offset = ${offset}")
            println("size = ${size}")

            showAddComponentPopupWindow()
        }

        R.drawable.ic_copy_outline -> onCopyComponent()
        else -> {}
    }

    fun onClickLockButton() {
        if (selectedComponents.isEmpty()) return

        recordHistory()

        val lockCount = currentEditData.getUnLockedComponentCount()

        //如果没有全部选中,就设置为选中状态
        val state = when (lockCount) {
            selectedComponents.size -> false
            else -> true
        }

        selectedComponents.forEach {
            it.isLocked = state
        }
    }

    fun getTransformShowValueByName(name: String): Float {
        if (selectedComponents.isEmpty()) return invalidValue

        val firstBase = selectedComponents.first()
        var value = invalidValue

        val configData =
            transformValueConfigMap[name] ?: textValueConfigMap[name] ?: return invalidValue
        if (selectedComponents.all { configData.getShowPropertyPredicate.invoke(it, firstBase) }) {
            value = configData.getProperty.invoke(firstBase)
        }

        return value
    }

    //当组件变换时
    fun onComponentTransform(
        name: String,
        value: Float
    ) {
        val configData = transformValueConfigMap[name] ?: textValueConfigMap[name]

        if (configData == null) {
            "没有找到key=[${name}]的配置".errorNotify()
            return
        }

        val actionComponents = getSelectedUnLockedComponents()
        val realValue = value.coerceAtLeast(configData.min).coerceAtMost(configData.max)

        println("onComponentTransform = ${name} = ${value} real = ${realValue}")

        actionComponents.forEach {
            configData.onValueChange.invoke(it).invoke(realValue)
        }
    }

    fun onDragComponent(offset: Offset) {
        getSelectedUnLockedComponents().forEach {
            val base = it.baseComponent
            base.x += offset.x
            base.y += offset.y
        }
    }

    fun onChangeTextInputValue(
        text: AppWidgetEditData.Component.Text,
        value: String,
        index: Int,
        item: AppWidgetEditData.Base.DataImpl
    ) {
        val realValue = value.take(textMaxLength)

        text.updateDataByIndex(value = realValue, item = item, index = index)
        println("${index}:${value}")
    }

    //改变组件层级
    fun onChangeComponentIndex() {

    }

    //当点击文字样式时
    fun onClickTextStyle(resId: Int) {
        val texts = getUnLockedComponentsByType(AppWidgetComponentType.Text)
        if (texts.isEmpty()) return

        when (resId) {
            R.drawable.ic_text_bold -> {
                val nev = texts.all { it.text?.isBold == true }
                texts.forEach { it.text?.apply { isBold = !nev } }
            }

            R.drawable.ic_text_italic_1 -> {
                val nev = texts.all { it.text?.isItalic == true }
                texts.forEach { it.text?.apply { isItalic = !nev } }
            }

            R.drawable.ic_text_underline_1 -> {
                val nev = texts.all { it.text?.isUnderLine == true }
                texts.forEach { it.text?.apply { isUnderLine = !nev } }
            }

            R.drawable.ic_text_strike_through_1 -> {
                val nev = texts.all { it.text?.isStrikeThrough == true }
                texts.forEach { it.text?.apply { isStrikeThrough = !nev } }
            }
        }
    }

    //当拖动数值编辑器时
    fun onDragEditTransformItem(name: String, value: Float) {
        val components = getSelectedUnLockedComponents()

        val configData = transformValueConfigMap[name] ?: return

        components.forEach {
            val currentValue = configData.getProperty.invoke(it)
            configData.onValueChange.invoke(it).invoke(currentValue + value)
        }
    }

    private fun getComponentsTransformData(
        components: List<AppWidgetEditData.Component>
    ): AppWidgetTransformData {

        if (components.isEmpty()) return AppWidgetTransformData(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

        val firstBase = components.first().baseComponent

        var maxX = firstBase.x
        var minX = firstBase.x
        var maxY = firstBase.y
        var minY = firstBase.y

        var maxWidth = firstBase.width
        var maxHeight = firstBase.height

        var sumWidth = 0f
        var sumHeight = 0f

        getSelectedUnLockedComponents().forEach { component ->
            val base = component.baseComponent

            if (maxX < base.x) {
                maxX = base.x
            }

            if (minX > base.x) {
                minX = base.x
            }

            if (maxY < base.y) {
                maxY = base.y
            }

            if (minY > base.y) {
                minY = base.y
            }

            if (maxWidth < base.width) {
                maxWidth = base.width
            }

            if (maxHeight < base.height) {
                maxHeight = base.height
            }

            sumWidth += base.width
            sumHeight += base.height
        }

        return AppWidgetTransformData(
            maxX = maxX,
            minX = minX,
            maxY = maxY,
            minY = minY,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            sumWidth = sumWidth,
            sumHeight = sumHeight
        )
    }

    //更新历史记录操作按钮的状态
    private fun updateHistoryActionButtonsState() {
        enableHistoryNext = currentHistoryIndex < historyList.size - 1
        enableHistoryPrev = currentHistoryIndex > 0
    }

    //操作撤销,更改当前
    fun historyNext() = changeCurrentEditDataFromHistory(1)
    fun historyPrev() = changeCurrentEditDataFromHistory(-1)

    //获取选择的组件中未锁定的组件
    private fun getSelectedComponentsUnLockedComponentCount() =
        selectedComponents.count { !it.isLocked }

    private fun getSelectedUnLockedComponents() = selectedComponents.filter { !it.isLocked }

    private fun getUnLockedComponents() = currentEditData.components.filter { !it.isLocked }

    private fun getUnLockedComponentsByType(type: AppWidgetComponentType) =
        selectedComponents.filter { !it.isLocked && it.type == type }

    private fun changeCurrentEditDataFromHistory(value: Int) {
        val index = currentHistoryIndex + value
        if (index < 0 || index >= historyList.size) {
            "没有更多的历史记录".warnNotify(false)
            return
        }

        currentEditData = historyList[index]
        currentHistoryIndex = index

        updateHistoryActionButtonsState()
    }

    //更新历史记录
    private fun recordHistory() {

        //当前索引不等于历史记录的最后一条时,代表从历史记录恢复了数据
        //需要在记录前移除后续冲突的记录
        val deleteLastCount = historyList.size - currentHistoryIndex - 1

//        repeat(deleteLastCount) {
//            historyList.removeLast()
//        }
//
//        //确保历史数据最多只有10条,避免内存占用过高
//        val deleteCount = historyList.size - 9
//
//        //清除最早的数据
//        repeat(deleteCount) {
//            historyList.removeFirst()
//        }

        historyList += currentEditData.clone()

        //更新当前历史记录索引
        currentHistoryIndex = historyList.size - 1

        updateHistoryActionButtonsState()
    }

    fun onStartDragSelectComponent(offset: Offset) {
    }

    fun onEndDragSelectComponent() {
    }

    fun onDraggingSelectComponent(offset: Offset, width: Float, height: Float) {
        clearSelectedComponents()
        val actionComponents = getUnLockedComponents()

        val selectedAreaWidth = offset.x + width
        val selectedAreaHeight = offset.y + height

        actionComponents.forEach {
            val base = it.baseComponent
            val baseWidthPx = base.x + base.width.localDpToPx()
            val baseHeightPx = base.y + base.height.localDpToPx()

            it.isSelected = !(base.x > selectedAreaWidth ||
                    baseWidthPx < offset.x ||
                    base.y > selectedAreaHeight ||
                    baseHeightPx < offset.y)

            println("x = ${base.x} y = ${base.y} it.isSelected = ${it.isSelected}")

            if (it.isSelected) {
                selectedComponents += it
            }
        }
    }

    //双击背景,取消全部组件的选中状态
    fun onDoubleClickBackground(offset: Offset) {
        clearSelectedComponents()
    }

    private fun clearSelectedComponents() {
        selectedComponents.forEach { it.isSelected = false }
        selectedComponents.clear()
    }

    private fun Float.localDpToPx() = this.dpToPx(baseDensity)

    fun onAddTextComponentValue() {
        if (selectedComponents.size != 1 || selectedComponents.first().text == null) return

        selectedComponents.first().text?.apply {
            addValue("文本${list.size + 1}")
        }
    }

    fun onBindingField(component: AppWidgetEditData.Component, index: Int) {
        fieldBindingData = AppWidgetFieldBindingData(uuid = component.uuid, index)

        showBindingFieldPanel()
    }

    fun updateComponentBindingFiled(appWidgetDataType: AppWidgetDataType, key: String) {
        if (!this::fieldBindingData.isInitialized) return

        val component =
            currentEditData.components.takeFirstIf { it.uuid == fieldBindingData.uuid } ?: return

        if (component.baseComponent.list.size <= fieldBindingData.index) return

        val data = component.baseComponent.list[fieldBindingData.index].copy(
            dataType = appWidgetDataType,
            key = key
        )

        component.baseComponent.updateDataByIndex(data = data, index = fieldBindingData.index)

        dismissBindingFieldPanel()
    }

    fun onDeleteBindingField(component: AppWidgetEditData.Component, index: Int) {
        component.baseComponent.list.removeAt(index)
    }

}