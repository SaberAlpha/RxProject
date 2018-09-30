package com.mgcoin.shenzhen.customproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends RxAppCompatActivity {

    @BindView(R.id.click_simple)
    Button clickSimple;
    @BindView(R.id.more_module)
    Button moreModule;
    @BindView(R.id.ed_text)
    EditText edText;
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.click_safe)
    Button clickSafe;
    @BindView(R.id.click_scheduler)
    Button clickScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RxTextView.textChanges(edText).subscribe(tvShow::setText);

        RxView.clicks(clickSimple).compose(bindToLifecycle()).subscribe(aVoid -> startActivity(new Intent(this, SimpleActivity.class)));

        RxView.clicks(clickSafe).compose(bindToLifecycle()).subscribe(aVoid -> startActivity(new Intent(this, SafeActivity.class)));

        RxView.clicks(clickScheduler).throttleFirst(5, TimeUnit.SECONDS).compose(bindToLifecycle()).subscribe(aVoid -> startActivity(new Intent(this, SchedulerActivity.class)));

    }

    private void jump(Void aVoid) {
        startActivity(new Intent(this, SimpleActivity.class));
    }


}
