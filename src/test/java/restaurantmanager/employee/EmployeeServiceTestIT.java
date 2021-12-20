package restaurantmanager.employee;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static restaurantmanager.utils.EmployeeFixture.createEmployeeEntity;
import static restaurantmanager.utils.EmployeeFixture.createEmployeeEntityWithNulls;
import static restaurantmanager.utils.EmployeeFixture.createModifyEmployeeDto;
import static restaurantmanager.utils.EmployeeFixture.createModifyEmployeeDtoWithNulls;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import restaurantmanager.NotFoundException;
import restaurantmanager.utils.EmployeeFixture;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EmployeeServiceTestIT {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmployeeService employeeService;
	
	@BeforeEach
	void setUp() {
		this.employeeDao.deleteAll();
	}
	
	@Test
	void getAllEmployees_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// when
		final var result = this.employeeService.getAllEmployees();
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void getAllEmployees_Should_ReturnResultList_When_EntitiesArePresentInDb() {
		// given
		final var employeesToAdd = List.of(createEmployeeEntity(1L),
										   createEmployeeEntity(2L),
										   createEmployeeEntity(3L));
		this.employeeDao.saveAll(employeesToAdd);
		
		// when
		final var result = this.employeeService.getAllEmployees();
		
		// then
		assertThat(result).isNotEmpty();
		assertThat(result).extracting(EmployeeDto::getId)
				.containsAll(employeesToAdd.stream().map(Employee::getId).collect(toUnmodifiableList()));
		assertThat(result).extracting(EmployeeDto::getFirstName)
				.containsAll(employeesToAdd.stream().map(Employee::getFirstName).collect(toUnmodifiableList()));
		assertThat(result).extracting(EmployeeDto::getLastName)
				.containsAll(employeesToAdd.stream().map(Employee::getLastName).collect(toUnmodifiableList()));
		assertThat(result).extracting(EmployeeDto::getEmail)
				.containsAll(employeesToAdd.stream().map(Employee::getEmail).collect(toUnmodifiableList()));
		assertThat(result).extracting(EmployeeDto::getPosition)
				.containsAll(employeesToAdd.stream().map(Employee::getPosition).collect(toUnmodifiableList()));
		assertThat(result).extracting(EmployeeDto::getSalary)
				.containsAll(employeesToAdd.stream().map(Employee::getSalary).collect(toUnmodifiableList()));
		assertThat(result).extracting(EmployeeDto::getPhoneNumber)
				.containsAll(employeesToAdd.stream().map(Employee::getPhoneNumber).collect(toUnmodifiableList()));
		assertThat(result).extracting(EmployeeDto::getStartDate)
				.containsAll(employeesToAdd.stream().map(Employee::getStartDate).collect(toUnmodifiableList()));
	}
	
	@Test
	void getEmployeeById_Should_ReturnResult_When_EntityExists() {
		// given
		final var employee = createEmployeeEntity(1L);
		this.employeeDao.save(employee);
		
		// when
		final var result = this.employeeService.getEmployeeById(employee.getId());
		
		// then
		assertThat(result).isEqualTo(EmployeeMapper.INSTANCE.map(employee));
	}
	
	@Test
	void getEmployeeById_Should_ThrowNotFoundException_When_EntityWithGivenIdNotExist() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.employeeService.getEmployeeById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Employee with id=" + id + " not found");
	}
	
	@Test
	void addEmployee_Should_SaveEntity() {
		// given
		final var employeeToAdd = createModifyEmployeeDto();
		
		// when
		final var result = this.employeeService.addEmployee(employeeToAdd);
		
		// then
		assertThat(result.getId()).isNotNull().isPositive();
		EmployeeFixture.assertEmployee(result, employeeToAdd);
	}
	
	@Test
	void updateEmployee_Should_UpdateEmployee() {
		// given
		final var existingEmployee = createEmployeeEntity(1L);
		this.employeeDao.save(existingEmployee);
		
		final var employeeToModify = createModifyEmployeeDto();
		
		// when
		final var result = this.employeeService.updateEmployee(existingEmployee.getId(), employeeToModify);
		
		// then
		assertThat(result.getId()).isEqualTo(existingEmployee.getId());
		EmployeeFixture.assertEmployee(result, employeeToModify);
	}
	
	@Test
	void updateEmployee_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.employeeService.updateEmployee(id, createModifyEmployeeDtoWithNulls()));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Employee with id=" + id + " not found");
	}
	
	@Test
	void deleteEmployeeById_Should_RemoveEntity_When_EntityExists() {
		// given
		final var employee = createEmployeeEntityWithNulls();
		this.employeeDao.save(employee);
		
		// when
		this.employeeService.deleteEmployeeById(employee.getId());
		
		//then
		assertThat(this.employeeDao.findById(employee.getId())).isNotPresent();
	}
	
	@Test
	void deleteEmployeeById_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.employeeService.deleteEmployeeById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Employee with id=" + id + " not found");
	}
}