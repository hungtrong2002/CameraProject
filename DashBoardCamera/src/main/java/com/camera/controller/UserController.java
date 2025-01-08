package com.camera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.camera.dto.TokenDTO;
import com.camera.dto.UserDTO;
import com.camera.entity.UserEntity;
import com.camera.map.UserMapper;
import com.camera.repo.UserRepository;
import com.camera.security.jwt.JwtUtils;
import com.camera.security.service.UserDetailsImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class UserController {
	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private UserMapper userMapper;

	@ApiOperation(value = "Get all users", notes = "Returns a list of all users by accessToken")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "access token valid!"),
			@ApiResponse(code = 401, message = "401: UNAUTHORIZED"),
			@ApiResponse(code = 409, message = "409 conflict: User address already exists"),
			@ApiResponse(code = 500, message = "500 Internal Server Error: Error occurred while registering user") })
	@PostMapping("/user-profile")
	public ResponseEntity<?> getUserByUserName(
			@ApiParam(value = "Authorization Token", required = true, defaultValue = "Bearer {access_token}") 
			@RequestHeader(name = "Authorization") String authorizationHeader) {
		try {
			UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			UserDTO userDTO = userMapper.toDto(userDetails);
			return ResponseEntity.ok(userDTO);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("401: UNAUTHORIZED");
		}
	}

}
