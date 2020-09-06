package restaurant_manager.others

import org.hibernate.annotations.ColumnDefault
import java.math.BigDecimal
import javax.persistence.Embeddable

@Embeddable
class Amount(@ColumnDefault("0.00") private var amount: BigDecimal) {

    override fun toString(): String {
        return "Amount(amount=$amount)"
    }
}
