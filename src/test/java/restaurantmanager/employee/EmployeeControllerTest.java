package restaurantmanager.employee;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static restaurantmanager.utils.EmployeeFixture.createEmployeeEntityWithNulls;
import static restaurantmanager.utils.EmployeeFixture.createModifyEmployeeDto;
import static restaurantmanager.utils.EmployeeFixture.createModifyEmployeeDtoWithNulls;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomLong;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EmployeeControllerTest {
	
	private static final String HTTP_LOCAL_HOST = "http://localhost:";
	
	private static final String SLASH = "/";
	
	private static final String BOARDS = "employees";
	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int randomServerPort;
	
	@BeforeEach
	void setUp() {
		this.employeeDao.deleteAll();
	}
	
	@Test
	void getAllEmployees_Should_ReturnStatusCode200AndEmptyList_When_DatabaseIsEmpty() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS;
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<EmployeeDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isEmpty();
	}
	
	@Test
	void getAllEmployees_Should_ReturnStatusCode200AndResultList_When_DatabaseIsNotEmpty() {
		//given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS;
		final var employees = List.of(
				createEmployeeEntityWithNulls(),
				createEmployeeEntityWithNulls(),
				createEmployeeEntityWithNulls());
		this.employeeDao.saveAll(employees);
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<EmployeeDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isNotEmpty();
		assertThat(result.getBody()).usingRecursiveComparison()
				.isEqualTo(employees.stream().map(EmployeeMapper.INSTANCE::map).collect(toUnmodifiableList()));
	}
	
	@Test
	void getEmployeeById_Should_ReturnStatusCode200AndResult_When_EntityWithGivenIdExists() {
		// given
		final var saved = this.employeeDao.save(createEmployeeEntityWithNulls());
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				EmployeeDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody())
				.isNotNull()
				.isEqualTo(EmployeeMapper.INSTANCE.map(saved));
	}
	
	@Test
	void getEmployeeById_Should_ReturnStatusCode404_When_EntityWithGivenIdNotExist() {
		// given
		final var id = createRandomLong();
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + id;
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				EmployeeDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void addEmployee_Should_ReturnStatusCode200AndResult_When_SuccessfullyAddedEmployee() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS;
		final var employeeToAdd = createModifyEmployeeDtoWithNulls();
		final var body = new HttpEntity<>(employeeToAdd);
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.POST,
				body,
				EmployeeDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isNotNull().isPositive();
		assertThat(result.getBody().getFirstName()).isEqualTo(employeeToAdd.getFirstName());
		assertThat(result.getBody().getLastName()).isEqualTo(employeeToAdd.getLastName());
		assertThat(result.getBody().getEmail()).isEqualTo(employeeToAdd.getEmail());
		assertThat(result.getBody().getPosition()).isEqualTo(employeeToAdd.getPosition());
		assertThat(result.getBody().getSalary()).isEqualTo(employeeToAdd.getSalary());
		assertThat(result.getBody().getPhoneNumber()).isEqualTo(employeeToAdd.getPhoneNumber());
		assertThat(result.getBody().getStartDate()).isEqualTo(employeeToAdd.getStartDate());
	}
	
	@Test
	void updateEmployee_Should_ReturnStatusCode200_When_SuccessfullyUpdatedEntity() {
		// given
		final var saved = this.employeeDao.save(createEmployeeEntityWithNulls());
		final var modifyEmployeeDto = createModifyEmployeeDto();
		
		final var body = new HttpEntity<>(modifyEmployeeDto);
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + UPDATE + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.PUT,
				body,
				EmployeeDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(saved.getId());
		assertThat(result.getBody().getFirstName()).isEqualTo(modifyEmployeeDto.getFirstName());
		assertThat(result.getBody().getLastName()).isEqualTo(modifyEmployeeDto.getLastName());
		assertThat(result.getBody().getEmail()).isEqualTo(modifyEmployeeDto.getEmail());
		assertThat(result.getBody().getPosition()).isEqualTo(modifyEmployeeDto.getPosition());
		assertThat(result.getBody().getSalary()).isEqualTo(modifyEmployeeDto.getSalary());
		assertThat(result.getBody().getPhoneNumber()).isEqualTo(modifyEmployeeDto.getPhoneNumber());
		assertThat(result.getBody().getStartDate()).isEqualTo(modifyEmployeeDto.getStartDate());
	}
	
	@Test
	void updateEmployee_Should_ReturnStatusCode404_When_EntityWithGivenIdNotExist() {
		// given
		final var id = createRandomLong();
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + UPDATE + SLASH + id;
		final var body = new HttpEntity<>(createModifyEmployeeDtoWithNulls());
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.PUT,
				body,
				EmployeeDto.class);
		
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void deleteEmployeeById_Should_ReturnStatusCode200AndResult_When_EntityWithGivenIdWasDeleted() {
		// given
		final var saved = this.employeeDao.save(createEmployeeEntityWithNulls());
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + DELETE + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.DELETE,
				HttpEntity.EMPTY,
				EmployeeDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody())
				.isNotNull()
				.isEqualTo(EmployeeMapper.INSTANCE.map(saved));
	}
}