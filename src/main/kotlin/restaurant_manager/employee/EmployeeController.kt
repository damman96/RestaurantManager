package restaurant_manager.employee

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employees")
class EmployeeController(private val applicationService: EmployeeApplicationService) {

    @GetMapping
    fun getEmployees(): ResponseEntity<List<EmployeeDetailDto>> {
        return ResponseEntity.ok(applicationService.getEmployees())
    }

    @GetMapping("/{employeeId}")
    fun getEmployeeById(@PathVariable employeeId: Long): ResponseEntity<EmployeeDetailDto> {
        return ResponseEntity.ok(applicationService.getEmployeeById(employeeId))
    }

    @PostMapping
    fun addEmployee(@RequestBody employeeAddDto: EmployeeAddDto): ResponseEntity<String> {
        return ResponseEntity.ok(applicationService.addEmployee(employeeAddDto))
    }

    @PutMapping("/edit/{id}")
    fun editEmployee(@RequestBody employeeEditDto: EmployeeEditDto, @PathVariable id: Long): ResponseEntity<String> {
        return ResponseEntity.ok(applicationService.editEmployee(employeeEditDto, id))
    }
}
