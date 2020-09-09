package restaurant_manager.board

import java.util.*

class BoardEditDto {

    var numberOfSeats: Int? = null
    var description: String? = null
    var employeeId: Long? = null

    constructor()

    constructor(numberOfSeats: Int?,
            description: String?,
            employeeId: Long?) {
        this.numberOfSeats = numberOfSeats
        this.description = description
        this.employeeId = employeeId
    }


    private fun isValidNumberOfSeats(): Boolean = Objects.nonNull(numberOfSeats)

    private fun isValidDescription(): Boolean = (Objects.nonNull(description)
            && description!!.isBlank().not()
            && Character.isUpperCase(description!!.codePointAt(0)))

    private fun isValidEmployeeId(): Boolean = Objects.nonNull(employeeId)

    fun isValid(): Boolean = (
            isValidNumberOfSeats()
                    && isValidDescription()
                    && isValidEmployeeId())

    override fun toString(): String =
            "BoardEditDto(" +
                    "numberOfSeats=$numberOfSeats," +
                    " description=$description," +
                    " employeeId=$employeeId" +
                    ")"


}
