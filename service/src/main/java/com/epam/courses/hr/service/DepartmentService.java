package com.epam.courses.hr.service;

import com.epam.courses.hr.model.Department;
import com.epam.courses.hr.stub.DepartmentStub;

import java.util.stream.Stream;

public interface DepartmentService {

    Stream<Department> findAll();

    Stream<DepartmentStub> findAllStubs();

    void add(Department... departments);

}
