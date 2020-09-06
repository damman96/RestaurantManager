package restaurant_manager.enums

import java.util.*

enum class Category(private val category: String) {

    EMPTY("Empty"),
    SOUPS("Soups"),
    DRINKS("Drinks"),
    ALCOHOLS("Alcohols"),
    DESSERTS("Desserts"),
    APPETIZERS("Appetizers"),
    MAIN_DISHES("Main dishes");

    fun getByValue(value: String): Category {
        return Arrays.stream(values())
                .filter { c: Category -> c.category == value }
                .findFirst()
                .orElse(EMPTY)
    }
}
