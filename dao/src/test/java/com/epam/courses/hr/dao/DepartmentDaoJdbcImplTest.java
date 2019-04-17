package com.epam.courses.hr.dao;

import com.epam.courses.hr.model.Department;
import com.epam.courses.hr.stub.DepartmentStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath:test-dao.xml"})
@Transactional
@Rollback
class DepartmentDaoJdbcImplTest {

    private static final String DEVELOPMENT_DEPARTMENT = "Development Department";
    private static final String DEV = "DEV";
    private static final String NEW_DEPARTMENT_NAME = "New Department";
    private static final String NEW_DEPARTMENT_DESCRIPTION = "New Department Description";

    private static final int FIRST_DEPARTMENT_ID = 1;
    private static final int FULL_DEPARTMENT_LIST = 4;


    @Autowired
    private DepartmentDao departmentDao;

    @Test
    void findAll() {
        Stream<Department> departments = departmentDao.findAll();
        assertNotNull(departments);
    }

    @Test
    void findAllStubs() {
        Stream<DepartmentStub> dtos = departmentDao.findAllStubs();
        assertNotNull(dtos);
        assertTrue(dtos.count() > 0);
    }

    @Test
    void findAllCheckCount() {
        Stream<Department> departments = departmentDao.findAll();
        assertNotNull(departments);
        assertEquals(FULL_DEPARTMENT_LIST, departments.count());
    }

    @Test
    void findById() {
        Optional<Department> departmentOptional = departmentDao.findById(1);
        assertTrue(departmentOptional.isPresent());
        departmentOptional.ifPresent(department -> {
            assertEquals(FIRST_DEPARTMENT_ID, department.getDepartmentId().intValue());
            assertEquals(DEV, department.getDepartmentName());
            assertEquals(DEVELOPMENT_DEPARTMENT, department.getDepartmentDescription());
        });
    }

    @Test
    void findByIdEmpty() {
        Optional<Department> departmentOptional = departmentDao.findById(99999);
        assertFalse(departmentOptional.isPresent());
    }

    @Test
    void create() {
        Stream<Department> departmentsBeforeInsert = departmentDao.findAll();

        Department department = new Department();
        department.setDepartmentName(NEW_DEPARTMENT_NAME);
        department.setDepartmentDescription(NEW_DEPARTMENT_DESCRIPTION);
        Department newDepartment = departmentDao.add(department).get();
        assertNotNull(newDepartment.getDepartmentId());

        Stream<Department> departmentsAfterInsert = departmentDao.findAll();
        assertEquals(1, departmentsAfterInsert.count() - departmentsBeforeInsert.count());
    }

    @Test
    void createDuplicateDepartment() {
        Department department2 = new Department();
        department2.setDepartmentName(NEW_DEPARTMENT_NAME);
        department2.setDepartmentDescription(NEW_DEPARTMENT_DESCRIPTION);
        Department newDepartment = departmentDao.add(department2).get();
        assertNotNull(newDepartment.getDepartmentId());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            departmentDao.add(department2);
        });
    }

    @Test
    void update() {
        Department department = new Department();
        department.setDepartmentName(NEW_DEPARTMENT_NAME);
        department.setDepartmentDescription(NEW_DEPARTMENT_NAME);
        Department newDepartment = departmentDao.add(department).get();
        assertNotNull(newDepartment.getDepartmentId());

        department.setDepartmentName(NEW_DEPARTMENT_NAME + "_2");
        department.setDepartmentDescription(NEW_DEPARTMENT_NAME + "_2");
        departmentDao.update(department);

        Department updatedDepartment = departmentDao.findById(department.getDepartmentId()).get();

        assertEquals(NEW_DEPARTMENT_NAME + "_2", updatedDepartment.getDepartmentName());
        assertEquals(NEW_DEPARTMENT_NAME + "_2", updatedDepartment.getDepartmentDescription());
    }


    @Test
    void delete() {
        Stream<Department> departments = departmentDao.findAll();
        Department department = departments.findFirst().get();
        departmentDao.delete(department.getDepartmentId());

        assertFalse(departmentDao.findById(department.getDepartmentId()).isPresent());
    }

}