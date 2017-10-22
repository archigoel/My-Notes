package com.app.mynotes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DisplayFragment extends Fragment {


    private NotesAdapter notesAdapter;
    private ListView listView;
    TextView emptyNotes;
    Notes notes;
    static int size =0;
    TextView viewText;
    ImageView share;
    static List<Notes> notesList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        System.out.println("DISPLAY FRAGMENT");
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        listView = (ListView) view.findViewById(R.id.list_notes);
        emptyNotes = (TextView)view.findViewById(R.id.empty_notes_text);
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_view);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_display);
        final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        listUpdated();
        registerForContextMenu(listView);
        notesAdapter = new NotesAdapter(getActivity(), notesList);
        share = (ImageView)dialog.findViewById(R.id.shareit);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                final Notes notes = notesList.get(position);
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_view);
                viewText = (TextView)dialog.findViewById(R.id.view_Text);
                share = (ImageView)dialog.findViewById(R.id.shareit);


                share.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Notes");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, notes.getTitle());
                        Log.i("Sharing text listview", notes.getTitle());
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));

                    }
                });
                viewText.setText(notes.getTitle());
                Log.i("Display text listview", notes.getTitle());
                dialog.show();


            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 viewPager.setCurrentItem(0);
            }
        });

        return view;
    }


    public void listUpdated(){

        final SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        notesList.clear();
        System.out.println("LIST SIZE" + notesList.size());
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            notes = new Notes();
            notes.title = entry.getValue().toString();
            notes.key = entry.getKey();
            size = allEntries.size();
            notesList.add(notes);
        }
        if(notesList.size() == 0){

            emptyNotes.setVisibility(View.VISIBLE);
        }
        else{
            emptyNotes.setVisibility(View.INVISIBLE);
        }

        for(int i = 0; i< notesList.size();i++)
        System.out.println("LIST VALUES" + notesList.get(i));
        listView.setAdapter(notesAdapter);


    }


  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
      super.onCreateContextMenu(menu, v, menuInfo);

      System.out.println("LIST CLICKED");
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "View");
    }

    public boolean onContextItemSelected(MenuItem item)
    {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        if(item.getTitle()=="Edit") {

            final Notes notes = notesList.get(position);
            AddNotesFragment fragment = new AddNotesFragment();
            ((TabActivity) getActivity()).updateText(notes.getTitle());
            viewPager.setCurrentItem(0);
            fragment.getKey(notes.getKey());
        }

        if(item.getTitle()=="Delete"){
            final SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Notes notes = notesList.get(position);
            editor.remove(notes.getKey());
            editor.commit();
            listUpdated();
            viewPager.setCurrentItem(1);
        }

        if(item.getTitle() == "View"){
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_view);
            viewText = (TextView)dialog.findViewById(R.id.view_Text);
            share = (ImageView)dialog.findViewById(R.id.shareit);
            final Notes notes = notesList.get(position);

            share.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Notes");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, notes.getTitle());
                    Log.i("Sharing text ", notes.getTitle());
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });
            viewText.setText(notes.getTitle());
            Log.i("Display text view", notes.getTitle());
            dialog.show();

        }
        
        return true;
    }







    }
