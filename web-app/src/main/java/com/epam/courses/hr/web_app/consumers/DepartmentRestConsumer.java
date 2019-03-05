package com.epam.courses.hr.web_app.consumers;

import com.epam.courses.hr.model.Department;
import com.epam.courses.hr.service.DepartmentService;
import com.epam.courses.hr.stub.DepartmentStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class DepartmentRestConsumer implements DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentRestConsumer.class);

    private String url;

    private RestTemplate restTemplate;

    public DepartmentRestConsumer(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Department> findAll() {
        LOGGER.debug("findAllStubs()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url + "/all", List.class);
        return (List<Department>) responseEntity.getBody();
    }

    @Override
    public List<DepartmentStub> findAllStubs() {
        LOGGER.debug("findAllStubs()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url + "/stubs", List.class);
        return  (List<DepartmentStub>) responseEntity.getBody();
    }

    @Override
    public Department findById(Integer id) {
        LOGGER.debug("findById({})", id);
        ResponseEntity<Department> responseEntity = restTemplate.getForEntity(url + "/" + id, Department.class);
        return responseEntity.getBody();
    }

    @Override
    public void add(Department department) {
        LOGGER.debug("add({})", department);
        restTemplate.postForEntity(url, department, Department.class);
    }

    @Override
    public void update(Department department) {
        LOGGER.debug("update({})", department);
        restTemplate.put(url, department);
    }

    @Override
    public void delete(int id) {
        LOGGER.debug("update({})", id);
        restTemplate.delete(url + "/" + id);
    }
}
