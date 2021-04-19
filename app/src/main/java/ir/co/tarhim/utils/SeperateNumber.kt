package ir.co.tarhim.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class SeperateNumber {
    public fun splitDigit(number:Int):String{
        var format=DecimalFormat("#,###")
        var symbol=format.decimalFormatSymbols
        symbol.groupingSeparator=','
        format.decimalFormatSymbols=symbol
        return format.format(number)
    }
}