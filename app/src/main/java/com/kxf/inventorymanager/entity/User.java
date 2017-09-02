package com.kxf.inventorymanager.entity;

import java.io.Serializable;
public class User implements Serializable {
	private int id;
	private String name;
	private String pw;
	private double money;
	private int age;
	private String tel;
	private String address;
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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
		return "User [id=" + id + ", name=" + name + ", pw=" + pw + ", money="
				+ money + ", age=" + age + ", tel=" + tel + ", address="
				+ address + ", info=" + info + "]";
	}

}
