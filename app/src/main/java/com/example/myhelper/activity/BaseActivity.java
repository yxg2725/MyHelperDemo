package com.example.myhelper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import com.example.myhelper.R;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/1/5.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        init();

        Log.e(TAG, this.getClass().getName() );
    }

    protected  void init(){

    }

    public void setToolbar(String title,boolean isShowBack){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isShowBack);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


    public abstract int getLayoutId();
}
