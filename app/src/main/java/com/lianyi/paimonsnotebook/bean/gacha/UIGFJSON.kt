package com.lianyi.paimonsnotebook.bean.gacha

class UIGFJSON(val info:UIGFInfo,val list: List<UIGFJsonBean>) {

    class UIGFInfo(val uid:String,val lang:String,val export_time:String,val export_timestamp:String,val export_app:String,val export_app_version:String,val uigf_version:String){
        override fun toString(): String {
            return "UIGFInfo(uid='$uid', lang='$lang', export_time='$export_time', export_timestamp='$export_timestamp', export_app='$export_app', export_app_version='$export_app_version', uigf_version='$uigf_version')"
        }
    }

    override fun toString(): String {
        return "UIGFJSON(info=$info, list=$list)"
    }
}