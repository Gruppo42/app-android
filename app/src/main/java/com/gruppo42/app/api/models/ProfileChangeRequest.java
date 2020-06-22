package com.gruppo42.app.api.models;

public class ProfileChangeRequest
{
    private String name;
    private String email;
    private String image;

    public ProfileChangeRequest(String name, String email, String image)
    {
        this.name = name;
        this.image = image;
        this.email = email;

    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}

