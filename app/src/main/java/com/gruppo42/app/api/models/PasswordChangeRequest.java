package com.gruppo42.app.api.models;

public class PasswordChangeRequest
{
    private String oldPassword;

    private String newPassword;

    public PasswordChangeRequest()
    {
    }

    public PasswordChangeRequest(String oldPassword, String newPassword)
    {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword()
    {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword)
    {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword()
    {
        return newPassword;
    }

    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }
}