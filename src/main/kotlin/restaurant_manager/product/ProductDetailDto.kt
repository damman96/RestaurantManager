package restaurant_manager.product

import java.math.BigDecimal

data class ProductDetailDto(

        var name: String? = null,
        var description: String? = null,
        var price: BigDecimal? = null,
        var category: String? = null,
        var subcategory: String? = null
) {
    override fun toString(): String =
            "ProductDetailDto(name=$name, description=$description, price=$price, category=$category, subcategory=$subcategory)"
}
