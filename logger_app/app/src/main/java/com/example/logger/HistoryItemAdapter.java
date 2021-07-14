package com.example.logger;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
//import android.support.v
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryItemAdapter extends ArrayAdapter<History> {

    public HistoryItemAdapter(Context context, ArrayList<History> histories)
    {
//        super(context, nodes);
        super(context,0, histories);
    }


    //    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.history_list_item,parent,false);
        }
        History currentHistory = getItem(position);

        int selectedColor = 0;
        if (position % 7 == 0) {
            selectedColor = R.color.colorListItem1;
        }
        if (position % 7 == 1) {
            selectedColor = R.color.colorListItem2;
        }
        if (position % 7 == 2) {
            selectedColor = R.color.colorListItem3;
        }
        if (position % 7 == 3) {
            selectedColor = R.color.colorListItem4;
        }
        if (position % 7 == 4) {
            selectedColor = R.color.colorListItem5;
        }
        if (position % 7 == 5) {
            selectedColor = R.color.colorListItem6;
        }
        if (position % 7 == 6) {
            selectedColor = R.color.colorListItem7;
        }

        TextView historyId = listItemView.findViewById(R.id.historyListItem_id);
        historyId.setText(currentHistory.getId());

        TextView historyValue = listItemView.findViewById(R.id.historyListItem_value);
        historyValue.setText(currentHistory.getValue());

        String logger_id = currentHistory.getLoggerId();
        String logger_name = currentHistory.getLoggerName();

        TextView historyDatetime = listItemView.findViewById(R.id.historyListItem_datetime);
        historyDatetime.setText("Updated at " + currentHistory.getDatetime() + "\nBy " + logger_name + " (" + logger_id + ")" );

        LinearLayout nodelistParentLayout = listItemView.findViewById(R.id.historyItem_parentLayout);
        setRoundedDrawable(nodelistParentLayout,getContext().getResources().getColor(selectedColor));


        return listItemView;
    }

    //Function to create rounded rectangles
    public static void setRoundedDrawable(View view, int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(20f);
        shape.setColor(backgroundColor);
        view.setBackgroundDrawable(shape);
    }
}
