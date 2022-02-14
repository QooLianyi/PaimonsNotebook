package com.lianyi.paimonsnotebook.bean.account


class UserBean(var nickName:String,
               var loginUid:String,
               var region:String,
               var regionName:String,
               var gameUid:String,
               var lToken:String,
               var cookieToken:String,
               var gameLevel:Int
               ) {

    constructor():this("","","","","","","",0)

    override fun toString(): String {
        return "UserBean(nickName='$nickName', loginUid='$loginUid', region='$region', regionName='$regionName', gameUid='$gameUid', lToken='$lToken', cookieToken='$cookieToken', gameLevel=$gameLevel)"
    }

    fun isNull():Boolean{
        return loginUid.isEmpty()||region.isEmpty()||cookieToken.isEmpty()||lToken.isEmpty()
    }

}