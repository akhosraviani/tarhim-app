package ir.co.mazar.utils

import java.text.DecimalFormat

class SeperateNumber {
    public fun splitDigit(number:Int):String{
        var format=DecimalFormat("#,###")
        var symbol=format.decimalFormatSymbols
        symbol.groupingSeparator=','
        format.decimalFormatSymbols=symbol
        return format.format(number)
    }
}