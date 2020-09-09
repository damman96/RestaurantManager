package restaurant_manager.product

import java.math.BigDecimal
import java.util.*

class ProductAddDto {

    var name: String? = null
    var description: String? = null
    var price: BigDecimal? = null
    var category: String? = null
    var subcategory: String? = null

    constructor()

    constructor(name: String,
            description: String,
            price: BigDecimal,
            category: String,
            subcategory: String) {
        this.name = name
        this.description = description
        this.price = price
        this.category = category
        this.subcategory = subcategory
    }

    fun isValid(): Boolean = (
            isValidName()
                    && isValidDescription()
                    && isValidPrice()
                    && isValidCategory()
                    && isValidSubcategory())

    override fun toString(): String =
            "ProductAddDto(name=$name, description=$description, price=$price, category=$category, subcategory=$subcategory)"

    private fun isValidName(): Boolean =
            (Objects.nonNull(name)
                    && name!!.isBlank().not()
                    && Character.isUpperCase(name!!.codePointAt(0)))

    private fun isValidDescription(): Boolean =
            (Objects.nonNull(description)
                    && Character.isUpperCase(description!!.codePointAt(0)))

    private fun isValidPrice(): Boolean =
            (Objects.nonNull(price)
                    && price!! >= BigDecimal(0))

    private fun isValidCategory(): Boolean =
            (Objects.nonNull(this.category)
                    && this.category!!.isBlank().not()
                    && Character.isUpperCase(this.category!!.codePointAt(0)))

    private fun isValidSubcategory(): Boolean =
            (Objects.nonNull(subcategory)
                    && subcategory!!.isBlank().not()
                    && Character.isUpperCase(subcategory!!.codePointAt(0)))

}
