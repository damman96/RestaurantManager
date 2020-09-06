package restaurant_manager.infrastructure

import org.hibernate.annotations.ColumnDefault
import restaurant_manager.enums.Category
import restaurant_manager.enums.Subcategory
import restaurant_manager.others.Price
import javax.persistence.*

@Entity
@Table(name = "products")
class Product(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var name: String,

        var description: String,

        @Embedded
        var price: Price,

        @Enumerated(EnumType.STRING)
        var category: Category,

        @Enumerated(EnumType.STRING)
        var subcategory: Subcategory

)
