package th.ac.dusit.dbizcom.bagculate.model;

import com.google.gson.annotations.SerializedName;

public class Object {

    @SerializedName("id")
    public final int id;
    @SerializedName("name")
    public final String name;
    @SerializedName("type")
    public final String type;
    @SerializedName("weight")
    public final int weight;
    @SerializedName("count")
    public int count;

    public Object(int id, String name, String type, int weight, int count) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increaseCount() {
        this.count++;
    }

    public boolean decreaseCount() {
        this.count--;
        if (this.count < 0) {
            this.count = 0;
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
