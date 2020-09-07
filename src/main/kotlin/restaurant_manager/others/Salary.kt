package restaurant_manager.others

import org.hibernate.annotations.ColumnDefault
import java.math.BigDecimal
import javax.persistence.Embeddable

@Embeddable
class Salary(@ColumnDefault("2700.00") internal var salary: BigDecimal) {

    override fun toString(): String {
        return "Salary(salary=$salary)"
    }
}
