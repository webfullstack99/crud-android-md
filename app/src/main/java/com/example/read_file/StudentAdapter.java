package com.example.read_file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter {
    private Context context;
    private int  layout;
    private ArrayList<Student> studentArrayList;

    public StudentAdapter(Context context, int layout, ArrayList<Student> studentArrayList) {
        this.context = context;
        this.layout = layout;
        this.studentArrayList = studentArrayList;
    }

    @Override
    public int getCount() {
        return studentArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        TextView txtId;
        TextView txtName;
        TextView txtMajor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            holder.txtId  = (TextView) convertView.findViewById(R.id.txtId);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtMajor  = (TextView) convertView.findViewById(R.id.txtMajor);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Student std = studentArrayList.get(position);
        holder.txtId.setText(std.getId());
        holder.txtName.setText(std.getName());
        holder.txtMajor.setText(std.getMajor());

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.listview_item_animate);
        convertView.startAnimation(animation);

        return convertView;
    }
}
