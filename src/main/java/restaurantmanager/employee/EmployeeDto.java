package restaurantmanager.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
@EqualsAndHashCode
public class EmployeeDto {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String position;
	private BigDecimal salary;
	private String phoneNumber;
	private LocalDate startDate;
}
