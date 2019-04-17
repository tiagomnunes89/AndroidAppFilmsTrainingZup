package br.com.estagio.oletrainning.zup.otmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("completeName")
    @Expose
    private String completeName;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("registrationStatus")
    @Expose
    private String registrationStatus;

    @SerializedName("password")
    @Expose
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompleteName() {
        return completeName;
    }
}
