package restaurant_manager.infrastructure

import org.springframework.data.repository.CrudRepository

interface ProductDAO : CrudRepository<Product, Long> {

    fun findAllByOrderByIdAsc(): List<Product>
}
