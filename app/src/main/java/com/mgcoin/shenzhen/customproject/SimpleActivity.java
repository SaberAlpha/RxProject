package com.mgcoin.shenzhen.customproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

public class SimpleActivity extends AppCompatActivity {

    @BindView(R.id.simple_tv)
    TextView simpleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        ButterKnife.bind(this);

        Observable.create(subscriber -> subscriber.onNext(sayMyname())).subscribe(s->simpleTv.setText((String)s));

        //观察事件
        Observable.OnSubscribe mObservable = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(sayMyname());
                subscriber.onCompleted();
            }
        };

        Subscriber<String> mSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                simpleTv.setText(s);
            }
        };


        Subscriber<String> mToastSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(SimpleActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };

        //分发订阅事件
        Observable<String> observable = Observable.create(mObservable);
        observable.subscribe(mSubscriber);
        observable.subscribe(mToastSubscriber);

    }

    //创建字符串
    private String sayMyname() {
        return "hello world";
    }
}
