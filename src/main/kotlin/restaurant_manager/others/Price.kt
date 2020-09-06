package restaurant_manager.others

import org.hibernate.annotations.ColumnDefault
import java.math.BigDecimal
import javax.persistence.Embeddable

@Embeddable
class Price(@ColumnDefault("0.00") private var price: BigDecimal) {

    override fun toString(): String {
        return "Price(price=$price)"
    }
}
