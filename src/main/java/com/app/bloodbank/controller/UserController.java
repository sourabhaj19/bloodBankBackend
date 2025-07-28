package com.app.bloodbank.controller;

import com.app.bloodbank.criteria.UserCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.exception.CustomException;
import com.app.bloodbank.model.Users;
import com.app.bloodbank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Get filtered users with pagination")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    @GetMapping
    public ResponseEntity<PagedResponse<Users>> getUsers(
            @Parameter(description = "Filter criteria") @Valid UserCriteria criteria,
            @Parameter(description = "Pagination parameters") @PageableDefault(size = 20) Pageable pageable) {
        log.debug("REST request to get Users by criteria: {}", criteria);
        return ResponseEntity.ok(userService.getUsers(criteria, pageable));
    }

    @PutMapping
    public Users updateUsers(@RequestBody Users user) {
        return userService.updateUsers(user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletUser(@PathVariable("id") Long id) {
        userService.deletUser(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User Deleted Successfully!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody Users user, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.toList());

            throw new CustomException(errors.toString(), HttpStatus.BAD_REQUEST);
        }
        return userService.postUser(user);
    }




    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        return userService.login(email, password);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/available-blood")
    public List<String> getAvailableBlood(){
        return userService.getAvailableBlood();
    }
}