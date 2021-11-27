package restaurantmanager.booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDao extends JpaRepository<Booking, Long> {
}
