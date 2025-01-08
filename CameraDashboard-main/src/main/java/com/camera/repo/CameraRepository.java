package com.camera.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.camera.entity.CameraEntity;

@Repository
public interface CameraRepository extends JpaRepository<CameraEntity, String> {
	public List<CameraEntity> findAll();

	public List<CameraEntity> findByRegionId(Long regionId);

//	@Modifying
//	@Query("delete from CameraEntity b where b.id=:id")
//	public void deleteCameras(@Param("id") Long id);
	public void deleteById(Long id);

	public CameraEntity findById(Long id);

}
