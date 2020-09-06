package restaurant_manager.enums

import java.util.*

enum class PaymentMethod(private val paymentMethod: String) {

    CASH("Cash"),
    CARD("Card"),
    EMPTY("Empty");

    fun getByValue(value: String): PaymentMethod {
        return Arrays.stream(values())
                .filter { p: PaymentMethod -> p.paymentMethod == value }
                .findFirst()
                .orElse(EMPTY)
    }
}
