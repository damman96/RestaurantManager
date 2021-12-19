package restaurantmanager.employee;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeDaoTestIT {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@BeforeEach
	void setUp() {
		this.employeeDao.deleteAll();
	}
	
	@Test
	void findAll_Should_ReturnEmptyResult_When_NoEntitiesArePresentInDb() {
		// when
		final var result = this.employeeDao.findAll();
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void save_Should_SaveEntity() {
		// given
		final var employeeToAdd = Employee.builder().build();
		
		// when
		final var result = this.employeeDao.save(employeeToAdd);
		
		// then
		assertThat(result).isEqualTo(employeeToAdd);
	}
	
	@Test
	void findById_Should_ReturnEntity_When_EntityIsPresent() {
		// given
		final var employeeToAdd = Employee.builder().build();
		this.employeeDao.save(employeeToAdd);
		
		// when
		final var result = this.employeeDao.findById(employeeToAdd.getId());
		
		// then
		assertThat(result)
				.isPresent()
				.contains(employeeToAdd);
	}
	
	@Test
	void findById_Should_ReturnEmptyResult_When_EntityIsNotPresent() {
		// given
		final var notExistingId = new Random().nextLong();
		// when
		final var result = this.employeeDao.findById(notExistingId);
		
		// then
		assertThat(result)
				.isNotPresent();
	}
	
	@Test
	void deleteAll_Should_ReturnEmptyResult_When_AllEntitiesWasRemoved() {
		// given
		this.employeeDao.save(Employee.builder().build());
		this.employeeDao.save(Employee.builder().build());
		this.employeeDao.save(Employee.builder().build());
		
		// when
		this.employeeDao.deleteAll();
		final var result = this.employeeDao.findAll();
		
		// then
		assertThat(result).isEmpty();
	}
}