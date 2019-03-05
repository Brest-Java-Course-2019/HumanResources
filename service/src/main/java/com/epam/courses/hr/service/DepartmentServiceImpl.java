package com.epam.courses.hr.service;

import com.epam.courses.hr.dao.DepartmentDao;
import com.epam.courses.hr.model.Department;
import com.epam.courses.hr.stub.DepartmentStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class DepartmentServiceImpl implements DepartmentService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private DepartmentDao dao;

    public DepartmentServiceImpl(DepartmentDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Department> findAll() {
        LOGGER.debug("Find all departments");
        return dao.findAll().collect(Collectors.toList());
    }

    @Override
    public List<DepartmentStub> findAllStubs() {
        LOGGER.debug("Find all department stubs");
        return dao.findAllStubs().collect(Collectors.toList());
    }

    @Override
    public void add(Department... departments) {
        for (Department department : departments) {
            dao.addDepartment(department);
        }
    }

    @Override
    public Department findById(Integer id) {
        LOGGER.debug("findById({})", id);
        return dao.findById(id)
                .orElseThrow(() -> new RuntimeException("Failed to get department from DB"));
    }

    @Override
    public void update(Department department) {
        LOGGER.debug("update({})", department);
        dao.update(department);
    }

    @Override
    public void delete(int id) {
        LOGGER.debug("delete({})", id);
        dao.delete(id);
    }
}
