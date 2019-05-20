package th.ac.dusit.dbizcom.bagculate.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    public final int id;
    @SerializedName("username")
    public final String username;
    @SerializedName("name")
    public final String name;

    public User(int id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.username;
    }
}
