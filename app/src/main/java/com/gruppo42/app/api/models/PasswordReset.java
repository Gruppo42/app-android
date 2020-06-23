package com.gruppo42.app.api.models;

public class PasswordReset
{

    private String password1;
    private String passsword2;

    public PasswordReset()
    {
    }

    public PasswordReset(String password1, String passsword2)
    {
        this.password1 = password1;
        this.passsword2 = passsword2;
    }

    public String getPassword1()
    {
        return password1;
    }

    public void setPassword1(String password1)
    {
        this.password1 = password1;
    }

    public String getPasssword2()
    {
        return passsword2;
    }

    public void setPasssword2(String passsword2)
    {
        this.passsword2 = passsword2;
    }
}
