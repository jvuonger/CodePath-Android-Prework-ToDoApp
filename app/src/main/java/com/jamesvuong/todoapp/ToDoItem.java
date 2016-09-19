package com.jamesvuong.todoapp;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jvuonger on 9/17/16.
 */
public class ToDoItem {
    private int mToDoId;
    private String mToDoItem;
    private Date mDueDate;

    public ToDoItem() {
        mToDoItem = "";
    }

    // To Do Items with no Due Date
    public ToDoItem(String toDoItem) {
        mToDoItem = toDoItem;
    }

    // To Do Items with a Due Date
    public ToDoItem(String toDoItem, Date dueDate) {
        mToDoItem = toDoItem;
        mDueDate = dueDate;
    }

    public void setToDoId(int id) {
        mToDoId = id;
    }

    public void setToDoItem(String toDoItem) {
        mToDoItem = toDoItem;
    }

    public void setDueDate(Date dueDate) {
        mDueDate = dueDate;
    }

    public int getToDoId() {
        return mToDoId;
    }

    public String getToDoItem() {
        return mToDoItem;
    }

    public String getDueDate() {
        if( mDueDate == null ) {
            return "";
        } else {
            return mDueDate.toString();
        }
    }

    public long getDueDateTime() {
        if( mDueDate == null ) {
            return -1;
        } else {
            return mDueDate.getTime();
        }
    }

    public Calendar getDueDateForDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);

        if( mDueDate != null ) {
            cal.setTime(mDueDate);
        }

        return cal;
    }
}
