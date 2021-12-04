package restaurantmanager.utils;

import static restaurantmanager.utils.RandomUtilsFixture.createRandomLong;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomString;

import java.math.BigDecimal;
import java.time.LocalDate;

import restaurantmanager.employee.Employee;
import restaurantmanager.employee.ModifyEmployeeDto;

public abstract class EmployeeFixture {
	
	private static final int SCALE = 2;
	
	public static Employee createEmployeeEntityWithNulls() {
		return Employee.builder().build();
	}
	
	public static Employee createEmployeeEntity(final Long id) {
		return Employee.builder()
				.id(id)
				.firstName(createRandomString())
				.lastName(createRandomString())
				.email(createRandomString())
				.position(createRandomString())
				.salary(BigDecimal.valueOf(createRandomLong(), SCALE))
				.phoneNumber(createRandomString())
				.startDate(LocalDate.now())
				.build();
	}
	
	public static ModifyEmployeeDto createModifyEmployeeDtoWithNulls() {
		return ModifyEmployeeDto.builder().build();
	}
	
	public static ModifyEmployeeDto createModifyEmployeeDto() {
		return ModifyEmployeeDto.builder()
				.firstName(createRandomString())
				.lastName(createRandomString())
				.email(createRandomString())
				.position(createRandomString())
				.salary(BigDecimal.valueOf(createRandomLong(), SCALE))
				.phoneNumber(createRandomString())
				.startDate(LocalDate.now())
				.build();
	}
}
