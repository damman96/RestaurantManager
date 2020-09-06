package restaurant_manager.infrastructure

import javax.persistence.*

@Entity
@Table(name = "boards")
data class Board(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var numberOfSeats: Int,

        var description: String,

        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        var employee: Employee

)
