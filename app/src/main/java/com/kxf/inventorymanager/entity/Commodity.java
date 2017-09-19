package com.kxf.inventorymanager.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "Commodity")
public class Commodity {
	@Column(name = "id", isId = true)
	private int id;
	@Column(name = "userId")
	private int userId;
	@Column(name = "name")
	private String name;
	@Column(name = "qcode")
	private String qcode;
	@Column(name = "ymd")
	private String ymd;//yyyyMMdd
	@Column(name = "hmsS")
	private String hmsS;//HHmmssSSS
	@Column(name = "state")
	private int state = 0;//0已入库，1是以出库
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQcode() {
		return qcode;
	}
	public void setQcode(String qcode) {
		this.qcode = qcode;
	}
	public String getYmd() {
		return ymd;
	}
	public void setYmd(String ymd) {
		this.ymd = ymd;
	}
	public String getHmsS() {
		return hmsS;
	}
	public void setHmsS(String hmsS) {
		this.hmsS = hmsS;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "Commodity [id=" + id + ", userId=" + userId + ", name=" + name
				+ ", qcode=" + qcode + ", ymd=" + ymd + ", hmsS=" + hmsS
				+ ", state=" + state + "]";
	}
}
