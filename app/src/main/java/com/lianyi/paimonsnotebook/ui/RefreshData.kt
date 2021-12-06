package com.lianyi.paimonsnotebook.ui

import com.lianyi.paimonsnotebook.config.JsonCacheName
import com.lianyi.paimonsnotebook.config.URL
import com.lianyi.paimonsnotebook.util.Ok
import com.lianyi.paimonsnotebook.util.ok
import com.lianyi.paimonsnotebook.util.sp


//刷新本地缓存数据
class RefreshData {
    companion object{

        fun getBanner(block:(Boolean)->Unit){
            Ok.get(URL.HOME_BANNER){
                if(it.ok){
                    val edit = sp.edit()
                    edit.putString(JsonCacheName.BANNER_CACHE,it.toString())
                    edit.apply()
                }
                block(it.ok)
            }
        }

        fun getBlackBoard(block: (Boolean) -> Unit){
            Ok.get(URL.BLACK_BOARD){
                if(it.ok){
                    val edit = sp.edit()
                    edit.putString(JsonCacheName.BLACK_BOARD,it.toString())
                    edit.apply()
                }
                block(it.ok)
            }
        }



    }
}