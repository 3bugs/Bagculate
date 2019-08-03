package th.ac.dusit.dbizcom.bagculate.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class History {

    @SerializedName("id")
    public int id;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("bag")
    public Bag bag;
    @SerializedName("object_list")
    public List<Object> objectList;

    @Override
    public String toString() {
        return createdAt;
    }
}
