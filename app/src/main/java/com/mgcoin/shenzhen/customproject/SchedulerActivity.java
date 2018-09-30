package com.mgcoin.shenzhen.customproject;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulerActivity extends AppCompatActivity {

    @BindView(R.id.btn_mainThread)
    Button btnMainThread;
    @BindView(R.id.asyn)
    Button asyn;
    @BindView(R.id.rx)
    Button rx;
    @BindView(R.id.root_view)
    LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        ButterKnife.bind(this);

        //多个观察者顺序执行
        Observable.zip(Observable.create((subscriber -> {
            subscriber.onNext(longRunningOperation());
            Log.e(Thread.currentThread().getName(), "1");
            subscriber.onCompleted();
        })), Observable.create(subscriber -> {
            Integer num = 5;
            subscriber.onNext(num);
            Log.e(Thread.currentThread().getName(), "2");
            subscriber.onCompleted();
        }), (s, inter) -> (String) s + (Integer) inter).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(str -> Log.e("测试", str), e -> {
        });

        btnMainThread.setOnClickListener(v -> {
            btnMainThread.setEnabled(false);
            Observable.create(subscriber -> subscriber.onNext(longRunningOperation())
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                Observable.create(subscriber -> {
                    subscriber.onNext(longRunningOperation());
                    subscriber.onCompleted();
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s1 -> {
                    Snackbar.make(rootView, s1.toString(), Snackbar.LENGTH_LONG).show();
                }, e -> {
                }, () -> btnMainThread.setEnabled(true));
            });
//            longRunningOperation();
//            Snackbar.make(rootView, longRunningOperation(), Snackbar.LENGTH_SHORT).show();
//            btnMainThread.setEnabled(true);
        });


//        RxView.clicks(btnMainThread).asObservable().create(subscriber -> {
//            btnMainThread.setEnabled(false);
//            subscriber.onStart();
//            subscriber.onNext(longRunningOperation());
//            subscriber.onCompleted();
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
//            Snackbar.make(rootView, s.toString(), Snackbar.LENGTH_LONG).show();
//        }, e -> {
//        }, () -> {
//            btnMainThread.setEnabled(true);
//        });

    }

    private void start() {

    }

    private String longRunningOperation() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("e:", e.toString());
        }

        return "mainThread Completed! ";
    }


}
