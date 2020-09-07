package restaurant_manager.employee

import org.apache.commons.lang.math.NumberUtils
import org.apache.commons.validator.routines.EmailValidator
import restaurant_manager.others.Defaults
import restaurant_manager.others.PhoneNumber
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class EmployeeAddDto {

    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var position: String? = null
    var salary: BigDecimal? = null
    var startDate: LocalDate? = null
    var phoneNumber: PhoneNumber? = null

    constructor()

    constructor(firstName: String,
            lastName: String,
            email: String,
            position: String,
            salary: BigDecimal,
            startDate: LocalDate,
            phoneNumber: PhoneNumber) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.position = position
        this.salary = salary
        this.startDate = startDate
        this.phoneNumber = phoneNumber
    }

    private fun isValidFirstName(): Boolean =
            (Objects.nonNull(firstName)
                    && firstName!!.isBlank().not()
                    && Character.isUpperCase(firstName!!.codePointAt(0)))

    private fun isValidLastName(): Boolean =
            (Objects.nonNull(lastName)
                    && lastName!!.isBlank().not()
                    && Character.isUpperCase(lastName!!.codePointAt(0)))

    private fun isValidEmail(): Boolean =
            (Objects.nonNull(email)
                    && EmailValidator.getInstance().isValid(email))

    private fun isValidPosition(): Boolean =
            (Objects.nonNull(position)
                    && position!!.isBlank().not()
                    && Character.isUpperCase(position!!.codePointAt(0)))

    private fun isValidSalary(): Boolean =
            (Objects.nonNull(salary)
                    && salary!! >= BigDecimal(Defaults.DEFAULT_SALARY))

    private fun isValidStartDate(): Boolean =
            (Objects.nonNull(startDate)
                    && startDate!! >= LocalDate.now())

    private fun isValidPhoneNumber(): Boolean =
            (Objects.nonNull(phoneNumber)
                    && NumberUtils.isDigits(phoneNumber!!.phoneNumber)
                    && phoneNumber!!.phoneNumber.length == Defaults.PHONE_NUMBER_LENGTH)

    fun isValid(): Boolean =
            (isValidFirstName() && isValidLastName()
                    && isValidEmail() && isValidPosition()
                    && isValidSalary() && isValidStartDate()
                    && isValidPhoneNumber())

    override fun toString(): String {
        return "EmployeeAddDto(firstName=$firstName, lastName=$lastName, email=$email, position=$position, salary=$salary, startDate=$startDate, phoneNumber=$phoneNumber)"
    }

}
