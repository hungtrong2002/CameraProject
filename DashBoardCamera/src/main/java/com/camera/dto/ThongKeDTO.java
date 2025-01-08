package com.camera.dto;

import java.util.Date;

public class ThongKeDTO {
	private String profile;
	private long khuvuc;
	private String serial;
	private Date date;

	public ThongKeDTO() {
	};

	public ThongKeDTO(String profile, long khuvuc, String serial, Date date) {
		this.profile = profile;
		this.khuvuc = khuvuc;
		this.serial = serial;
		this.date = date;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public long getKhuvuc() {
		return khuvuc;
	}

	public void setKhuvuc(long khuvuc) {
		this.khuvuc = khuvuc;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
