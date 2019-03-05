package com.epam.courses.hr.rest_app;

import com.epam.courses.hr.model.Department;
import com.epam.courses.hr.service.DepartmentService;
import com.epam.courses.hr.stub.DepartmentStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Home MVC controller.
 */
@RestController
public class DepartmentRestController implements DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentRestController.class);

    @Autowired
    private DepartmentService service;

    @GetMapping(value = "/departments")
    public List<Department> findAll() {
        LOGGER.debug("get all departments");
        return service.findAll();
    }

    @Override
    public List<DepartmentStub> findAllStubs() {
        return null;
    }

    @Override
    public void add(Department... departments) {

    }

    @Override
    public Department findById(Integer id) {
        return null;
    }

    @Override
    public void update(Department department) {

    }

    @Override
    public void delete(int id) {

    }
}
