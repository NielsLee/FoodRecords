package lying.fengfeng.foodrecords.ui.theme

enum class ThemeOptions(val int: Int) {
    DYNAMIC(0),
    RED(1),
    YELLOW(2),
    GREEN(3),
    BLUE(4),
    MAGENTA(5);

    companion object {
        fun fromInt(value: Int): ThemeOptions {
            return entries.find { it.int == value } ?: DYNAMIC
        }
    }
}