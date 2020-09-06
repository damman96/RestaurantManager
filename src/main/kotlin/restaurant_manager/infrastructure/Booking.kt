package restaurant_manager.infrastructure

import org.hibernate.annotations.ColumnDefault
import restaurant_manager.others.PhoneNumber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "bookings")
data class Booking(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ColumnDefault("now()::timestamp::date")
        var date: LocalDate,

        @ColumnDefault("now()::timestamp::time")
        var time: LocalTime,

        @ColumnDefault("now()")
        var dateTimeOfBooking: LocalDateTime,

        var personalData: String,

        @Embedded
        var phoneNumber: PhoneNumber,

        var details: String,

        @ManyToOne
        @JoinColumn(name = "board_id", nullable = false)
        var board: Board,

        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        var employee: Employee

)
