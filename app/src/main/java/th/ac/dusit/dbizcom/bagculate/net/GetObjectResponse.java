package th.ac.dusit.dbizcom.bagculate.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import th.ac.dusit.dbizcom.bagculate.model.Object;

public class GetObjectResponse extends BaseResponse {
    @SerializedName("data_list")
    public List<Object> objectList;
}