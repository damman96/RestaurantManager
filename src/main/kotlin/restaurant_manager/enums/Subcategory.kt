package restaurant_manager.enums

import java.util.*

enum class Subcategory(internal val subcategory: String) {
    EMPTY("Empty"),
    BEERS("Beers"),
    VODKAS("Vodkas"),
    WHISKEYS("Whiskeys");

    companion object {
        fun getByValue(value: String): Subcategory {
            return Arrays.stream(values())
                    .filter { c: Subcategory -> c.subcategory == value }
                    .findFirst()
                    .orElse(EMPTY)
        }
    }
}
