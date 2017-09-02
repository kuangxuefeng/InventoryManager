package com.kxf.inventorymanager.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "User")
public class User {
	@Column(name = "id", isId = true)
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "pw")
	private String pw;
	@Column(name = "permissions")
	private int permissions = 0;//0是普通用户，1是管理员，2是系统管理员
	@Column(name = "tel")
	private String tel;
	@Column(name = "address")
	private String address;
	@Column(name = "info")
	private String info;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public int getPermissions() {
		return permissions;
	}

	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", pw='" + pw + '\'' +
				", permissions=" + permissions +
				", tel='" + tel + '\'' +
				", address='" + address + '\'' +
				", info='" + info + '\'' +
				'}';
	}
}
