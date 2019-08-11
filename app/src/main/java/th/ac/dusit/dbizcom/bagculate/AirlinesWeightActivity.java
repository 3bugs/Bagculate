package th.ac.dusit.dbizcom.bagculate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.ac.dusit.dbizcom.bagculate.model.Airline;

public class AirlinesWeightActivity extends AppCompatActivity {

    private List<Airline> mAirlineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airlines_weight);

        setTitle("เกณฑ์น้ำหนักของสายการบิน");

        prepareData();
        setupRecyclerView();
    }

    private void prepareData() {
        mAirlineList = new ArrayList<>();
        mAirlineList.add(new Airline(
                "การบินไทย", R.drawable.airline_thai_airways, "https://www.thaiairways.com/th_TH/Terms_condition/baggage_policy.page"
        ));
        mAirlineList.add(new Airline(
                "บางกอกแอร์เวย์ส", R.drawable.airline_bangkok_airways, "https://www.bangkokair.com/tha/baggage-allowance"
        ));
        mAirlineList.add(new Airline(
                "นกแอร์", R.drawable.airline_nok_air, "https://content.nokair.com/th/Journey-Planning/Baggage.aspx"
        ));
        mAirlineList.add(new Airline(
                "ไทยแอร์เอเชีย", R.drawable.airline_air_asia, "https://www.airasia.com/th/th/baggage.page"
        ));
        mAirlineList.add(new Airline(
                "ไทยสมายล์", R.drawable.airline_thai_smile, "https://www.thaismileair.com/th/BaggagePolicy"
        ));
        mAirlineList.add(new Airline(
                "ไทยไลอ้อนแอร์", R.drawable.airline_thai_lion_air, "https://www.lionairthai.com/th/ThaiLionAir-Experience/Baggage-Allowance"
        ));
        mAirlineList.add(new Airline(
                "เจ็ทสตาร์ เอเซีย", R.drawable.airline_jetasia_airways, "https://www.jetstar.com/th/th/flights/baggage"
        ));
        mAirlineList.add(new Airline(
                "นิวเจน แอร์เวย์ส", R.drawable.airline_newgen_airways, "https://www.ngairways.com/baggagepolicy"
        ));
        mAirlineList.add(new Airline(
                "นกสกู๊ต", R.drawable.airline_nokscoot, "https://www.nokscoot.com/th/baggage-2/"
        ));
        mAirlineList.add(new Airline(
                "ไทยเวียดเจ็ทแอร์", R.drawable.airline_thai_vietjet, "https://www.vietjetair.com/Sites/Web/th-TH/NewsDetail/%e0%b8%9a%e0%b8%a3%e0%b8%81%e0%b8%b2%e0%b8%a3%e0%b8%82%e0%b8%99%e0%b8%96%e0%b8%b2%e0%b8%a2%e0%b8%81%e0%b8%a3%e0%b8%b0%e0%b9%80%e0%b8%9b%e0%b8%b2/%201809/%e0%b8%9a%e0%b8%a3%e0%b8%81%e0%b8%b2%e0%b8%a3%e0%b8%aa%e0%b8%a1%e0%b8%a0%e0%b8%b2%e0%b8%a3%e0%b8%b0"
        ));
        mAirlineList.add(new Airline(
                "วิสดอม แอร์เวย์", R.drawable.airline_wisdom_airways, "http://wisdomairways.com/checked_baggage.php"
        ));
    }

    private void setupRecyclerView() {
        AirlinesAdapter adapter = new AirlinesAdapter(AirlinesWeightActivity.this, mAirlineList);

        RecyclerView airlinesRecyclerView = findViewById(R.id.airlines_recycler_view);
        airlinesRecyclerView.setLayoutManager(new LinearLayoutManager(AirlinesWeightActivity.this));
        airlinesRecyclerView.addItemDecoration(new SpacingDecoration(AirlinesWeightActivity.this));
        airlinesRecyclerView.setAdapter(adapter);
    }

    private static class AirlinesAdapter extends RecyclerView.Adapter<AirlinesAdapter.ViewHolder> {

        private final Context mContext;
        private final List<Airline> mAirlineList;

        AirlinesAdapter(Context context, List<Airline> airlineList) {
            this.mContext = context;
            this.mAirlineList = airlineList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_airline, parent, false
            );
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Airline airline = mAirlineList.get(position);
            holder.mAirline = airline;
            holder.mNameTextView.setText(airline.name);
            holder.mImageView.setImageResource(airline.imageRes);
        }

        @Override
        public int getItemCount() {
            return mAirlineList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final View mRootView;
            private final TextView mNameTextView;
            private final ImageView mImageView;

            private Airline mAirline;

            ViewHolder(View itemView) {
                super(itemView);

                mRootView = itemView;
                mNameTextView = itemView.findViewById(R.id.name_text_view);
                mImageView = itemView.findViewById(R.id.image_view);

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(mAirline.url));
                        mContext.startActivity(i);
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
