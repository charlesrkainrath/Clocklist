package com.clocklist.kainrath.clocklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class AddAlarmActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;

    private int hour;
    private int minute;
    private int dayOfMonth;
    private int month;
    private int year;

    private ArrayList<String> todoList = new ArrayList<>();
    private ListView list;
    private TodoAdaptor listAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        list = (ListView) findViewById(R.id.todoList);
        listAdaptor = new TodoAdaptor(this, R.layout.todo_list_item, todoList);
        list.setAdapter(listAdaptor);
        registerOnClick();

        viewFlipper = (ViewFlipper) findViewById(R.id.alarmFlipper);
    }

    private void registerOnClick() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view_clicked, final int position, long id) {
                // TODO: EDIT/DELETE/DO NOTHING TO ITEM
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAlarmActivity.this);
                builder.setTitle("Eat a Dick");
                final EditText input = new EditText(AddAlarmActivity.this);
                input.setText(todoList.get(position), TextView.BufferType.EDITABLE);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!todoList.contains(input.getText().toString().trim())) {
                            todoList.set(position, input.getText().toString().trim());
                            listAdaptor.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todoList.remove(position);
                        listAdaptor.notifyDataSetChanged();
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    public void onTimePickNext(View v) {
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        hour = timePicker.getHour();
        minute = timePicker.getMinute();
        viewFlipper.showNext();
    }

    public void onDatePickNext(View v) {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        dayOfMonth = datePicker.getDayOfMonth();
        month = datePicker.getMonth();
        year = datePicker.getYear();

        // TODO: Check if date is valid (after current time)

        viewFlipper.showNext();
    }

    public void onDatePickBack(View v) {
        viewFlipper.showPrevious();
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);
    }

    public void onListBack(View v) {
        viewFlipper.showPrevious();
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.updateDate(year, month, dayOfMonth);
    }

    public void onCancelAddAlarm(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addListItem(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add to list");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!todoList.contains(input.getText().toString().trim())) {
                    todoList.add(input.getText().toString().trim());
                    listAdaptor.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    class TodoAdaptor extends ArrayAdapter<String> {

        List<String> todoList;

        private LayoutInflater layoutInflater = null;

        public TodoAdaptor(Context context, int textResId, ArrayList<String> list) {
            super(context, textResId, list);
            this.todoList = list;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return todoList.size();
        }

        @Override
        public String getItem(int position) {
            return todoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            if (rowView == null) {
                rowView = layoutInflater.inflate(R.layout.todo_list_item, null);
            }

            TextView item =(TextView) rowView.findViewById(R.id.list_item);
            item.setText(todoList.get(position));
            ImageView icon = (ImageView) rowView.findViewById(R.id.list_image);
            icon.setImageResource(android.R.drawable.ic_menu_edit);
            return rowView;
        }
    }
}