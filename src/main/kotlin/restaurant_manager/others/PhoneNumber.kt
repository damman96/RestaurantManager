package restaurant_manager.others

import javax.persistence.Embeddable

@Embeddable
class PhoneNumber(private var phoneNumber: String) {

    override fun toString(): String {
        return "PhoneNumber(phoneNumber='$phoneNumber')"
    }
}
