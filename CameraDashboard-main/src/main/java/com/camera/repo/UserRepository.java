package com.camera.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.camera.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    UserEntity findOneByEmailIgnoreCaseAndPassword(String email, String password);
    UserEntity findOneByEmail(String Email);
	UserEntity findByUserName(String username);
	UserEntity findById(Long userId);
}
