package com.example.smearcamera.utils;

enum Direction {
  NONE,
  LEFT,
  TOP,
  RIGHT,
  BOTTOM;

  public static Direction get(int ordinal) {
    for (Direction direction :values() ){
      if (direction.ordinal() == ordinal) {
        return direction;
      }
    }
            return NONE;
  }

}