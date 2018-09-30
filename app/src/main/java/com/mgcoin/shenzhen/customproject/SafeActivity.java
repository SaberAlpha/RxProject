package com.mgcoin.shenzhen.customproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class SafeActivity extends RxAppCompatActivity {

    @BindView(R.id.tv_show)
    TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        ButterKnife.bind(this);

        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(this::showTime);

    }

    private void showTime(Long aLong) {
        tvShow.setText("时间计时:"+aLong);
        Log.e("时间:",aLong.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("页面关闭","safe");
    }
}
