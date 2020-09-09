package restaurant_manager.product

import org.springframework.stereotype.Component
import restaurant_manager.enums.Category
import restaurant_manager.enums.Subcategory
import restaurant_manager.infrastructure.Product
import restaurant_manager.others.Price

@Component
class ProductRepository {

    fun mapProductToProductDetailDto(product: Product): ProductDetailDto =
            ProductDetailDto(
                    name = product.name,
                    description = product.description,
                    price = product.price.price,
                    category = product.category.name,
                    subcategory = product.subcategory.name
            )

    fun mapProductToProductAddDto(dto: ProductAddDto): Product =
            Product(
                    id = null,
                    name = dto.name!!,
                    description = dto.description!!,
                    price = Price(dto.price!!),
                    category = Category.getByValue(dto.category!!),
                    subcategory = Subcategory.getByValue(dto.subcategory!!)
            )

    fun mapProductEditDtoToProduct(product: Product, dto: ProductEditDto): Product =
            product.copy(
                    id = product.id,
                    name = dto.name!!,
                    description = dto.description!!,
                    price = Price(dto.price!!),
                    category = Category.getByValue(dto.category!!),
                    subcategory = Subcategory.getByValue(dto.subcategory!!)
            )

}
