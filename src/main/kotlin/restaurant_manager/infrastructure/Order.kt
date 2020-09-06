package restaurant_manager.infrastructure

import org.hibernate.annotations.ColumnDefault
import restaurant_manager.enums.PaymentMethod
import restaurant_manager.others.Amount
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "orders")
class Order(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Embedded
        var amount: Amount,

        @Enumerated(EnumType.STRING)
        var paymentMethod: PaymentMethod,

        @ColumnDefault("now()")
        var orderDateTime: LocalDateTime,

        @ManyToOne
        @JoinColumn(name = "board_id", nullable = false)
        var board: Board,

        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        var employee: Employee

)
