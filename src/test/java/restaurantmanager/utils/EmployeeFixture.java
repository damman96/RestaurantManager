package restaurantmanager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomBigDecimal;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomString;

import java.time.LocalDate;

import restaurantmanager.employee.Employee;
import restaurantmanager.employee.EmployeeDto;
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
				.salary(createRandomBigDecimal())
				.phoneNumber(createRandomString())
				.startDate(LocalDate.now())
				.build();
	}
	
	public static Employee createEmployeeEntityFromModifyDto(final Long id, final ModifyEmployeeDto modifyEmployeeDto) {
		return Employee.builder()
				.id(id)
				.firstName(modifyEmployeeDto.getFirstName())
				.lastName(modifyEmployeeDto.getLastName())
				.email(modifyEmployeeDto.getEmail())
				.position(modifyEmployeeDto.getPosition())
				.salary(modifyEmployeeDto.getSalary())
				.phoneNumber(modifyEmployeeDto.getPhoneNumber())
				.startDate(modifyEmployeeDto.getStartDate())
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
				.salary(createRandomBigDecimal())
				.phoneNumber(createRandomString())
				.startDate(LocalDate.now())
				.build();
	}
	
	public static void assertEmployee(final EmployeeDto result, final ModifyEmployeeDto modifyEmployeeDto) {
		assertThat(result.getFirstName()).isEqualTo(modifyEmployeeDto.getFirstName());
		assertThat(result.getLastName()).isEqualTo(modifyEmployeeDto.getLastName());
		assertThat(result.getEmail()).isEqualTo(modifyEmployeeDto.getEmail());
		assertThat(result.getPosition()).isEqualTo(modifyEmployeeDto.getPosition());
		assertThat(result.getSalary()).isEqualTo(modifyEmployeeDto.getSalary());
		assertThat(result.getPhoneNumber()).isEqualTo(modifyEmployeeDto.getPhoneNumber());
		assertThat(result.getStartDate()).isEqualTo(modifyEmployeeDto.getStartDate());
	}
}
