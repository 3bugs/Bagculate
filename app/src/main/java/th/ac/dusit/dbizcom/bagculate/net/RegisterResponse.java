package th.ac.dusit.dbizcom.bagculate.net;

import com.google.gson.annotations.SerializedName;

import th.ac.dusit.dbizcom.bagculate.model.User;

public class RegisterResponse extends BaseResponse {

    @SerializedName("user")
    public User user;
}
