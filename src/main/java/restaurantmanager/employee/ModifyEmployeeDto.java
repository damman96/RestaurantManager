package restaurantmanager.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class ModifyEmployeeDto {
	
	private String firstName;
	private String lastName;
	private String email;
	private String position;
	private BigDecimal salary;
	private String phoneNumber;
	private LocalDate startDate;
}
