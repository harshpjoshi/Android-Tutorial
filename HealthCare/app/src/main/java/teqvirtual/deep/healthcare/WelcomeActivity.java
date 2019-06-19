package teqvirtual.deep.healthcare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Adapter.MpagerAdapter;
import Adapter.PrefManager;
import Adapter.PrefrenceManager;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager mPager;
    int[] layouts={R.layout.welcome_slide1,R.layout.welcome_slide2,R.layout.welcome_slide3};
    MpagerAdapter mpagerAdapter;

    LinearLayout dots_layout;
    ImageView[] dots;

    Button m_next,m_skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new PrefrenceManager(this).chechPrefrence())
        {
            loadHome();
        }

        if (Build.VERSION.SDK_INT>=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_welcome);

        getSupportActionBar().hide();

        mPager=(ViewPager)findViewById(R.id.view_pager);
        mpagerAdapter=new MpagerAdapter(layouts,this);
        mPager.setAdapter(mpagerAdapter);

        dots_layout=(LinearLayout)findViewById(R.id.layoutDots);

        m_next=(Button)findViewById(R.id.btn_next);
        m_skip=(Button)findViewById(R.id.btn_skip);

        m_next.setOnClickListener(this);
        m_skip.setOnClickListener(this);

        createDots(0);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                createDots(position);
                if (position==layouts.length-1)
                {
                    m_next.setText("Start");
                    m_skip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    m_next.setText("Next");
                    m_skip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    void createDots(int currentpos)
    {
        if (dots_layout!=null)
        {
            dots_layout.removeAllViews();
        }

        dots=new ImageView[layouts.length];

        for (int i=0;i<layouts.length;i++)
        {
            dots[i]=new ImageView(this);
            if (i==currentpos)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inactive_dots));
            }

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            dots_layout.addView(dots[i],params);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_next:
                loadNextSlide();
                break;

            case R.id.btn_skip:
                loadHome();
                new PrefrenceManager(this).writePreference();
                break;
        }
    }

    void loadHome()
    {
        startActivity(new Intent(this,SelectLogin.class));
        finish();
    }

    private void loadNextSlide()
    {
        int next_slide=mPager.getCurrentItem()+1;

        if (next_slide<layouts.length)
        {
            mPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new PrefrenceManager(this).writePreference();
        }
    }
}
