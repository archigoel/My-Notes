package com.app.mynotes;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AddNotesFragment extends Fragment {

    private EditText txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    static int count = 1;
    static int img_count = 1;
    static String key;
    String text;
    private ViewInterface viewInterface;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_notes, container, false);
        txtSpeechInput = (EditText) view.findViewById(R.id.txtSpeechInput);
        txtSpeechInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text = txtSpeechInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                text = txtSpeechInput.getText().toString();
                SaveEditText saveEditText = (SaveEditText) getActivity();
                saveEditText.onTextChange(text);
            }
        });

        try {
            ViewInterface callback = (ViewInterface) getActivity();
            callback.onLinearLayoutCreated(view);
        } catch (ClassCastException e) {
            Log.e("ERROR", getActivity() + " must implement ViewInterface");
        }

        btnSpeak = (ImageButton)view.findViewById(R.id.btnSpeak);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        System.out.println("ON CREATE VIEW");
        final SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });




        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor editor = preferences.edit();
            @Override
            public void onClick(View view) {

                String text = txtSpeechInput.getText().toString();
                if (!text.equals("")) {


                    if (key != null) {

                        System.out.println("KEY" + key);
                        editor.putString(key, text);
                    } else {
                        editor.putString("note" + "" + count, text);
                        System.out.println("note" + "" + count);
                        System.out.println("ADD NOTE SIZE" + preferences.getAll().size());
                        count = count + 1;
                    }

                    editor.commit();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    Snackbar.make(view, Html.fromHtml("<font color=\"#ffff00\">Saved in My Notes</font>"), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                }


            }
        });


    return view;
    }



    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    System.out.println(result.get(0));

                    txtSpeechInput.append(result.get(0) + ".");
                    Log.e("SIZE OF result", String.valueOf(result.size()));
                }
                break;
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("ON PAUSE");
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("ON RESUME");
    }

    interface ViewInterface{
        public void onLinearLayoutCreated(View view);
    }
    interface SaveEditText{
        public void onTextChange(String text);
    }

    public String getKey(String k){
        key = k;
        System.out.println("KEY to edit" + key);
        return key;
    }

}
