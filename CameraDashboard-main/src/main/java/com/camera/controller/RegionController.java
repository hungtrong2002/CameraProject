package com.camera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.camera.common.APIWindyResponse;
import com.camera.entity.RegionEntity;
import com.camera.modelAPI.Webcams;
import com.camera.repo.CameraRepository;
import com.camera.repo.RegionRepository;
import com.camera.repo.UserRepository;
import com.camera.security.jwt.JwtUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@ControllerAdvice
public class RegionController {
	@Autowired
	RegionRepository regionRepository;
	@Autowired
	CameraRepository cameraRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/regions")
	public ResponseEntity<?> CloneRegions() {
		try {
			String url = "https://api.windy.com/api/webcams/v2/list/webcam=1677977438,1609741784,1622174931,1481291050?key=SGOCSQ1w0Rdfz0vWx08Ia6PVQOeydmYQ&show=webcams:url,location,user";
			RestTemplate restTemplate = new RestTemplate();
			APIWindyResponse apiWindyResponses = restTemplate.getForObject(url, APIWindyResponse.class);
			RegionEntity regionEntity;
			for (Webcams webcams : apiWindyResponses.getResult().getWebcams()) {
				regionEntity = new RegionEntity();

				regionEntity.setName(webcams.getUser().getName());
				regionEntity.setActivate(webcams.getLocation().getTimezone());
				regionEntity.setSerial(webcams.getId());
				regionEntity = regionRepository.save(regionEntity);
			}
			return ResponseEntity.ok("clone region database successfully ");
		} catch (Exception e) {
			// Log the error
			e.printStackTrace();
			// Return an error response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while cloning the regions");
		}
	}

	@ApiOperation(value = "Get all regions", notes = "Returns a list of all regions by accessToken")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "access token valid!"),
			@ApiResponse(code = 401, message = "401: UNAUTHORIZED"),
			@ApiResponse(code = 409, message = "409 conflict: User address already exists"),
			@ApiResponse(code = 500, message = "500 Internal Server Error: Error occurred while registering user") })
	@GetMapping("/regions")
	public ResponseEntity<?> getCamera(
			@ApiParam(value = "Authorization Token", required = true, defaultValue = "Bearer {access_token}") 
			@RequestHeader(name = "Authorization") String authorizationHeader) {
		SecurityContextHolder.getContext().getAuthentication();
		return ResponseEntity.ok(regionRepository.findAll());
	}

	@DeleteMapping("/regions/{id}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "access token valid!"),
			@ApiResponse(code = 409, message = "409 conflict"),
			@ApiResponse(code = 500, message = "500 Internal Server Error") })
	public ResponseEntity<?> deleteRegions(@PathVariable("id") Long id) {
		try {
			regionRepository.delete(regionRepository.findById(id));
			return ResponseEntity.status(HttpStatus.OK).body("delete regions success!!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no regions found with id " + id + "or you need delete warning first");
		}
	}
}
