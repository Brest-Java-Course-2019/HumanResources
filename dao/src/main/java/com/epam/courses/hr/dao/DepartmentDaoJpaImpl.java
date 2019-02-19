package com.epam.courses.hr.dao;

import com.epam.courses.hr.model.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DepartmentDaoJpaImpl implements DepartmentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDaoJpaImpl.class);

    private static final String SELECT_ALL = "select departmentId, departmentName, departmentDescription from department";
    private static final String FIND_BY_ID = "select departmentId, departmentName, departmentDescription from department where departmentId = :departmentId";
    private static final String CHECK_COUNT_NAME = "select count(departmentId) from department where lower(departmentName) = lower(:departmentName)";
    private static final String INSERT = "insert into department (departmentName, departmentDescription) values (:departmentName, :departmentDescription)";
    private static final String UPDATE = "update department set departmentName = :departmentName, departmentDescription = :departmentDescription where departmentId = :departmentId";
    private static final String DELETE = "delete from department where departmentId = :departmentId";
    private static final String DEPARTMENT_ID = "departmentId";
    private static final String DEPARTMENT_NAME = "departmentName";
    private static final String DEPARTMENT_DESCRIPTION = "departmentDescription";

    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DepartmentDaoJpaImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Stream<Department> findAll() {
        LOGGER.debug("findAll()");
        List<Department> departmentList = namedParameterJdbcTemplate.query(SELECT_ALL, new DepartmentRowMapper());
        return departmentList.stream();
    }

    @Override
    public Optional<Department> findById(Integer departmentId) {
        LOGGER.debug("findById({})", departmentId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(DEPARTMENT_ID, departmentId);
        Department department = namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameters,
                BeanPropertyRowMapper.newInstance(Department.class));
        return Optional.ofNullable(department);
    }

    @Override
    public Optional<Department> add(Department department) {
        LOGGER.debug("add({})", department);
        return Optional.of(department)
                .filter(this::isNameUnique)
                .map(this::insertDepartment)
                .orElseThrow(() -> new IllegalArgumentException("Department with the same name already exsists in DB."));
    }

    private boolean isNameUnique(Department department) {
        return namedParameterJdbcTemplate.queryForObject(CHECK_COUNT_NAME,
                new MapSqlParameterSource(DEPARTMENT_NAME, department.getDepartmentName()),
                Integer.class) == 0;
    }

    private Optional<Department> insertDepartment(Department department) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(DEPARTMENT_NAME, department.getDepartmentName());
        mapSqlParameterSource.addValue(DEPARTMENT_DESCRIPTION, department.getDepartmentDescription());

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        int result = namedParameterJdbcTemplate.update(INSERT, mapSqlParameterSource, generatedKeyHolder);
        LOGGER.debug("add( result update = {}, keyholder = {})", result, generatedKeyHolder.getKey().intValue());

        department.setDepartmentId(generatedKeyHolder.getKey().intValue());
        return Optional.of(department);
    }

    private class DepartmentRowMapper implements RowMapper<Department> {

        @Override
        public Department mapRow(ResultSet resultSet, int i) throws SQLException {
            Department department = new Department();
            department.setDepartmentId(resultSet.getInt(DEPARTMENT_ID));
            department.setDepartmentName(resultSet.getString(DEPARTMENT_NAME));
            department.setDepartmentDescription(resultSet.getString(DEPARTMENT_DESCRIPTION));
            return department;
        }
    }

    @Override
    public void update(Department department) {
         Optional.of(namedParameterJdbcTemplate.update(UPDATE, new BeanPropertySqlParameterSource(department)))
                .filter(this::successfullyUpdated)
                .orElseThrow(() -> new RuntimeException("Failed to update department in DB"));
    }

    @Override
    public void delete(int departmentId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(DEPARTMENT_ID, departmentId);
        Optional.of(namedParameterJdbcTemplate.update(DELETE, mapSqlParameterSource))
                .filter(this::successfullyUpdated)
                .orElseThrow(() -> new RuntimeException("Failed to delete department from DB"));
    }

    private boolean successfullyUpdated(int numRowsUpdated) {
        return numRowsUpdated > 0;
    }
}
