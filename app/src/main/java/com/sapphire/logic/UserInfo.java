package com.sapphire.logic;

public class UserInfo {
    private String loginMethod = "";
    private String userName = "";
    private String email = "";
    private String login = "";
    private String phone = "";
    private String userId = "";

    //---------------------Singleton---------------------------
    private static UserInfo userInfo;
    private UserInfo() {}
    public static UserInfo getUserInfo() {
        if(userInfo == null)
            userInfo = new UserInfo();
        return userInfo;
    }
    //---------------------------------------------------------

    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isEmailValid() {
        return isEmailValidation(email);
    }

    public boolean isPhoneValid() {
        return isPhoneValidation(phone);
    }

    private boolean isPhoneValidation(String phone) {
        int length = phone.length();

        if (length < 11 || length > 15)
            return false;

        for (int i=0; i < length; i++)
        {
            String str = phone.substring(i, i+1);
            if ("0123456789".indexOf(str) == -1) {
                return false;
            }
        }

        return true;
    }

    public boolean isLoginValid() {
        boolean isLoginValid = false;
        //сначала нужно понять телефон это или емаил
        boolean isPhone = true;
        if (login.indexOf("@") != -1) {
            isPhone = false;
        }
        if (!isPhone) {
            email = login;
            isLoginValid = isEmailValid();
        } else {
            phone = login;
            isLoginValid = isPhoneValid();
        }
        return isLoginValid;
    }

    private boolean isEmailValidation(String email) {
        //'/^([a-z0-9_\-]+\.)*[a-z0-9_\-]+@([a-z0-9][a-z0-9\-]*[a-z0-9]\.)+[a-z]{2,6}$/i';
        //'^([a-z0-9_]|\\-|\\.)+'.'@'.'(([a-z0-9_]|\\-)+\\.)+'.'[a-z0-9]{2,4}$'
        //String re = "^[\\w-_.]*[\\w-_.]@[\\w].+[\\w]+[\\w]$";
        //String re = "^";
        //re = re + "[\\w-";
        //re = re + "_.]@[\\w].+[\\w]+[\\w]$";
        //Pattern p = Pattern.compile(re);
        //Matcher m = p.matcher(Username);
        //boolean result = m.matches();
        //boolean result = true;

        int length = email.length()-1;

        if (length < 4)
            return false;

        String str1 = email.substring(0, 1);
        if ("@.".indexOf(str1) != -1)
            return false;

        if (email.indexOf(".") == -1)
            return false;

        int ind = email.indexOf("@")+1;
        int ind2 = email.indexOf("@")+2;

        if (ind == -1 || ind == length+1)
            return false;

        String str2 = email.substring(ind, ind2);

        if ("@.".indexOf(str2) != -1)
            return false;

        String str3 = email.substring(length);

        if ("@.".indexOf(str3) != -1)
            return false;

        //точка должна быть после @
        String str4 = email.substring(ind);

        if (str4.indexOf(".") == -1)
            return false;

        if (str4.indexOf("@") != -1)
            return false;

        return true;
        //return !Username.equals("");
    }
}