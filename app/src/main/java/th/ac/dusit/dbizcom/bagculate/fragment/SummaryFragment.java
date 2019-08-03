package th.ac.dusit.dbizcom.bagculate.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;
import th.ac.dusit.dbizcom.bagculate.LoginActivity;
import th.ac.dusit.dbizcom.bagculate.MenuActivity;
import th.ac.dusit.dbizcom.bagculate.R;
import th.ac.dusit.dbizcom.bagculate.db.LocalDb;
import th.ac.dusit.dbizcom.bagculate.etc.Utils;
import th.ac.dusit.dbizcom.bagculate.model.Bag;
import th.ac.dusit.dbizcom.bagculate.model.Object;
import th.ac.dusit.dbizcom.bagculate.model.ObjectType;
import th.ac.dusit.dbizcom.bagculate.model.User;
import th.ac.dusit.dbizcom.bagculate.net.AddHistoryResponse;
import th.ac.dusit.dbizcom.bagculate.net.ApiClient;
import th.ac.dusit.dbizcom.bagculate.net.LoginResponse;
import th.ac.dusit.dbizcom.bagculate.net.MyRetrofitCallback;
import th.ac.dusit.dbizcom.bagculate.net.WebServices;

public class SummaryFragment extends Fragment {

    private FloatingActionButton mSaveFab;
    private ProgressBar mProgressBar;

    private SummaryFragmentListener mListener;

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        double totalWeight = mListener.getSelectedBag().weight;
        for (Object o : mListener.getObjectListInBag()) {
            totalWeight += ((o.weight * o.getCount()) / 1000d);
        }

        TextView totalWeightTextView = view.findViewById(R.id.total_weight_text_view);
        String msg = String.format(Locale.getDefault(), "น้ำหนักรวม %.2f กก.", totalWeight);
        totalWeightTextView.setText(msg);

        if (getContext() != null) {
            RecyclerView objectRecyclerView = view.findViewById(R.id.object_recycler_view);
            SummaryAdapter adapter = new SummaryAdapter(
                    getContext(),
                    mListener.getSelectedBag(),
                    mListener.getObjectListInBag()
            );
            objectRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            objectRecyclerView.addItemDecoration(new SummaryFragment.SpacingDecoration(getContext()));
            objectRecyclerView.setAdapter(adapter);
        }

        mProgressBar = view.findViewById(R.id.progress_bar);
        mSaveFab = view.findViewById(R.id.save_history_fab);
        mSaveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveHistory();
            }
        });
    }

    private void doSaveHistory() {
        if (getActivity() == null) return;

        MenuActivity activity = (MenuActivity) getActivity();

        LocalDb localDb = new LocalDb(getActivity());
        User user = localDb.getUser();
        if (user == null) {
            Toast.makeText(getActivity(), "ยังไม่ได้ login", Toast.LENGTH_SHORT).show();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);

            Retrofit retrofit = ApiClient.getClient();
            WebServices services = retrofit.create(WebServices.class);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < activity.getObjectListInBag().size(); i++) {
                Object object = activity.getObjectListInBag().get(i);
                sb.append(object.id).append("-").append(object.getCount());
                if (i < activity.getObjectListInBag().size() - 1) {
                    sb.append(",");
                }
            }

            Call<AddHistoryResponse> call = services.addHistory(
                    user.id,
                    activity.getSelectedBag().id,
                    sb.toString()
            );
            call.enqueue(new MyRetrofitCallback<>(
                    getActivity(),
                    null,
                    null,
                    new MyRetrofitCallback.MyRetrofitCallbackListener<AddHistoryResponse>() {
                        @Override
                        public void onSuccess(AddHistoryResponse responseBody) {
                            mProgressBar.setVisibility(View.GONE);
                            String successMessage = responseBody.errorMessage;
                            Utils.showLongToast(getActivity(), successMessage);

                            if (mListener != null) {
                                mListener.onSaveHistory();
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            mProgressBar.setVisibility(View.GONE);
                            Utils.showOkDialog(
                                    getActivity(),
                                    "Error",
                                    errorMessage
                            );
                        }
                    }
            ));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SummaryFragmentListener) {
            mListener = (SummaryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SummaryFragmentListener");
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
            mListener.setTitle("รายการสิ่งของที่เลือกไว้");
            //mListener.updateNavView(1);
        }
    }

    public interface SummaryFragmentListener {
        void setTitle(String title);

        Bag getSelectedBag();

        List<Object> getObjectListInBag();

        void onSaveHistory();
    }

    private static class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

        private final Context mContext;
        private final Bag mBag;
        private final List<Object> mObjectList;

        SummaryAdapter(Context context, Bag bag, List<Object> objectList) {
            mContext = context;
            mBag = bag;
            mObjectList = objectList;
        }

        @NonNull
        @Override
        public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_summary, parent, false
            );
            return new SummaryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
            if (position == 0) { // กระเป๋า
                holder.mObjectNameTextView.setText(mBag.name);
                String weightText = String.format(Locale.getDefault(), "น้ำหนัก %.2f กก.", mBag.weight);
                holder.mObjectWeightTextView.setText(weightText);
                holder.mBadgeTextView.setText("1");
                holder.mObjectImageView.setImageResource(R.drawable.ic_work);
            } else {
                final Object object = mObjectList.get(position - 1);

                holder.mObject = object;
                holder.mObjectNameTextView.setText(object.name);
                String weightText = "น้ำหนัก " + (object.weight * object.getCount()) + " กรัม";
                holder.mObjectWeightTextView.setText(weightText);

                for (Object o : mObjectList) {
                    if (o.id == object.id) {
                        object.setCount(o.getCount());
                    }
                }

                holder.mBadgeTextView.setText(String.valueOf(object.getCount()));
                if (object.getCount() > 0) {
                    holder.mBadgeTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.mBadgeTextView.setVisibility(View.GONE);
                }

                int imageResource = -1;
                switch (object.type) {
                    case ObjectType.TYPE_SHIRT:
                        imageResource = R.drawable.ic_shirt;
                        break;
                    case ObjectType.TYPE_PANTS:
                        imageResource = R.drawable.ic_pants;
                        break;
                    case ObjectType.TYPE_SHOES:
                        imageResource = R.drawable.ic_shoes;
                        break;
                    case ObjectType.TYPE_THING:
                        //imageResource = R.drawable.ic_shirt;
                        break;
                    case ObjectType.TYPE_ETC:
                        //imageResource = R.drawable.ic_shirt;
                        break;
                }
                if (imageResource != -1) {
                    holder.mObjectImageView.setImageResource(imageResource);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mObjectList.size() + 1;
        }

        class SummaryViewHolder extends RecyclerView.ViewHolder {

            private final View mRootView;
            private final TextView mObjectNameTextView;
            private final TextView mObjectWeightTextView;
            private final ImageView mObjectImageView;
            private final TextView mBadgeTextView;

            private Object mObject;

            SummaryViewHolder(View itemView) {
                super(itemView);

                mRootView = itemView;
                mObjectNameTextView = itemView.findViewById(R.id.object_name_text_view);
                mObjectWeightTextView = itemView.findViewById(R.id.object_weight_text_view);
                mObjectImageView = itemView.findViewById(R.id.object_image_view);
                mBadgeTextView = itemView.findViewById(R.id.badge_text_view);
            }
        }
    }

    public class SpacingDecoration extends RecyclerView.ItemDecoration {

        private final static int MARGIN_TOP = 12;
        private final static int MARGIN_BOTTOM = 12;
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
