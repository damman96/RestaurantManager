package restaurantmanager.employee;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static restaurantmanager.utils.EmployeeFixture.assertEmployee;
import static restaurantmanager.utils.EmployeeFixture.createEmployeeEntity;
import static restaurantmanager.utils.EmployeeFixture.createEmployeeEntityFromModifyDto;
import static restaurantmanager.utils.EmployeeFixture.createModifyEmployeeDto;
import static restaurantmanager.utils.EmployeeFixture.createModifyEmployeeDtoWithNulls;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import restaurantmanager.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	
	private final EmployeeDao employeeDao = Mockito.mock(EmployeeDao.class);
	
	@InjectMocks
	private EmployeeService employeeService;
	
	@BeforeEach
	void setUp() {
		this.employeeDao.deleteAll();
	}
	
	@Test
	void getAllEmployees_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// given
		when(this.employeeDao.findAll()).thenReturn(emptyList());
		
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
		
		when(this.employeeDao.findAll()).thenReturn(employeesToAdd);
		
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
		final var id = 1L;
		final var employee = createEmployeeEntity(id);
		when(this.employeeDao.findById(id)).thenReturn(Optional.of(employee));
		
		// when
		final var result = this.employeeService.getEmployeeById(id);
		
		// then
		assertThat(result).isEqualTo(EmployeeMapper.INSTANCE.map(employee));
	}
	
	@Test
	void getEmployeeById_Should_ThrowNotFoundException_When_EntityWithGivenIdNotExist() {
		// given
		final var id = new Random().nextLong();
		when(this.employeeDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.employeeService.getEmployeeById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Employee with id=" + id + " not found");
	}
	
	@Test
	void addEmployee_Should_SaveEntity() {
		// given
		final var id = 1L;
		final var employeeToAdd = createModifyEmployeeDto();
		
		// when
		final var entity = createEmployeeEntityFromModifyDto(id, employeeToAdd);
		
		when(this.employeeDao.save(EmployeeMapper.INSTANCE.mapFromModify(employeeToAdd))).thenReturn(entity);
		final var result = this.employeeService.addEmployee(employeeToAdd);
		
		// then
		assertThat(result.getId()).isNotNull().isPositive();
		assertEmployee(result, employeeToAdd);
	}
	
	@Test
	void updateEmployee_Should_UpdateEmployee() {
		// given
		final var id = 1L;
		final var existingEmployee = createEmployeeEntity(id);
		when(this.employeeDao.findById(id)).thenReturn(Optional.of(existingEmployee));
		
		final var employeeToUpdate = createModifyEmployeeDto();
		
		// when
		final var entity = createEmployeeEntityFromModifyDto(id, employeeToUpdate);
		
		when(this.employeeDao.save(entity)).thenReturn(entity);
		final var result = this.employeeService.updateEmployee(id, employeeToUpdate);
		
		// then
		assertThat(result.getId()).isEqualTo(id);
		assertEmployee(result, employeeToUpdate);
	}
	
	@Test
	void updateEmployee_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		when(this.employeeDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.employeeService.updateEmployee(id, createModifyEmployeeDtoWithNulls()));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Employee with id=" + id + " not found");
	}
	
	@Test
	void deleteEmployeeById_Should_RemoveEntity_When_EntityExists() {
		// given
		final var id = 1L;
		final var employee = createEmployeeEntity(id);
		when(this.employeeDao.findById(id)).thenReturn(Optional.of(employee));
		
		// when
		this.employeeService.deleteEmployeeById(id);
		
		//then
		verify(this.employeeDao, times(1)).deleteById(id);
	}
	
	@Test
	void deleteEmployeeById_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		when(this.employeeDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.employeeService.deleteEmployeeById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Employee with id=" + id + " not found");
	}
}
