package work.sehippocampus.springboot.testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import work.sehippocampus.springboot.testing.model.Employee;

import java.lang.annotation.Native;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // query method
    Optional<Employee> findByEmail(String email);

    // JPQL custom query method
    @Query("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
    Employee findByJPQL(@Param("firstName")String firstName, @Param("lastName") String lastName);

    // native query method
    @Query( value = "select * from employees e where e.first_name = :firstName and e.last_name = :lastName",
            nativeQuery = true)
    Employee findByNativeSQL(@Param("firstName")String firstName, @Param("lastName")String lastName);
}
