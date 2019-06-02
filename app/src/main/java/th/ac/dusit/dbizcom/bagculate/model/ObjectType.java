package th.ac.dusit.dbizcom.bagculate.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectType {

    public static final String TYPE_SHIRT = "shirt";
    public static final String TYPE_PANTS = "pants";
    public static final String TYPE_SHOES = "shoes";
    public static final String TYPE_THING = "thing";
    public static final String TYPE_ETC = "etc";

    public final String type;
    public final String name;
    public final List<Object> objectList;

    public ObjectType(String type) {
        this.type = type;
        this.objectList = new ArrayList<>();
        switch (type) {
            case TYPE_SHIRT:
                name = "เสื้อ";
                break;
            case TYPE_PANTS:
                name = "กางเกง";
                break;
            case TYPE_SHOES:
                name = "รองเท้า";
                break;
            case TYPE_THING:
                name = "สิ่งของ";
                break;
            case TYPE_ETC:
                name = "อื่นๆ";
                break;
            default:
                name = "";
                break;
        }
    }
}
