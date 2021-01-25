package natec.androidapp.mycalc.ui.convert

/**
 * A class to handle all unit conversions
 */

class ConvertHelper {
    private val unitMaps = UnitMaps()

    fun convert(cType: String, cVal: Double, iUnit: String, oUnit: String): Double{
        if(iUnit == oUnit){
            return cVal
        }

        return when(cType) {
            "Length" -> (cVal * unitMaps.lengthMap[iUnit]!!) / unitMaps.lengthMap[oUnit]!!
            "Area" -> (cVal * unitMaps.areaMap[iUnit]!!) / unitMaps.areaMap[oUnit]!!
            "Data" -> (cVal * unitMaps.dataMap[iUnit]!!) / unitMaps.dataMap[oUnit]!!
            "Mass" -> (cVal * unitMaps.massMap[iUnit]!!) / unitMaps.massMap[oUnit]!!
            "Speed" -> (cVal * unitMaps.speedMap[iUnit]!!) / unitMaps.speedMap[oUnit]!!
            "Temperature" -> convertTemp(cVal, iUnit, oUnit)
            "Time" -> (cVal * unitMaps.timeMap[iUnit]!!) / unitMaps.timeMap[oUnit]!!
            "Volume" -> (cVal * unitMaps.volMap[iUnit]!!) / unitMaps.volMap[oUnit]!!
            else -> 0.0
        }
    }

    private fun convertTemp(cVal: Double, iUnit: String, oUnit: String): Double{
        //tmp holds both conversions of the temperature
        val tmp: Pair<Double, Double> = when(iUnit){
            "Fahrenheit" -> {
                val c = ((cVal - 32.0) * (5.0/9.0))
                val k = c + 273.15
                Pair(c, k)
            }
            "Celsius" ->{
                val f = (cVal * (9.0/5.0)) + 32.0
                val k = cVal + 273.15
                Pair(f, k)
            }
            "Kelvin" ->{
                val c = cVal - 273.15
                val f = (c * (9.0/5.0)) + 32.0
                Pair(c, f)
            }
            else -> Pair(0.0, 0.0)
        }

        //if the input units is not celsius then the celsius conversion is the first value in the pair
        return if(iUnit != "Celsius" && oUnit == "Celsius"){
            tmp.first
        }else if (iUnit != "Celsius"){
            tmp.second
        // last 2 conditions are for when iUnit is Celsius
        }else if(oUnit == "Fahrenheit"){
            tmp.first
        }else{
            tmp.second
        }
    }
}