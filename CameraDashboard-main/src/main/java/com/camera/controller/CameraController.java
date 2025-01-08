package com.camera.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.camera.common.APIWindyResponse;
import com.camera.dto.ThongKeDTO;
import com.camera.entity.CameraEntity;
import com.camera.modelAPI.Webcams;
import com.camera.repo.CameraRepository;
import com.camera.repo.RegionRepository;
import com.camera.repo.WarningRepository;
import com.camera.security.jwt.AuthTokenFilter;
import com.camera.security.jwt.JwtUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CameraController {
	@Autowired
	private CameraRepository cameraRepository;
	@Autowired
	private RegionRepository regionRepository;
	@Autowired
	private WarningRepository warningRepository;
	@Autowired
	AuthTokenFilter authTokenFilter;
	@Autowired
	JwtUtils jwtUtils;

	// save camera to dbs
	@ApiOperation(value = "Clone cameras API", notes = "clone 4 cameras from https://api.windy.com/api/webcams to database")
	@PostMapping("/cameras")
	public void pushCamera() {
		String url = "https://api.windy.com/api/webcams/v2/list/webcam=1677977438,1609741784,1622174931,1481291050?key=SGOCSQ1w0Rdfz0vWx08Ia6PVQOeydmYQ&show=webcams:url,location,user";
		RestTemplate restTemplate = new RestTemplate();
		APIWindyResponse apiWindyResponses = restTemplate.getForObject(url, APIWindyResponse.class);
		CameraEntity cameraEntity;
		for (Webcams webcams : apiWindyResponses.getResult().getWebcams()) {
			cameraEntity = new CameraEntity();
			cameraEntity.setSerial(webcams.getId());
			cameraEntity.setStatus(webcams.getStatus());
			cameraEntity.setRegion(regionRepository.findBySerial(webcams.getId()));
			cameraEntity.setHome_id(webcams.getLocation().getRegion_code());
			String tempUrl = "https://webcams.windy.com/webcams/stream/" + webcams.getId();
			cameraEntity.setUrl(tempUrl);
			cameraEntity = cameraRepository.save(cameraEntity);
		}
	}

	// Respone API for frontend
	@GetMapping("/cameras")
	@ApiOperation(value = "Get cameras API", notes = "Get 4 cameras from database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "access token valid!"),
			@ApiResponse(code = 401, message = "401: UNAUTHORIZED"), @ApiResponse(code = 409, message = "409 conflict"),
			@ApiResponse(code = 500, message = "500 Internal Server Error") })
	public ResponseEntity<?> getCamera(
			@ApiParam(value = "Authorization Token", required = true, defaultValue = "Bearer {access_token}") 
			@RequestHeader(name = "Authorization") String authorizationHeader) {
		return ResponseEntity.ok(cameraRepository.findAll());
	}

	@ApiOperation(value = "Delete cameras API by id", notes = "delete all cameras in database")
	@DeleteMapping("/cameras/{id}")
	public ResponseEntity<?> deleteCameras(@PathVariable("id") Long id) {
		try {
			cameraRepository.delete(cameraRepository.findById(id));
			return ResponseEntity.status(HttpStatus.OK).body("delete cameras success!!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("not found id cameras or delete warning first" + id);
		}
	}

	@GetMapping("/warning")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "access token valid!"),
			@ApiResponse(code = 409, message = "409 conflict"),
			@ApiResponse(code = 500, message = "500 Internal Server Error") })
	public ResponseEntity<?> getWarning(
			@ApiParam(value = "Authorization Token", required = true, defaultValue = "Bearer {access_token}") 
			@RequestHeader(name = "Authorization") String authorizationHeader) {

		return ResponseEntity.ok(warningRepository.findAll());
	}

	@DeleteMapping("/warning/{id}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "access token valid!"),
			@ApiResponse(code = 409, message = "409 conflict"),
			@ApiResponse(code = 500, message = "500 Internal Server Error") })
	public ResponseEntity<?> deleteWarning(@PathVariable("id") Long id) {
		try {
			warningRepository.delete(warningRepository.findById(id));
			return ResponseEntity.status(HttpStatus.OK).body("delete warning success!!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("delete warning unsuccess!!");
		}
	}

	@GetMapping("/warning/{id}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "access token valid!"),
			@ApiResponse(code = 409, message = "409 conflict"),
			@ApiResponse(code = 500, message = "500 Internal Server Error") })
	public ResponseEntity<?> getWarning(
			@PathVariable("id") Long id,
			@ApiParam(value = "Authorization Token", required = true, defaultValue = "Bearer {access_token}") 
			@RequestHeader(name = "Authorization") String authorizationHeader) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(warningRepository.findById(id));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no found warning!!");
		}
	}

	@ApiOperation(value = "Get thong-ke API", notes = "Get all profile from database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "access token valid!"),
			@ApiResponse(code = 401, message = "401: UNAUTHORIZED"),
			@ApiResponse(code = 409, message = "409 conflict: User address already exists"),
			@ApiResponse(code = 500, message = "500 Internal Server Error: Error occurred while registering user") })
	@GetMapping("/thong-ke")
	public ResponseEntity<?> getThongKe(
			@ApiParam(value = "Authorization Token", required = true, defaultValue = "Bearer {access_token}") @RequestHeader(name = "Authorization") String authorizationHeader) {
		SecurityContextHolder.getContext().getAuthentication();
		List<ThongKeDTO> thongKeDTO = new ArrayList<ThongKeDTO>();
		ThongKeDTO temp;
		for (CameraEntity cameraEntity : cameraRepository.findAll()) {
			temp = new ThongKeDTO();
			temp.setProfile(cameraEntity.getRegion().getName());
			Calendar calendar = Calendar.getInstance();
			temp.setDate(calendar.getTime());
			temp.setKhuvuc(cameraEntity.getRegion().getId());
			temp.setSerial(cameraEntity.getSerial());
			thongKeDTO.add(temp);
		}
		return ResponseEntity.ok(thongKeDTO);
	}

}
