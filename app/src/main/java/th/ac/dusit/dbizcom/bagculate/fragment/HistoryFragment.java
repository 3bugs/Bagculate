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
import th.ac.dusit.dbizcom.bagculate.model.Bag;
import th.ac.dusit.dbizcom.bagculate.model.History;
import th.ac.dusit.dbizcom.bagculate.model.Object;
import th.ac.dusit.dbizcom.bagculate.model.User;
import th.ac.dusit.dbizcom.bagculate.net.ApiClient;
import th.ac.dusit.dbizcom.bagculate.net.GetHistoryResponse;
import th.ac.dusit.dbizcom.bagculate.net.MyRetrofitCallback;
import th.ac.dusit.dbizcom.bagculate.net.WebServices;

public class HistoryFragment extends Fragment {

    private static final String TAG = HistoryFragment.class.getName();

    private ProgressBar mProgressBar;

    private HistoryFragmentListener mListener;
    private List<History> mHistoryList;

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
        doGetHistory();
    }

    private void doGetHistory() {
        LocalDb localDb = new LocalDb(getActivity());
        User user = localDb.getUser();
        if (user == null) {

        } else {
            mProgressBar.setVisibility(View.VISIBLE);

            Retrofit retrofit = ApiClient.getClient();
            WebServices services = retrofit.create(WebServices.class);

            Call<GetHistoryResponse> call = services.getHistory(user.id);
            call.enqueue(new MyRetrofitCallback<>(
                    getActivity(),
                    null,
                    null,
                    new MyRetrofitCallback.MyRetrofitCallbackListener<GetHistoryResponse>() {
                        @Override
                        public void onSuccess(GetHistoryResponse responseBody) {
                            mProgressBar.setVisibility(View.GONE);
                            mHistoryList = responseBody.historyList;
                            setupRecyclerView();

                            Log.i(TAG, "History list size: " + String.valueOf(mHistoryList.size()));
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

    private void setupRecyclerView() {
        if (getView() == null) return;

        HistoryAdapter adapter = new HistoryAdapter(
                getContext(),
                mHistoryList
        );

        RecyclerView recyclerView = getView().findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacingDecoration(getContext()));
        recyclerView.setAdapter(adapter);
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

    public interface HistoryFragmentListener {
        void setTitle(String title);

        Bag getSelectedBag();

        List<Object> getObjectListInBag();
    }

    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        private final Context mContext;
        private final List<History> mHistoryList;

        HistoryAdapter(Context context, List<History> historyList) {
            this.mContext = context;
            this.mHistoryList = historyList;
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

            int objectCount = 0;
            for (Object object : history.objectList) {
                objectCount += object.count;
            }

            holder.mTitleTextView.setText(history.bag.name);
            String objectCountText = String.format(
                    Locale.getDefault(),
                    "จำนวนสิ่งของ %d ชิ้น",
                    objectCount
            );
            holder.mSubTitleTextView.setText(objectCountText);
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

            private Object mObject;

            ViewHolder(View itemView) {
                super(itemView);

                mRootView = itemView;
                mTitleTextView = itemView.findViewById(R.id.title_text_view);
                mSubTitleTextView = itemView.findViewById(R.id.sub_title_text_view);
                mDateTextView = itemView.findViewById(R.id.date_text_view);
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
