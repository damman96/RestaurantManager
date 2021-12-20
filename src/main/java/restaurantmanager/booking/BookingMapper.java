package restaurantmanager.booking;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {
	
	BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);
	
	BookingDto map(Booking booking);
	
	Booking mapFromModify(ModifyBookingDto modifyBookingDto);
	
	ModifyBookingDto mapToModify(Booking booking);
	
}
