package com.camera.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.camera.entity.RegionEntity;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, String> {
	List<RegionEntity> findAll();

	RegionEntity findBySerial(String serial);

	void deleteById(Long id);

	RegionEntity findById(Long id);
}
