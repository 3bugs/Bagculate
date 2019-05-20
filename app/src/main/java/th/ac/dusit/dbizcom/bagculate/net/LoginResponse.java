package th.ac.dusit.dbizcom.bagculate.net;

import com.google.gson.annotations.SerializedName;

import th.ac.dusit.dbizcom.bagculate.model.User;

public class LoginResponse extends BaseResponse {

    @SerializedName("login_success")
    public boolean loginSuccess;
    @SerializedName("user")
    public User user;
}
