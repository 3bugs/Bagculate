package th.ac.dusit.dbizcom.bagculate.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;
import th.ac.dusit.dbizcom.bagculate.R;
import th.ac.dusit.dbizcom.bagculate.etc.Utils;
import th.ac.dusit.dbizcom.bagculate.model.Bag;
import th.ac.dusit.dbizcom.bagculate.net.ApiClient;
import th.ac.dusit.dbizcom.bagculate.net.GetBagResponse;
import th.ac.dusit.dbizcom.bagculate.net.MyRetrofitCallback;
import th.ac.dusit.dbizcom.bagculate.net.WebServices;

public class BagFragment extends Fragment {

    private BagFragmentListener mListener;

    private RecyclerView mBagRecyclerView;

    private List<Bag> mBagList = null;
    private BagListAdapter mAdapter;

    public BagFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBagRecyclerView = view.findViewById(R.id.bag_recycler_view);

        RadioGroup bagTypeRadioGroup = view.findViewById(R.id.bag_type_radio_group);
        bagTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (R.id.bag_type_0_radio_button == id) {
                    mAdapter.setShowBagType(0);
                } else {
                    mAdapter.setShowBagType(1);
                }
            }
        });

        doGetBag();
    }

    private void doGetBag() {
        Retrofit retrofit = ApiClient.getClient();
        WebServices services = retrofit.create(WebServices.class);

        Call<GetBagResponse> call = services.getBag();
        call.enqueue(new MyRetrofitCallback<>(
                getActivity(),
                null,
                null,
                new MyRetrofitCallback.MyRetrofitCallbackListener<GetBagResponse>() {
                    @Override
                    public void onSuccess(GetBagResponse responseBody) { // register สำเร็จ
                        if (getContext() == null) {
                            return;
                        }

                        mBagList = responseBody.bagList;
                        mAdapter = new BagListAdapter(
                                getContext(),
                                mBagList,
                                mListener,
                                0
                        );
                        mBagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mBagRecyclerView.addItemDecoration(new SpacingDecoration(getContext()));
                        mBagRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(String errorMessage) { // register ไม่สำเร็จ หรือเกิดข้อผิดพลาดอื่นๆ (เช่น ไม่มีเน็ต, server ล่ม)
                        if (getActivity() != null) {
                            Utils.showOkDialog(getActivity(), "ผิดพลาด", errorMessage);
                        }
                    }
                }
        ));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BagFragmentListener) {
            mListener = (BagFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BagFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.setTitle("เลือกกระเป๋า");
            //mListener.updateNavView(0);
        }
    }

    public interface BagFragmentListener {
        void setTitle(String title);

        void updateNavView(int which);

        void onSelectBag(Bag bag);
    }

    private static class BagListAdapter extends RecyclerView.Adapter<BagListAdapter.BagViewHolder> {

        private final Context mContext;
        private List<Bag> mOriginalBagList;
        private List<Bag> mBagList;
        private final BagFragmentListener mListener;

        BagListAdapter(Context context, List<Bag> bagList, BagFragmentListener listener, int bagType) {
            mContext = context;
            mOriginalBagList = bagList;
            mListener = listener;
            setShowBagType(bagType);
        }

        @NonNull
        @Override
        public BagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_bag, parent, false
            );
            return new BagViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BagViewHolder holder, int position) {
            final Bag bag = mBagList.get(position);

            holder.mBagNameTextView.setText(bag.name);
            String weightText = String.format(Locale.getDefault(), "น้ำหนัก %.2f กก.", bag.weight);
            holder.mBagWeightTextView.setText(weightText);
            holder.mBag = bag;
        }

        @Override
        public int getItemCount() {
            return mBagList.size();
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

        class BagViewHolder extends RecyclerView.ViewHolder {

            private final View mRootView;
            private final TextView mBagNameTextView;
            private final TextView mBagWeightTextView;

            private Bag mBag;

            BagViewHolder(View itemView) {
                super(itemView);

                mRootView = itemView;
                mBagNameTextView = itemView.findViewById(R.id.bag_name_text_view);
                mBagWeightTextView = itemView.findViewById(R.id.bag_weight_text_view);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onSelectBag(mBag);
                        }
                    }
                });
            }
        }
    }

    public class SpacingDecoration extends RecyclerView.ItemDecoration {

        private final static int MARGIN_TOP = 0;
        private final static int MARGIN_BOTTOM = 16;
        private final int mMarginTop, mMarginBottom;

        SpacingDecoration(@NonNull Context context) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            mMarginTop = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    MARGIN_TOP,
                    metrics
            );
            mMarginBottom = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    MARGIN_BOTTOM,
                    metrics
            );
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            final int itemPosition = parent.getChildAdapterPosition(view);
            if (itemPosition == RecyclerView.NO_POSITION) {
                return;
            }
            if (itemPosition == 0) {
                outRect.top = mMarginTop;
            }
            final RecyclerView.Adapter adapter = parent.getAdapter();
            if ((adapter != null) && (itemPosition == adapter.getItemCount() - 1)) {
                outRect.bottom = mMarginBottom;
            }
        }
    }

}
