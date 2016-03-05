package com.github.pwittchen.swipedetector.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import com.github.pwittchen.swipedetector.library.SwipeDetector;
import com.github.pwittchen.swipedetector.library.SwipeEvent;
import pwittchen.com.swipedetector.R;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SwipeDetectorReactiveActivity extends AppCompatActivity {
  protected TextView info;
  private SwipeDetector swipeDetector;
  private Subscription subscription;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    info = (TextView) findViewById(R.id.info);
    swipeDetector = new SwipeDetector();
    subscription = swipeDetector.observeSwipes()
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<SwipeEvent>() {
          @Override public void call(SwipeEvent swipeEvent) {
            info.setText(swipeEvent.toString());
          }
        });
  }

  @Override public boolean dispatchTouchEvent(MotionEvent event) {
    swipeDetector.onTouchEvent(event);
    return super.dispatchTouchEvent(event);
  }

  @Override protected void onPause() {
    super.onPause();
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.listener:
        onBackPressed();
        break;
      case R.id.rx:
        break;
    }
    return true;
  }
}
