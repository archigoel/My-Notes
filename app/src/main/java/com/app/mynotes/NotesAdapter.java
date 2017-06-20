package com.app.mynotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anshulgupta on 6/9/17.
 */
public class NotesAdapter extends ArrayAdapter<Notes>{

    private Context context;
    private List<Notes> notesList = new ArrayList<Notes>();
//    private ArrayList<String> notesList = new ArrayList<String>();
    private LayoutInflater inflater = null;
    Notes notes;


    class NotesViewHolder{
        TextView textView;
    }

    public NotesAdapter(Context context, List<Notes> list) {
         super(context,R.layout.list_item);


        this.context = context;
        this.notesList = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public void add(Notes object) {
        notesList.add(object);

        super.add(object);
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.notesList.size();
    }

    @Override
    public Notes getItem(int index) {
        return this.notesList.get(index);
    }


    public View getView (int position, View convertView, ViewGroup parent){
        NotesViewHolder viewHolder = null;
        View view = convertView;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            view = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new NotesViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.notes);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (NotesViewHolder)view.getTag();

        }

        viewHolder.textView.setText(getItem(position).getTitle());


    return view;
    }



    }
