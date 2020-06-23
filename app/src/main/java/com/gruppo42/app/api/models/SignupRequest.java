package com.gruppo42.app.api.models;


import com.google.gson.annotations.SerializedName;

public class SignUpRequest
{
    @SerializedName("name")
    private String name;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("image")
    private String image;
    @SerializedName("password")
    private String password;

    public SignUpRequest() {
    }

    public SignUpRequest(String name, String username, String email, String image, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.image = image;
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail() { return email; }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}