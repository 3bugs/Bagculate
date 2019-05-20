package th.ac.dusit.dbizcom.bagculate.model;

import com.google.gson.annotations.SerializedName;

public class Object {

    @SerializedName("id")
    public final int id;
    @SerializedName("name")
    public final String name;
    @SerializedName("type")
    public final int type;
    @SerializedName("weight")
    public final double weight;

    public Object(int id, String name, int type, double weight) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return name;
    }
}
