package restaurant_manager.enums

import java.util.*

enum class Position(private val position: String) {

    WAITER("Waiter"),
    MANAGER("Manager"),
    WAITRESS("Waitress"),
    NEW_EMPLOYEE("New employee");

    fun getByValue(value: String): Position {
        return Arrays.stream(values())
                .filter { p: Position -> p.position == value }
                .findFirst()
                .orElse(NEW_EMPLOYEE)
    }
}
