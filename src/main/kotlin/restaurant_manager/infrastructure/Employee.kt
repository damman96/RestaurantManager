package restaurant_manager.infrastructure

import org.hibernate.annotations.ColumnDefault
import restaurant_manager.enums.Position
import restaurant_manager.others.PhoneNumber
import restaurant_manager.others.Salary
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "employees")
class Employee(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var firstName: String,

        var lastName: String,

        var email: String,

        @Enumerated(EnumType.STRING)
        var position: Position,

        @Embedded
        var salary: Salary,

        @ColumnDefault("now()::timestamp::date")
        var startDate: LocalDate,

        var phoneNumber: PhoneNumber

)
