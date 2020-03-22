package com.teratail.q248448;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
  private static final String[] WEEKRY_HEADER = {"月","火","水","木","金","土"};
  private static final int TIME_COUNT = 6; //一日の最大講義数
  private Button[][] lectureSchedule;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button scheduleSelectButton = findViewById(R.id.scheduleSelectButton);
    scheduleSelectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = SubActivity2.createStartActivityIntent( MainActivity.this, WEEKRY_HEADER, TIME_COUNT);
        startActivityForResult(intent, 0);
      }
    });

    lectureSchedule = initScheduleGrid(WEEKRY_HEADER, TIME_COUNT);
  }
  private Button[][] initScheduleGrid(String[] columnLabels, int rowCount) {
    lectureSchedule = new Button[rowCount][columnLabels.length];

    GridLayout grid = findViewById(R.id.grid);
    grid.setColumnCount(1+columnLabels.length);
    grid.setRowCount(1+rowCount);

    //一行目(曜日)
    Space space = new Space(this); //左上角
    grid.addView(space, createLayoutParamsOfLabel());
    for(int j=0; j<columnLabels.length; j++) {
      TextView textView = new TextView(this);
      textView.setText(columnLabels[j]);
      textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
      grid.addView(textView, createLayoutParamsOfWeekday());
    }
    //二行目以降(時間割)
    for(int i=0; i<rowCount; i++) {
      TextView timeLabel = new TextView(this);
      timeLabel.setText(""+(i+1));
      grid.addView(timeLabel, createLayoutParamsOfLabel());
      for(int j=0; j<columnLabels.length; j++) {
        Button button = new Button(this);
        button.setText("");
        button.setTag(new Lecture(j, i));
        //button.setOnClickListener(clickListener);
        grid.addView(button, createLayoutParamsOfWeekday());
        lectureSchedule[i][j] = button;
      }
    }
    return lectureSchedule;
  }
  private GridLayout.LayoutParams createLayoutParamsOfLabel() {
    return createLayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, 0, GridLayout.CENTER);
  }
  private GridLayout.LayoutParams createLayoutParamsOfWeekday() {
    return createLayoutParams( 0, 1, GridLayout.FILL);
  }
  private GridLayout.LayoutParams createLayoutParams(int width, int columnSpecWeight, GridLayout.Alignment rowSpecAlignment) {
    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
    params.width = width;
    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, columnSpecWeight);
    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, rowSpecAlignment, 1);
    return params;
  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode, resultCode, data);
    //リクエストコードと結果コードをチェック
    if(requestCode == 0 && resultCode == Activity.RESULT_OK){
      Lecture lecture = data.getParcelableExtra(Lecture.INTENT_NAME);
      Button button = lectureSchedule[lecture.time][lecture.dayOfWeek];
      button.setText(lecture.name);
      button.setTag(lecture);
    }
  }
}