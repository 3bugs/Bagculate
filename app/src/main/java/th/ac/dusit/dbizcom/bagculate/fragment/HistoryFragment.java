package th.ac.dusit.dbizcom.bagculate.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;
import th.ac.dusit.dbizcom.bagculate.R;
import th.ac.dusit.dbizcom.bagculate.db.LocalDb;
import th.ac.dusit.dbizcom.bagculate.etc.Utils;
import th.ac.dusit.dbizcom.bagculate.model.History;
import th.ac.dusit.dbizcom.bagculate.model.Object;
import th.ac.dusit.dbizcom.bagculate.model.User;
import th.ac.dusit.dbizcom.bagculate.net.ApiClient;
import th.ac.dusit.dbizcom.bagculate.net.GetHistoryResponse;
import th.ac.dusit.dbizcom.bagculate.net.MyRetrofitCallback;
import th.ac.dusit.dbizcom.bagculate.net.WebServices;

public class HistoryFragment extends Fragment {

    private static final String TAG = HistoryFragment.class.getName();
    private static final String KEY_LIST_POSITION = "list_position";

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    private HistoryFragmentListener mListener;
    private List<History> mHistoryList = null;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.progress_bar);
        if (mHistoryList == null) {
            doGetHistory();
        } else {
            setupRecyclerView();
        }
    }

    private void doGetHistory() {
        LocalDb localDb = new LocalDb(getActivity());
        User user = localDb.getUser();
        if (user == null) {
            mHistoryList = localDb.getHistory();
            setupRecyclerView();

        } else {
            mProgressBar.setVisibility(View.VISIBLE);

            Retrofit retrofit = ApiClient.getClient();
            WebServices services = retrofit.create(WebServices.class);

            Call<GetHistoryResponse> call = services.getHistory(user.id);
            call.enqueue(new MyRetrofitCallback<>(
                    getActivity(),
                    null,
                    mProgressBar,
                    new MyRetrofitCallback.MyRetrofitCallbackListener<GetHistoryResponse>() {
                        @Override
                        public void onSuccess(GetHistoryResponse responseBody) {
                            mHistoryList = responseBody.historyList;
                            setupRecyclerView();

                            Log.i(TAG, "History list size: " + String.valueOf(mHistoryList.size()));
                        }

                        @Override
                        public void onError(String errorMessage) {
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

    private void setupRecyclerView() {
        if (getView() == null) return;

        HistoryAdapter adapter = new HistoryAdapter(
                getContext(),
                mHistoryList,
                mListener
        );

        mRecyclerView = getView().findViewById(R.id.history_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SpacingDecoration(getContext()));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryFragmentListener) {
            mListener = (HistoryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HistoryFragmentListener");
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
            mListener.setTitle("ประวัติสัมภาระ");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecyclerView.getLayoutManager() != null) {
            outState.putParcelable(KEY_LIST_POSITION,
                    mRecyclerView.getLayoutManager().onSaveInstanceState());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && mRecyclerView.getLayoutManager() != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(KEY_LIST_POSITION);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    public interface HistoryFragmentListener {
        void setTitle(String title);

        void onClickHistoryItem(History history);
    }

    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        private final Context mContext;
        private final List<History> mHistoryList;
        private final HistoryFragmentListener mListener;

        HistoryAdapter(Context context, List<History> historyList, HistoryFragmentListener listener) {
            this.mContext = context;
            this.mHistoryList = historyList;
            this.mListener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_history, parent, false
            );
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            History history = mHistoryList.get(position);
            holder.mHistory = history;

            int objectCount = 0;
            for (Object object : history.objectList) {
                objectCount += object.count;
            }

            String title = String.format(
                    Locale.getDefault(),
                    "กระเป๋า%s + สิ่งของ %d ชิ้น",
                    history.bag.type == 0 ? "สะพาย" : "ล้อลาก",
                    objectCount
            );
            holder.mTitleTextView.setText(title);

            String weightText = String.format(
                    Locale.getDefault(),
                    "น้ำหนักรวม %.2f กก.",
                    SummaryFragment.calculateTotalWeight(history.bag, history.objectList)
            );
            holder.mSubTitleTextView.setText(weightText);
            holder.mDateTextView.setText(history.createdAt);
        }

        @Override
        public int getItemCount() {
            return mHistoryList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final View mRootView;
            private final TextView mTitleTextView;
            private final TextView mSubTitleTextView;
            private final TextView mDateTextView;

            private History mHistory;

            ViewHolder(View itemView) {
                super(itemView);

                mRootView = itemView;
                mTitleTextView = itemView.findViewById(R.id.title_text_view);
                mSubTitleTextView = itemView.findViewById(R.id.sub_title_text_view);
                mDateTextView = itemView.findViewById(R.id.date_text_view);

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onClickHistoryItem(mHistory);
                        }
                    }
                });
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
