package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.viewmodel

import android.content.Intent
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateProject
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.scope.withContextMain
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
import com.lianyi.paimonsnotebook.ui.screen.setting.components.widgets.SettingsOptionSwitch
import com.lianyi.paimonsnotebook.ui.screen.setting.data.OptionListData
import com.lianyi.paimonsnotebook.ui.theme.Primary_2

class CultivateProjectOptionScreenViewModel : ViewModel() {

    var currentSelectedProject: CultivateProject? by mutableStateOf(null)
        private set

    private val cultivateProjectDao = PaimonsNotebookDatabase.database.cultivateProjectDao
    private val cultivateEntityDao = PaimonsNotebookDatabase.database.cultivateEntityDao

    val cultivateProjectList = mutableStateListOf<CultivateProject>()

    private var cultivateProjectSortByEntityType by mutableStateOf(false)

    init {
        viewModelScope.launchIO {
            launchIO {
                cultivateProjectDao.getSelectedProjectFlow().collect {
                    withContextMain {
                        currentSelectedProject = it
                    }
                }
            }
            launchIO {
                cultivateProjectDao.getCultivateProjectListFlow().collect {
                    withContextMain {
                        cultivateProjectList.clear()
                        cultivateProjectList += it
                    }
                }
            }

            launchIO {
                dataStoreValues {
                    withContextMain {
                        cultivateProjectSortByEntityType =
                            it[PreferenceKeys.CultivateProjectSortByEntityType] ?: false
                    }
                }
            }
        }
    }

    var showChooseCultivateProjectDialog by mutableStateOf(false)
        private set
    var showConfirmDeleteCultivateProjectDialog by mutableStateOf(false)
        private set

    var showConfirmDeleteCultivateProjectSuccessEntityDialog by mutableStateOf(false)
        private set

    var showAddCultivateProjectDialog by mutableStateOf(false)
        private set

    private var deleteSelectedCultivateProject = false


    //选中的养成计划
    var dialogSelectedCultivateProject: CultivateProject? = null
        private set

    val cultivateProjectSettings = listOf(
        OptionListData(
            name = "当前养成计划",
            description = "用于显示不同的养成计划",
            onClick = {
                showChooseCultivateProjectDialog = true
            },
            slot = {
                Text(
                    text = currentSelectedProject?.projectName ?: "",
                    fontSize = 14.sp,
                    color = Primary_2
                )
            }
        ),
        OptionListData(
            name = "添加养成计划",
            description = "添加一个新的养成计划",
            onClick = {
                showAddCultivateProjectDialog = true
            },
            slot = {
                Text(
                    text = dialogSelectedCultivateProject?.projectName ?: "",
                    fontSize = 14.sp,
                    color = Primary_2
                )
            }
        ),
        OptionListData(
            name = "删除养成计划",
            description = "删除养成计划",
            onClick = {
                showChooseCultivateProjectDialog = true
                deleteSelectedCultivateProject = true
            },
            slot = {
                Text(
                    text = dialogSelectedCultivateProject?.projectName ?: "",
                    fontSize = 14.sp,
                    color = Primary_2
                )
            }
        )
    )

    val cultivateProjectLayout = listOf(
        OptionListData(
            name = "养成计划列表按照类型排序",
            description = "默认关闭,开启后,养成计划养成中的养成项列表将按照先角色后武器的形式排序;默认根据添加的顺序进行排序",
            onClick = {
                cultivateProjectSortByEntityType = !cultivateProjectSortByEntityType

                viewModelScope.launchIO {
                    PreferenceKeys.CultivateProjectSortByEntityType.editValue(
                        cultivateProjectSortByEntityType
                    )
                }
            },
            slot = {
                SettingsOptionSwitch(checked = cultivateProjectSortByEntityType)
            }
        )
    )

    val cultivateProjectActions = listOf(
        OptionListData(
            name = "删除所有完成的养成项",
            description = "删除当前养成计划中,所需材料全部收集完毕的养成项",
            onClick = {
                showConfirmDeleteCultivateProjectSuccessEntityDialog = true
            }
        )
    )

    fun init(intent: Intent?) {
        showAddCultivateProjectDialog = intent?.getBooleanExtra("add", false) ?: false
    }

    fun dismissConfirmDeleteCultivateProjectDialog() {
        showConfirmDeleteCultivateProjectDialog = false
    }

    fun dismissChooseCultivateProjectDialog() {
        showChooseCultivateProjectDialog = false
        deleteSelectedCultivateProject = false
    }

    fun dismissConfirmDeleteCultivateProjectSuccessEntityDialog() {
        showConfirmDeleteCultivateProjectSuccessEntityDialog = false
    }

    fun confirmDeleteCultivateProjectSuccessEntity() {
        dismissConfirmDeleteCultivateProjectSuccessEntityDialog()
        val projectId = currentSelectedProject?.projectId ?: -1

        if (projectId == -1) {
            "你还没有设置选中的养成计划".warnNotify(false)
            return
        }

        viewModelScope.launchIO {
            val res =
                cultivateEntityDao.deleteAllSuccessEntityByProjectId(projectId = projectId)

            "从养成计划[${currentSelectedProject?.projectName}]中删除了${res}个完成的养成项".notify()
        }
    }

    fun confirmDeleteChooseCultivateProject() {
        dismissConfirmDeleteCultivateProjectDialog()

        val projectId = dialogSelectedCultivateProject?.projectId ?: -1

        if (projectId == -1) {
            "删除失败:没有选中用户".warnNotify()
            return
        }

        viewModelScope.launchIO {
            cultivateProjectDao.deleteProjectById(projectId)

            "养成计划[${dialogSelectedCultivateProject?.projectName}]已删除".notify()

            dialogSelectedCultivateProject = null
            deleteSelectedCultivateProject = false

            dismissConfirmDeleteCultivateProjectDialog()
        }
    }

    fun onSelectedProject(project: CultivateProject) {
        dialogSelectedCultivateProject = project

        showConfirmDeleteCultivateProjectDialog = deleteSelectedCultivateProject

        if (!deleteSelectedCultivateProject) {
            changeCurrentSelectedCultivateProject()
        }

        dismissChooseCultivateProjectDialog()
    }

    private fun changeCurrentSelectedCultivateProject() {
        val projectId = dialogSelectedCultivateProject?.projectId ?: -1

        if (projectId == -1) {
            "切换失败:没有选中用户".warnNotify()
            return
        }

        if (projectId == currentSelectedProject?.projectId) {
            "当前显示的已经是[${currentSelectedProject?.projectName}]".notify()
            return
        }

        viewModelScope.launchIO {

            if (currentSelectedProject != null) {
                cultivateProjectDao.updateSelectedProject(
                    unSelectProjectId = currentSelectedProject?.projectId ?: -1,
                    selectProjectId = projectId
                )
            } else {
                cultivateProjectDao.setSelectedProjectById(projectId = projectId)
            }

            "当前显示的是[${dialogSelectedCultivateProject?.projectName}]".notify()
        }
    }

    fun onClickChooseCultivateProjectDialogButton(i: Int) {
        dismissChooseCultivateProjectDialog()
    }

    fun confirmAddProjectCultivateProject(value: String) {
        if (value.isBlank()) {
            "还没有输入养成计划名称".show()
            return
        }

        viewModelScope.launchIO {
            val hasSameName = cultivateProjectDao.hasSameNameCultivateProject(value)

            if (hasSameName) {
                withContextMain {
                    "已经有相同名称的养成计划".show()
                }
                return@launchIO
            }

            cultivateProjectDao.insert(
                CultivateProject(
                    projectName = value,
                    isSelected = currentSelectedProject == null
                )
            )

            "养成计划[${value}]添加成功".notify()

            dismissAddCultivateProjectDialog()
        }
    }

    fun dismissAddCultivateProjectDialog() {
        showAddCultivateProjectDialog = false
    }
}