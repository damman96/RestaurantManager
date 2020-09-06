package restaurant_manager.enums

import java.util.*

enum class Subcategory(private val subcategory: String) {
    EMPTY("Empty"),
    BEERS("Beers"),
    VODKAS("Vodkas"),
    WHISKEYS("Whiskeys");

    fun getByValue(value: String): Subcategory {
        return Arrays.stream(values())
                .filter { c: Subcategory -> c.subcategory == value }
                .findFirst()
                .orElse(EMPTY)
    }
}
