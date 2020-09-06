package restaurant_manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RestaurantManagerApplication

fun main(args: Array<String>) {
    runApplication<RestaurantManagerApplication>(*args)
}
