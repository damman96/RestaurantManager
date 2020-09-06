package restaurant_manager.infrastructure

import org.hibernate.annotations.ColumnDefault
import restaurant_manager.others.Amount
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "order_details")
class OrderDetails(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var quantity: BigDecimal,

        @Embedded
        var amount: Amount,

        @ManyToOne
        @JoinColumn(name = "product_id", nullable = false)
        var product: Product,

        @ManyToOne
        @JoinColumn(name = "order_id", nullable = false)
        var order: Order

)
