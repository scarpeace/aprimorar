package com.aprimorar.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.aprimorar.api.domain.employee.EmployeeRepository;
import com.aprimorar.api.domain.event.EventRepository;
import com.aprimorar.api.domain.parent.ParentRepository;
import com.aprimorar.api.domain.student.StudentRepository;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
class ApiAprimorarApplicationTests {

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private ParentRepository parentRepository;

    @Test
    @DisplayName("application context loads successfully")
    void contextLoads() {
    }

}
