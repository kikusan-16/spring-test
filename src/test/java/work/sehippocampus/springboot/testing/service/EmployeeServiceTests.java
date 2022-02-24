package work.sehippocampus.springboot.testing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import work.sehippocampus.springboot.testing.exception.ResourceNotFoundException;
import work.sehippocampus.springboot.testing.model.Employee;
import work.sehippocampus.springboot.testing.repository.EmployeeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstName("taro")
                .lastName("yamada")
                .email("taro.yamada@sehippocampus.work")
                .build();
        // @Mock @InjectMocksによって不要
        // employeeRepository = mock(EmployeeRepository.class);
        // employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @DisplayName("従業員保存")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnEmployeeObject(){
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("従業員保存時Email重複エラー")
    @Test
    public void givenExistingEmail_whenSave_thenThrowsException(){
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("全従業員一覧取得")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList(){
        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("taro")
                .lastName("yamada")
                .email("taro.yamada@sehippocampus.work")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        // when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("全従業員一覧取得(空)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){
        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("taro")
                .lastName("yamada")
                .email("taro.yamada@sehippocampus.work")
                .build();
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("従業員ID検索")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        // given - precondition or setup
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("従業員更新")
    @Test
    public void givenEmployeeObject_whenUpdate_thenReturnEmployeeObject(){
        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("yamada.taro@sehippocampus.work");

        // when - action or the behaviour that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("yamada.taro@sehippocampus.work");
    }

    @DisplayName("従業員削除")
    @Test
    public void givenEmployeeId_whenDelete_thenDeleteEmployee(){
        // given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(employee.getId());

        // when - action or the behaviour that we are going test
        employeeService.deleteEmployee(employee.getId());

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }
}
