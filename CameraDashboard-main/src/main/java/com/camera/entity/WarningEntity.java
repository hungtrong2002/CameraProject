package com.camera.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "warning")
public class WarningEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String name = null;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "camera_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@NotNull
	private CameraEntity camera;
	@Column
	private String content = null;
	@Column
	private String time_from = null;
	@Column
	private String time_to = null;
	@Column
	private String level = null;
	@Column
	private String face_image = null;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@NotNull
	private RegionEntity region_id;
	

	public CameraEntity getCamera() {
		return camera;
	}

	public void setCamera(CameraEntity camera) {
		this.camera = camera;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime_from() {
		return time_from;
	}
	public void setTime_from(String time_from) {
		this.time_from = time_from;
	}
	public String getTime_to() {
		return time_to;
	}
	public void setTime_to(String time_to) {
		this.time_to = time_to;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getFace_image() {
		return face_image;
	}
	public void setFace_image(String face_image) {
		this.face_image = face_image;
	}

	public RegionEntity getRegion_id() {
		return region_id;
	}

	public void setRegion_id(RegionEntity region_id) {
		this.region_id = region_id;
	}
}
