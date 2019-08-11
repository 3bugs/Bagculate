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

    public History(int id, String createdAt, Bag bag, List<Object> objectList) {
        this.id = id;
        this.createdAt = createdAt;
        this.bag = bag;
        this.objectList = objectList;
    }

    @Override
    public String toString() {
        return createdAt;
    }
}
