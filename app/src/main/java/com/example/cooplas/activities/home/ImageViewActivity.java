package com.example.cooplas.activities.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cooplas.R;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageViewActivity extends AppCompatActivity {
    ArrayList<String> listImages;
    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Intent intent = getIntent();
        listImages = intent.getStringArrayListExtra("listOfImages");
        position = intent.getStringExtra("position");

        ImageView back_img = findViewById(R.id.back_img);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewPager viewPager = findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(getApplicationContext());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.parseInt(position));

        ExtensiblePageIndicator extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
        extensiblePageIndicator.initViewPager(viewPager);


    }

    public class ImageAdapter extends PagerAdapter {
        Context context;

        LayoutInflater mLayoutInflater;

        ImageAdapter(Context context) {
            this.context = context;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return listImages.size();
        }

//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == ((LinearLayout) object);
//        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.page_viewer_pics, container, false);


            ImageView pic_img = itemView.findViewById(R.id.pic_img);
            Picasso.get().load(listImages.get(position)).fit().centerInside().into(pic_img);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}