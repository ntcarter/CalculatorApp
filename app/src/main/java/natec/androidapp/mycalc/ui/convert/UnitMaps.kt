package natec.androidapp.mycalc.ui.convert

/**
 * Class to store all the maps for units conversions
 * All values are in relation to a base value
 * The base value is the unit to 1.0
 */
class UnitMaps {

    var lengthMap = mapOf(
        "Kilometer" to 1000.0,
        "Meter" to 1.0,
        "Centimeter" to 0.01,
        "Millimeter" to 0.001,
        "Nanometer" to 0.000000001,
        "Mile" to 1609.3445,
        "Yard" to 0.9144,
        "Foot" to 0.3048,
        "Inch" to 0.02534
    )

    var areaMap = mapOf(
        "Square Kilometer" to 1000000.0,
        "Square Meter" to 1.0,
        "Square Mile" to 2590002.59,
        "Square Feet" to 0.0929,
        "Square Inch" to 0.000645,
        "Square Yard" to 0.83612,
        "Acre" to 4046.8627,
        "Hectare" to 10000.0
    )

    var dataMap = mapOf(
        "Bit" to 0.000001,
        "Kilobit" to 0.001,
        "Megabit" to 1.0,
        "Gigabit" to 1000.0,
        "Terabit" to 1000000.0,
        "Petabit" to 1000000000.0,
        "Byte" to 0.000008,
        "Kilobyte" to 0.008 ,
        "Megabyte" to 8.0,
        "Gigabyte" to 8000.0,
        "Terabyte" to 8000000.0,
        "Petabyte" to 8000000000.0
    )

    var massMap = mapOf(
        "Kilogram" to 1000.0,
        "Gram" to 1.0 ,
        "Milligram" to 0.001,
        "Microgram" to 0.000001,
        "Stone" to 6350.2949712,
        "Pounds" to 453.592497943,
        "Ounce" to 28.3495311214
    )

    var speedMap = mapOf(
        "Miles per Hour" to 0.44703925898,
        "Feet per Second" to 0.30479999024,
        "Kilometers per Hour" to 0.2777778,
        "Meters per Second" to 1.0
    )

    var timeMap = mapOf(
        "Nanosecond" to 0.000000001,
        "Millisecond" to 0.01,
        "Second" to 1.0,
        "Minute" to 60.0,
        "Hour" to 3600.0,
        "Day" to 86400.0,
        "Week" to 604800.0,
        "Month" to 26280028.8,
        "Year" to 31536000.0
    )

    var volMap = mapOf(
        "Cubic Inch" to 0.0163870758,
        "Cubic Foot" to 28.3168199079,
        "Cubic Meter" to 1000.0,
        "Liter" to 1.0,
        "Milliliter" to 0.01,
        "(US) Gallon" to 3.78541253426,
        "(US) Quart" to 0.94635134239,
        "(US) Pint" to 0.4731756712,
        "(US) Cup" to 0.239999808,
        "(US) Tablespoon" to 0.01478675,
        "(US) Teaspoon" to 0.0049289249,
        "Gallon" to 4.54609513159,
        "Quart" to 1.13652249121,
        "Pint" to 0.5682612871,
        "Cup" to 0.28413059971,
        "Tablespoon" to 0.01775817138,
        "Teaspoon" to 0.00591939046,
    )
}