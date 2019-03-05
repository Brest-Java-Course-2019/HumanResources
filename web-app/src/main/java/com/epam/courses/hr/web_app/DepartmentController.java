package com.epam.courses.hr.web_app;

import com.epam.courses.hr.model.Department;
import com.epam.courses.hr.service.DepartmentService;
import com.epam.courses.hr.web_app.validators.DepartmentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Department controller.
 */
@Controller
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    DepartmentValidator departmentValidator;

    /**
     * Goto departments list page.
     *
     * @return view name
     */
    @GetMapping(value = "/departments")
    public final String departments(Model model) {

        LOGGER.debug("findAll({})", model);
        model.addAttribute("departmentStubs", departmentService.findAllStubs();
        return "departments";
    }

    /**
     * Goto edit department page.
     *
     * @return view name
     */
    @GetMapping(value = "/department/{id}")
    public final String gotoEditDepartmentPage(@PathVariable Integer id, Model model) {

        LOGGER.debug("gotoEditDepartmentPage({},{})", id, model);
        Department department = departmentService.findById(id);
        model.addAttribute("department", department);
        return "department";
    }

    /**
     * Update department into persistence storage.
     *
     * @return view name
     */
    @PostMapping(value = "/department/{id}")
    public String updateDepartment(@Valid Department department,
                                   BindingResult result) {

        LOGGER.debug("updateDepartment({}, {})", department, result);
        departmentValidator.validate(department, result);
        if (result.hasErrors()) {
            return "department";
        } else {
            this.departmentService.update(department);
            return "redirect:/departments";
        }
    }

    /**
     * Goto add department page.
     *
     * @return view name
     */
    @GetMapping(value = "/department")
    public final String gotoAddDepartmentPage(Model model) {

        LOGGER.debug("create({})", model);
        Department department = new Department();
        model.addAttribute("isNew", true);
        model.addAttribute("department", department);
        return "department";
    }

    /**
     * Persist new department into persistence storage.
     *
     * @param department new department with filled data.
     * @param result     binding result.
     * @return view name
     */
    @PostMapping(value = "/department")
    public String addDepartment(@Valid Department department,
                                BindingResult result) {

        LOGGER.debug("addDepartment({}, {})", department, result);
        departmentValidator.validate(department, result);
        if (result.hasErrors()) {
            return "department";
        } else {
            this.departmentService.add(department);
            return "redirect:/departments";
        }
    }

    /**
     * Delete department.
     *
     * @return view name
     */
    @GetMapping(value = "/department/{id}/delete")
    public final String deleteDepartmentById(@PathVariable Integer id, Model model) {
        LOGGER.debug("delete({},{})", id, model);
        departmentService.delete(id);
        return "redirect:/departments";
    }
}
