package com.epam.courses.hr.service;

import com.epam.courses.hr.model.Department;
import com.epam.courses.hr.stub.DepartmentStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath:test-service.xml"})
class DepartmentServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentService departmentService;

    @Test
    void findAll() {
        List<Department> departments = departmentService.findAll();
        assertNotNull(departments);
    }

    @Test
    void findAllStubs() {
        List<DepartmentStub> departments = departmentService.findAllStubs();
        assertNotNull(departments);
    }

    @Test
    void add() {

        long count = departmentService.findAll().size();
        LOGGER.debug("Count before: {}", count);

        Department department = create();
        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            departmentService.add(department, department);
        });


        long newCount = departmentService.findAll().size();
        LOGGER.debug("Count after: {}", newCount);
        assert count == newCount;
    }

    private Department create() {
        Department department = new Department();
        department.setDepartmentName("name");
        department.setDepartmentDescription("desc");
        return department;
    }

    @Test
    void findById() {
        // given
        Department firstDepartment = departmentService.findAll().get(0);
        assertNotNull(firstDepartment);
        Integer id = firstDepartment.getDepartmentId();

        // when
        Department department = departmentService.findById(id);
        assertNotNull(department);

        assertEquals(firstDepartment.getDepartmentName(), department.getDepartmentName());
    }
}