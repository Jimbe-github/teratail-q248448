package com.teratail.q248448;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.text.*;
import android.view.View;
import android.widget.*;

public class SubActivity2 extends AppCompatActivity {
  public static final String INTENT_EXTRA_WEEKDAY = "Weekday";
  public static final String INTENT_EXTRA_TIMECOUNT = "TimeCount";
  private String[] weekday;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sub2);

    final EditText lectureName = findViewById(R.id.lectureName);
    final EditText lectureRoom = findViewById(R.id.lectureRoom);
    final NumberPicker timePicker = findViewById(R.id.timePicker);
    final Button okButton = findViewById(R.id.okButton);

    // name と room の入力状態によって ok の有効/無効を設定
    TextWatcher textWatcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        okButton.setEnabled(lectureName.length() > 0 && lectureRoom.length() > 0);
      }
      @Override
      public void afterTextChanged(Editable s) {}
    };

    lectureName.addTextChangedListener(textWatcher);
    lectureRoom.addTextChangedListener(textWatcher);

    okButton.setEnabled(false);
    okButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String name = lectureName.getText().toString();
        String room = lectureRoom.getText().toString();
        int dayOfWeek = timePicker.getValue() / weekday.length;
        int time = timePicker.getValue() % weekday.length;

        Intent data = new Intent();
        data.putExtra(Lecture.INTENT_NAME, new Lecture(name, room, dayOfWeek, time));
        setResult(Activity.RESULT_OK, data);
        finish();
      }
    });

    Intent data = getIntent(); //必須
    if(data == null) throw new IllegalArgumentException("必要な intent データがありません");

    weekday = data.getStringArrayExtra(INTENT_EXTRA_WEEKDAY);
    int timeCount = data.getIntExtra(INTENT_EXTRA_TIMECOUNT, 0);

    timePicker.setMinValue(0);
    timePicker.setMaxValue(weekday.length*timeCount-1);
    timePicker.setDisplayedValues(createDisplayedValues(weekday, timeCount));
    timePicker.setValue(0);
  }
  //ドラムロールに表示したい値を含んだ配列を作る
  private String[] createDisplayedValues(String[] weekday, int timeCount) {
    String[] displayedValues = new String[weekday.length * timeCount];
    for(int i=0, k=0; i<weekday.length; i++) {
      for(int j=1; j<=timeCount; j++, k++) displayedValues[k] = weekday[i] + j;
    }
    return displayedValues;
  }

  public static Intent createStartActivityIntent(Context packageContext, String[] weekday, int timeCount) {
    Intent intent = new Intent(packageContext, SubActivity2.class);
    intent.putExtra(INTENT_EXTRA_WEEKDAY, weekday);
    intent.putExtra(INTENT_EXTRA_TIMECOUNT, timeCount);
    return intent;
  }
}