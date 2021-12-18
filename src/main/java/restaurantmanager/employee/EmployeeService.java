package restaurantmanager.employee;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import restaurantmanager.NotFoundException;

@Slf4j
@Service
public class EmployeeService {
	
	private final EmployeeDao employeeDao;
	
	public EmployeeService(final EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}
	
	List<EmployeeDto> getAllEmployees() {
		final var employees = this.employeeDao.findAll()
				.stream()
				.map(EmployeeMapper.INSTANCE::map)
				.collect(toUnmodifiableList());
		log.info("Received employees={}", employees);
		return employees;
	}
	
	EmployeeDto getEmployeeById(final Long id) {
		final var receivedEmployee = EmployeeMapper.INSTANCE.map(this.getEntityFromDb(id));
		log.info("Received employee={}", receivedEmployee);
		return receivedEmployee;
	}
	
	EmployeeDto addEmployee(final ModifyEmployeeDto modifyEmployeeDto) {
		final var savedEmployee = this.employeeDao.save(EmployeeMapper.INSTANCE.mapToModify(modifyEmployeeDto));
		log.info("Saved employee={}", savedEmployee);
		return EmployeeMapper.INSTANCE.map(savedEmployee);
	}
	
	EmployeeDto updateEmployee(final Long id, final ModifyEmployeeDto modifyEmployeeDto) {
		final var employeeFromDb = this.getEntityFromDb(id);
		final var modifiedEmployee = Employee.builder()
				.id(employeeFromDb.getId())
				.firstName(modifyEmployeeDto.getFirstName())
				.lastName(modifyEmployeeDto.getLastName())
				.email(modifyEmployeeDto.getEmail())
				.position(modifyEmployeeDto.getPosition())
				.salary(modifyEmployeeDto.getSalary())
				.phoneNumber(modifyEmployeeDto.getPhoneNumber())
				.startDate(modifyEmployeeDto.getStartDate())
				.build();
		return EmployeeMapper.INSTANCE.map(this.employeeDao.save(modifiedEmployee));
	}
	
	EmployeeDto deleteEmployeeById(final Long id) {
		final var removedEmployee = this.getEmployeeById(id);
		this.employeeDao.deleteById(removedEmployee.getId());
		log.info("Removed employee={}", removedEmployee);
		return removedEmployee;
	}
	
	private Employee getEntityFromDb(final Long id) {
		return this.employeeDao.findById(id)
				.orElseThrow(() -> new NotFoundException("Employee with id=" + id + " not found"));
	}
}
