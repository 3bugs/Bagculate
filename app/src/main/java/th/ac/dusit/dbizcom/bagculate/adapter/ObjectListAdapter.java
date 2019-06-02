package th.ac.dusit.dbizcom.bagculate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import th.ac.dusit.dbizcom.bagculate.R;
import th.ac.dusit.dbizcom.bagculate.model.Object;

public class ObjectListAdapter extends ArrayAdapter<Object> {

    private Context mContext;
    private int mResId;
    private List<Object> mObjectList;

    public ObjectListAdapter(Context context, int resource, List<Object> objectList) {
        super(context, resource, objectList);
        mContext = context;
        mResId = resource;
        mObjectList = objectList;
    }

    @Override
    public int getCount() {
        return mObjectList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(mResId, parent, false);
        }

        Object object = mObjectList.get(position);

        TextView objectNameTextView = v.findViewById(R.id.object_name_text_view);
        objectNameTextView.setText(object.name);
        TextView objectWeightTextView = v.findViewById(R.id.object_weight_text_view);
        String weightText = "น้ำหนัก " + String.valueOf(object.weight) + " กรัม";
        objectWeightTextView.setText(weightText);

        return v;
    }
}
