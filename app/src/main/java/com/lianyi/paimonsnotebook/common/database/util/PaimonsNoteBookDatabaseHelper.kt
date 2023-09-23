package com.lianyi.paimonsnotebook.common.database.util

import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache

object PaimonsNoteBookDatabaseHelper {

    //更新图片信息
    fun updateImageUseInfo(diskCache:DiskCache){
        PaimonsNotebookDatabase.database.diskCacheDao.let { dao ->
            diskCache.apply {
                if (dao.queryDataCountByUrl(url) > 0) {
                    dao.updateUseInfo(url,lastUseFrom, lastUseTime)
                } else {
                    dao.insert(this)
                }
            }
        }
    }
}