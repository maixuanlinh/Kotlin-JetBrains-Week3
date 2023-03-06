package nicestring

fun String.isNice(): Boolean {
    var niceConditions = 0
    val vowels = setOf('a', 'e', 'i', 'o', 'u')

    if (!this.contains("bu") && !this.contains("ba") && !this.contains("be")) {
        niceConditions++
    }

    if (this.count { vowels.contains(it) } >= 3) {
        niceConditions++
    }

    if (this.windowed(2).any { it[0] == it[1] }) {
        niceConditions++
    }

    return niceConditions >= 2
}
