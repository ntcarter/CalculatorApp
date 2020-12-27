package natec.androidapp.mycalc

fun String.insert(index: Int, string: String): String {
    return this.substring(0, index) + string + this.substring(index, this.length)
}