package com.teratail.q248448;

import android.os.*;

class Lecture implements Parcelable {
  static final String INTENT_NAME = "Lecture";
  final String name;
  final String room;
  final int dayOfWeek; //0-5
  final int time; //0-5
  Lecture(int dayOfWeek, int time) {
    this("","", dayOfWeek, time);
  }
  Lecture(String name, String room, int dayOfWeek, int time) {
    this.name = name;
    this.room = room;
    this.dayOfWeek = dayOfWeek;
    this.time = time;
  }
  @Override
  public int describeContents() {
    return 0;
  }
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(room);
    dest.writeInt(dayOfWeek);
    dest.writeInt(time);
  }
  public static final Creator<Lecture> CREATOR = new Creator<Lecture>() {
    public Lecture createFromParcel(Parcel source) {
      return new Lecture(source);
    }
    public Lecture[] newArray(int size) {
      return new Lecture[size];
    }
  };
  //Creator<Lecture> 用
  private Lecture(Parcel source) {
    name = source.readString();
    room = source.readString();
    dayOfWeek = source.readInt();
    time = source.readInt();
  }
}