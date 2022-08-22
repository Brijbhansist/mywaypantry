package com.fabwalley.food.utils

import java.text.DecimalFormat
import java.util.*

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */

fun Double.getTwoDigitVlaue(): String? {
    val a: Double =
        DecimalFormat("##.##").format(this).toDouble()
    return String.format(Locale.US, "%.2f", a)
}

fun String.escapeMetaCharacters(): String? {
    var inputString = this
    val metaCharacters = arrayOf(
        "\\",
        "^",
        "$",
        "{",
        "}",
        "[",
        "]",
        "(",
        ")",
        ".",
        "*",
        "+",
        "?",
        "|",
        "<",
        ">",
        "-",
        "&",
        "%"
    )
    for (i in metaCharacters.indices) {
        if (inputString.contains(metaCharacters[i])) {
            inputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i])
        }
    }
    return inputString
}