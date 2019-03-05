package com.epam.courses.hr.rest_app;

import com.epam.courses.hr.model.Department;
import com.epam.courses.hr.service.DepartmentService;
import com.epam.courses.hr.stub.DepartmentStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Home MVC controller.
 */
@RestController
@RequestMapping(value = "/departments")
public class DepartmentRestController implements DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentRestController.class);

    @Autowired
    private DepartmentService departmentService;

    @Override
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Department> findAll() {
        LOGGER.debug("get all departments");
        return departmentService.findAll();
    }

    @Override
    @RequestMapping(value = "/stubs", method = RequestMethod.GET)
    public List<DepartmentStub> findAllStubs() {
        LOGGER.debug("get all departments stubs");
        return departmentService.findAllStubs();
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Department findById(@PathVariable Integer id) {
        LOGGER.debug("find department by id({})", id);
        return departmentService.findById(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestBody Department department) {
        LOGGER.debug("add department({})", department);
        departmentService.add(department);
    }

    @Override
    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody Department department) {
        LOGGER.debug("update department ({})", department);
        departmentService.update(department);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) {
        LOGGER.debug("delete department ({})", id);
        departmentService.delete(id);
    }
}
