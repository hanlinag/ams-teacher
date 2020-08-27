package com.ucsm.tylersai.amsteacher.model

data class Teacher(
    var id:String,
    var name:String,
    var email:String,
    var phone:String,
    var password: String,
    var address:String,
    var profileurl: String,
    var isdean: String
){
    constructor(): this("","","","","","","", "")
}