package com.test.aop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.aop.exception.ResourceNotFoundException;
import com.test.aop.model.Funcionario;
import com.test.aop.repository.FuncionarioRepository;

/**
 * Employee Service
 * 
 * @author Ramesh
 *
 */
@Service
public class FuncionarioService {
	@Autowired
	private FuncionarioRepository employeeRepository;

	public List<Funcionario> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Optional<Funcionario> getEmployeeById(Long employeeId) throws ResourceNotFoundException {
		return employeeRepository.findById(employeeId);
	}

	public Funcionario createEmployee(Funcionario employee) {
		return employeeRepository.save(employee);
	}

	public Funcionario updateEmployee(Long employeeId,
        Funcionario employeeDetails) throws ResourceNotFoundException {
        Funcionario employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Funcionario updatedEmployee = employeeRepository.save(employee);
        return updatedEmployee;
    }

	public Map <String, Boolean> deleteEmployee(Long employeeId)
    throws ResourceNotFoundException {
        Funcionario employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map <String, Boolean> response = new HashMap < > ();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}