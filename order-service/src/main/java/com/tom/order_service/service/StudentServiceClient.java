package com.tom.order_service.service;


import com.tom.order_service.model.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentServiceClient {

    @GetMapping("/student/{id}")
    StudentDto getStudentById(@PathVariable Long id);


}