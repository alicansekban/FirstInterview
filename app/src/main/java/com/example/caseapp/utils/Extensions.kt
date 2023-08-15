package com.example.caseapp.utils

import java.text.SimpleDateFormat
import java.util.Date

val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
fun String.toDate() : Date? {
    return dateFormatter.parse(this)
}

fun Date.toParsedString() : String {
    return dateFormatter.format(this)
}