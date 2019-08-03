package th.ac.dusit.dbizcom.bagculate.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import th.ac.dusit.dbizcom.bagculate.model.History;
import th.ac.dusit.dbizcom.bagculate.model.Object;

public class GetHistoryResponse extends BaseResponse {

    @SerializedName("data_list")
    public List<History> historyList;
}