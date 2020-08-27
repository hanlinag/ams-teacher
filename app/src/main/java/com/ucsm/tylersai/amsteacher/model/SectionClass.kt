package com.ucsm.tylersai.amsteacher.model

data class SectionClass(
    var teachingsubject :String,
    var year: String,
    var sectionname: String
){
    constructor():this("","","")
}