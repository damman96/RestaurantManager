package restaurantmanager.employee;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	private final EmployeeService employeeService;
	
	public EmployeeController(final EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@GetMapping
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
		return ResponseEntity.ok(this.employeeService.getAllEmployees());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.employeeService.getEmployeeById(id));
	}
	
	@PostMapping
	public ResponseEntity<EmployeeDto> addEmployee(@RequestBody final ModifyEmployeeDto modifyEmployeeDto) {
		return ResponseEntity.ok(this.employeeService.addEmployee(modifyEmployeeDto));
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable final Long id,
													  @RequestBody final ModifyEmployeeDto modifyEmployeeDto) {
		return ResponseEntity.ok(this.employeeService.updateEmployee(id, modifyEmployeeDto));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<EmployeeDto> deleteEmployeeById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.employeeService.deleteEmployeeById(id));
		
	}
}
