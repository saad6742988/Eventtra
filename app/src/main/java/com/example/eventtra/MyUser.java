package com.example.eventtra;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

public class MyUser {
    private String fname;
    private String lname;
    private String email;
    private String phone;
    private String role;
    private String userId;
    private Uri profilePic;
    private String deviceToken;


    public MyUser() {
    }


    @Exclude
    public Uri getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Uri profilePic) {
        this.profilePic = profilePic;
    }

    @Exclude
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MyUser(String fname, String lname, String email, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.role = "attendee";
        deviceToken = "";
    }

    public MyUser(String fname, String lname, String email, String phone, String role, String userId, Uri profilePic, String deviceToken) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.userId = userId;
        this.profilePic = profilePic;
        this.deviceToken = deviceToken;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", userId='" + userId + '\'' +
                ", profilePic=" + profilePic +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyUser myUser = (MyUser) o;

        if (!fname.equals(myUser.fname)) return false;
        if (!lname.equals(myUser.lname)) return false;
        if (!email.equals(myUser.email)) return false;
        if (!phone.equals(myUser.phone)) return false;
        if (!role.equals(myUser.role)) return false;
        if (!userId.equals(myUser.userId)) return false;
        if (!profilePic.equals(myUser.profilePic)) return false;
        return deviceToken.equals(myUser.deviceToken);
    }

    @Override
    public int hashCode() {
        int result = fname.hashCode();
        result = 31 * result + lname.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + profilePic.hashCode();
        result = 31 * result + deviceToken.hashCode();
        return result;
    }
}
