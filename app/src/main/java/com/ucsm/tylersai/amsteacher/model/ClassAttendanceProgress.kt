package com.ucsm.tylersai.amsteacher.model

data class ClassAttendanceProgress(
    var className: String,
    var overallPercentage: String
){
    constructor():this("","")
}