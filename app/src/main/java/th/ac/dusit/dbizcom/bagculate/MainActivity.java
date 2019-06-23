package th.ac.dusit.dbizcom.bagculate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {

    private SliderLayout mSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupImageSlider();

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupImageSlider() {
        mSlider = findViewById(R.id.image_slider);

        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add("http://5911011802057.msci.dusit.ac.th/bagculate/images/image_slide_01.jpg");
        imageUrlList.add("http://5911011802057.msci.dusit.ac.th/bagculate/images/image_slide_02.jpg");
        imageUrlList.add("http://5911011802057.msci.dusit.ac.th/bagculate/images/image_slide_03.jpg");
        imageUrlList.add("http://5911011802057.msci.dusit.ac.th/bagculate/images/image_slide_04.jpg");

        /* ***********************************************************************/
        /* ใส่คำอธิบายรูปภาพตรงนี้ ****************************************************/
        /* ***********************************************************************/
        List<String> captionList = new ArrayList<>();
        captionList.add("คำอธิบายรูปภาพ 1");
        captionList.add("คำอธิบายรูปภาพ 2");
        captionList.add("คำอธิบายรูปภาพ 3");
        captionList.add("คำอธิบายรูปภาพ 4");

        RequestOptions requestOptions = new RequestOptions().fitCenter();

        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);

        for (int i = 0; i < imageUrlList.size(); i++) {
            TextSliderView sliderView = new TextSliderView(this);
            sliderView
                    .image(imageUrlList.get(i))
                    .description(captionList.get(i))
                    .setRequestOption(requestOptions)
                    //.setBackgroundColor(Color.WHITE)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            //sliderView.getBundle().putString("extra", listName.get(i));
            mSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mSlider.setPresetTransformer(SliderLayout.Transformer.Default);

        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(5000);
        mSlider.addOnPageChangeListener(this);
    }

    @Override
    protected void onStop() {
        mSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
