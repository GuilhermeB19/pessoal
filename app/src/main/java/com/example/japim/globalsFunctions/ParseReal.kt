package com.example.japim.globalsFunctions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

fun Double.parseReal() : String{
    val simbols = DecimalFormatSymbols(Locale.forLanguageTag("pt-BR"));
    val parse = DecimalFormat("#,##0.00",simbols)
    return parse.format(this);
}