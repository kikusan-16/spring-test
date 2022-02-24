package work.sehippocampus.springboot.testing.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import work.sehippocampus.springboot.testing.model.Employee;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("taro")
                .lastName("yamada")
                .email("taro.yamada@sehippocampus.work")
                .build();
    }

    @DisplayName("従業員保存")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        // given - precondition or setup

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("全従業員一覧取得")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList(){
        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .firstName("taro")
                .lastName("yamada")
                .email("taro.yamada@sehippocampus.work")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("従業員ID検索")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("従業員Email検索")
    @Test
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("従業員更新")
    @Test
    public void givenEmployeeObject_whenUpdate_thenReturnUpdatedEmployee(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("yamada.taro@sehippocampus.work");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output
        assertThat(updatedEmployee.getId()).isEqualTo(employee.getId());
        assertThat(updatedEmployee.getEmail()).isEqualTo("yamada.taro@sehippocampus.work");
    }

    @DisplayName("従業員削除")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("JPQL検索")
    @Test
    public void givenEmployeeObject_whenFindByJPQL_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());
    }

    @DisplayName("NativeSQL検索")
    @Test
    public void givenEmployeeObject_whenFindByNativeSQL_thenReturnEmployeeObject(){
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());
    }
}
