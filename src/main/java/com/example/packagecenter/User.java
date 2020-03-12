package com.example.packagecenter;

public class User {
    private int userId;
    private int userPassword;
    private String userName=null;
    private String firstName=null;
    private String lastName;
    private String email;
    private String stringIdOfImg;



    public User() {

    }






    public User(String userName,int userPassword) {
        this.userName = userName;
        this.userPassword=userPassword;

    }

    public User(int userId, String userName, String firstName, String lastName, String email,int userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userPassword=userPassword;
    }

    public User(String firstName, String lastName, String email, String stringIdOfImg) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.stringIdOfImg = stringIdOfImg;
    }

    public String getStringIdOfImg() {
        return stringIdOfImg;
    }

    public void setStringIdOfImg(String stringIdOfImg) {
        this.stringIdOfImg = stringIdOfImg;
    }

    public int getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(int userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
