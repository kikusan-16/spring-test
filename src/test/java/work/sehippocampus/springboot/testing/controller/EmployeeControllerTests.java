package work.sehippocampus.springboot.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import work.sehippocampus.springboot.testing.model.Employee;
import work.sehippocampus.springboot.testing.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("taro")
                .lastName("yamada")
                .email("taro.yamada@sehippocampus.work")
                .build();
    }

    @DisplayName("従業員保存")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee()
            throws Exception {
        // given - precondition or setup
        // 第一引数をそのまま返すstub
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("全従業員一覧取得")
    @Test
    public void givenListOfEmployees_whenGetAllEmployee_thenReturnAllEmployeeList()
            throws Exception {
        // given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder()
                                    .firstName("taro")
                                    .lastName("yamada")
                                    .email("taro.yamada@sehippocampus.work").build());
        listOfEmployees.add(Employee.builder()
                                    .firstName("jiro")
                                    .lastName("yamada")
                                    .email("jiro.yamada@sehippocampus.work").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @DisplayName("従業員ID検索")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject()
            throws Exception {
        // given - precondition or setup
        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("従業員ID検索時エラー")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturn404()
            throws Exception {
        // given - precondition or setup
        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("従業員更新")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee()
            throws Exception {
        // given - precondition or setup
        Employee updatedEmployee = Employee.builder()
                .firstName("jiro")
                .lastName("yamada")
                .email("jiro.yamada@sehippocampus.work")
                .build();
        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(
                put("/api/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("従業員更新エラー")
    @Test
    public void givenInvalidEmployee_whenUpdateEmployee_thenReturn404()
            throws Exception {
        // given - precondition or setup
        Employee updatedEmployee = Employee.builder()
                .firstName("jiro")
                .lastName("yamada")
                .email("jiro.yamada@sehippocampus.work")
                .build();
        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.empty());
        // given(employeeService.updateEmployee(any(Employee.class)))
        //         .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(
                put("/api/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("従業員削除")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200()
            throws Exception {
        // given - precondition or setup
        willDoNothing().given(employeeService).deleteEmployee(employee.getId());

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
