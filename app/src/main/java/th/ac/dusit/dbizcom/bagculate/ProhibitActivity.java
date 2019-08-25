package th.ac.dusit.dbizcom.bagculate;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import th.ac.dusit.dbizcom.bagculate.model.Prohibit;

public class ProhibitActivity extends AppCompatActivity {

    private List<Prohibit> mProhibitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prohibit);

        prepareData();
        setupRecyclerView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("สิ่งของที่ห้ามนำขึ้นเครื่องบิน");
        }
    }

    private void prepareData() {
        mProhibitList = new ArrayList<>();
        mProhibitList.add(new Prohibit(
                "เครื่องดื่ม เครื่องสำอาง",
                "ของเหลวทุกชนิดที่มีความจุต่อชิ้นเกิน 100 มล. รวมไม่เกิน 1 ลิตร (1,000 มล.) เช่น เจล น้ำหอม สบู่เหลว สเปรย์ ห้ามพกติดตัวขึ้นเครื่องบิน แต่สามารถนำใส่กระเป๋าที่จะโหลดได้ ทั้งนี้เป็นมาตรการป้องกันการก่อการร้าย ที่เข้มงวดขึ้นหลังจากเหตุการณ์ 911 แต่ถ้าเป็นยาที่จำเป็นต้องพกติดตัว ก็สามารถพกพาได้ในปริมาณที่เหมาะสม เช่น ชุดยาแก้เบาหวาน",
                R.drawable.prohibit_cosmetics
        ));
        mProhibitList.add(new Prohibit(
                "แบตเตอรี่สำรอง (Power Bank)",
                "ไม่สามารถนำแบตเตอรี่สำรองทุกชนิดใส่กระเป๋าที่นำไปเช็คอิน แต่สามารถพกพาใส่กระเป๋าถือขึ้นไปได้ โดยต้องเป็นแบตเตอรี่สำรองที่มีค่าความจุไฟฟ้าไม่เกิน 32,000 mAh ไม่เกินคนละ 2 ชิ้น",
                R.drawable.prohibit_power_bank
        ));
        mProhibitList.add(new Prohibit(
                "อาวุธ",
                "ปืน และอาวุธทุกชนิด รวมถึงอาวุธโดยสภาพ เช่น มีด สนับมือ ดาบ ทวน กระบอง และสิ่งเทียมอาวุธ เช่น ปืนไฟแช็ก ระเบิดไฟแช็ก ปืนเด็กเล่น",
                R.drawable.prohibit_weapon
        ));
        mProhibitList.add(new Prohibit(
                "ของมีคม",
                "ไม้บรรทัดเหล็ก คัตเตอร์ กรรไกรตัดเล็บ เข็มเย็บผ้า มีด มีดพับ มีดพก และของมีคมทุกชนิด ที่อาจทำให้เกิดอันตราย แต่หากเป็นของใช้ที่จำเป็น อาจอนุญาตให้เก็บไว้ในกระเป๋าใบใหญ่ที่โหลดใต้เครื่องบินได้ แต่ไม่อนุญาตให้พกพาขึ้นเครื่องบิน",
                R.drawable.prohibit_knife
        ));
        mProhibitList.add(new Prohibit(
                "วัตถุไวไฟ",
                "เพื่อไม่ให้เกิดปัญหาระเบิดหรือเพลิงไหม้ เช่น น้ำมันไฟแช็ก เชื้อเพลิงแข็ง สีน้ำมัน ไม้ขีดไฟ ฯลฯ",
                R.drawable.prohibit_fire
        ));
        mProhibitList.add(new Prohibit(
                "อาหารที่มีกลิ่นแรง",
                "อาหารต่างๆ เช่น อาหารทะเล เนื้อสัตว์สด/แช่แข็ง ทุเรียน ปลาร้า ต้องแพ็คมาอย่างเหมาะสม",
                R.drawable.prohibit_durian
        ));
        mProhibitList.add(new Prohibit(
                "สัตว์มีพิษ สัตว์ดุร้าย",
                "รวมถึงสัตว์ขนาดใหญ่ สัตว์สงวน นอกจากอาจรบกวนผู้อื่นแล้ว ยังผิดกฎหมายอีกด้วย",
                R.drawable.prohibit_snake
        ));
        mProhibitList.add(new Prohibit(
                "สารอันตรายต่างๆ",
                "เช่น สารกำจัดแมลง สารหนู วัตถุออกซิไดซ์ เช่น แอมโมเนียไนเตรท แคลเซียมเปอร์ออกไซด์ วัตถุก๊าซมันตภาพรังสี เช่น ธาตุยูเรเนียม วัตถุกัดกร่อน เช่น แบตเตอรีที่บรรจุสารกัดกร่อน น้ำกรด ปรอท ฯลฯ",
                R.drawable.prohibit_danger
        ));
        mProhibitList.add(new Prohibit(
                "สิ่งของอื่นๆ",
                "เช่น แม่เหล็ก น้ำแข็งแห้ง",
                R.drawable.prohibit_magnet
        ));

    }

    private void setupRecyclerView() {
        ProhibitAdapter adapter = new ProhibitAdapter(ProhibitActivity.this, mProhibitList);

        RecyclerView airlinesRecyclerView = findViewById(R.id.prohibit_recycler_view);
        airlinesRecyclerView.setLayoutManager(new LinearLayoutManager(ProhibitActivity.this));
        airlinesRecyclerView.addItemDecoration(new SpacingDecoration(ProhibitActivity.this));
        airlinesRecyclerView.setAdapter(adapter);
    }

    private static class ProhibitAdapter extends RecyclerView.Adapter<ProhibitAdapter.ViewHolder> {

        private final Context mContext;
        private final List<Prohibit> mProhibitList;

        ProhibitAdapter(Context context, List<Prohibit> prohibitList) {
            this.mContext = context;
            this.mProhibitList = prohibitList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_prohibit, parent, false
            );
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Prohibit prohibit = mProhibitList.get(position);
            holder.mProhibit = prohibit;
            String title = String.format(
                    Locale.getDefault(),
                    "%d. %s",
                    position + 1,
                    prohibit.title
            );
            holder.mTitleTextView.setText(title);
            holder.mDetailsTextView.setText(prohibit.details);
            holder.mImageView.setImageResource(prohibit.imageRes);
        }

        @Override
        public int getItemCount() {
            return mProhibitList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final View mRootView;
            private final TextView mTitleTextView;
            private final TextView mDetailsTextView;
            private final ImageView mImageView;

            private Prohibit mProhibit;

            ViewHolder(View itemView) {
                super(itemView);

                mRootView = itemView;
                mTitleTextView = itemView.findViewById(R.id.title_text_view);
                mDetailsTextView = itemView.findViewById(R.id.details_text_view);
                mImageView = itemView.findViewById(R.id.image_view);

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
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
