package th.ac.dusit.dbizcom.bagculate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.ac.dusit.dbizcom.bagculate.R;
import th.ac.dusit.dbizcom.bagculate.model.Bag;

public class BagListAdapter extends ArrayAdapter<Bag> {

    private Context mContext;
    private int mResId;
    private List<Bag> mOriginalBagList;
    private List<Bag> mBagList;

    public BagListAdapter(Context context, int resource, List<Bag> bagList, int bagType) {
        super(context, resource, bagList);
        mContext = context;
        mResId = resource;
        mOriginalBagList = bagList;
        setShowBagType(bagType);
    }

    @Override
    public int getCount() {
        return mBagList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(mResId, parent, false);
        }

        Bag bag = mBagList.get(position);

        TextView bagNameTextView = v.findViewById(R.id.bag_name_text_view);
        bagNameTextView.setText(bag.name);
        TextView bagWeightTextView = v.findViewById(R.id.bag_weight_text_view);
        String weightText = "น้ำหนัก " + String.valueOf(bag.weight) + " กก.";
        bagWeightTextView.setText(weightText);

        return v;
    }

    public void setShowBagType(int type) {
        List<Bag> bagList = new ArrayList<Bag>();

        for (Bag bag : mOriginalBagList) {
            if (bag.type == type) {
                bagList.add(bag);
            }
        }
        mBagList = bagList;
        notifyDataSetChanged();
    }

    public Bag getBagByPosition(int position) {
        return mBagList.get(position);
    }
}
