package restaurant_manager.infrastructure

import org.springframework.data.repository.CrudRepository

interface EmployeeDAO : CrudRepository<Employee, Long>
