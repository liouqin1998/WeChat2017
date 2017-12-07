package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class User implements Serializable{
	private String username;
	private String password;
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	private String nickname;
	private String sex;
	private int age;
	private String signatrue;
	private String imagePath;
	
	private Map<String, HashSet<User>> friends;
	public Map<String, HashSet<User>> getFriends() {
		return friends;
	}
	public void setFriends(Map<String, HashSet<User>> friends) {
		this.friends = friends;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSignatrue() {
		return signatrue;
	}
	public void setSignatrue(String signatrue) {
		this.signatrue = signatrue;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public User(String username, String password, String nickname,String sex, int age,  String signatrue,
			String imagePath) {
		super();
		this.username = username;
		this.password = password;
		this.sex = sex;
		this.age = age;
		this.nickname = nickname;
		this.signatrue = signatrue;
		this.imagePath = imagePath;
	}
	public User() {
		super();
	}
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", sex=" + sex + ", age=" + age + ", nickname="
				+ nickname + ", signatrue=" + signatrue + ", imagePath=" + imagePath + "]";
	}
}
