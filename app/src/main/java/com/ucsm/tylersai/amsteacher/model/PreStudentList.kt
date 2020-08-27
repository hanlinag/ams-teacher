package com.ucsm.tylersai.amsteacher.model

data class PreStudentList(
    var mkpt: String,
    var major: String,
    var name: String
){

    constructor(): this("","","")
}