//package com.camera.service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.camera.common.APIResponse;
//import com.camera.dto.LoginRequestDTO;
//import com.camera.dto.SignUpRequestDTO;
//import com.camera.entity.UserEntity;
//import com.camera.repo.UserRepository;
//import com.camera.util.JwtUtils;
//
//@Service
//public class LoginService {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private JwtUtils jwtUtils;
//    
//    
//    public APIResponse signUp(SignUpRequestDTO signUpRequestDTO) {
//        APIResponse apiResponse = new APIResponse();
//
//        // validation
//
//        // dto to entity
//        UserEntity userEntity = new UserEntity();
//        userEntity.setName(signUpRequestDTO.getName());
//        userEntity.setUserName(signUpRequestDTO.getUserName());
//        userEntity.setEmail(signUpRequestDTO.getEmail());
//        userEntity.setPassword(signUpRequestDTO.getPassword());
//
//        // store entity
//        userEntity = userRepository.save(userEntity);
//
//        // generate jwt
//        String token = jwtUtils.generateJwtToken(userEntity);
//
//        Map<String , Object> data = new HashMap<>();
//        data.put("accessToken", token);
//
//        apiResponse.setData(data);
//
//        // return
//        return apiResponse;
//    }
//    
//    public APIResponse login(LoginRequestDTO loginRequestDTO) {
//
//        APIResponse apiResponse = new APIResponse();
//
//
//        // validation
//
//        // verify UserEntity exist with given email and password
//        UserEntity userEntity = userRepository.findOneByEmailIgnoreCaseAndPassword(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
//
//        // response
//        if(userEntity == null){
//            apiResponse.setData("UserEntity login failed");
//            return apiResponse;
//        }
//
//        // generate jwt
//        String token = jwtUtils.generateJwtToken(userEntity);
////        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEntity.getId());
//
//
//        Map<String , Object> data = new HashMap<>();
//        data.put("accessToken", token);
//
//        apiResponse.setData(data);
//
//        return apiResponse;
//    }
//    
//
//
//
//}
