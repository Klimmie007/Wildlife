package com.example.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.data.EntryDB;
import com.example.todolist.data.ImageDB;
import com.example.todolist.data.LoginDataSource;
import com.example.todolist.data.LoginRepository;
import com.example.todolist.data.MyCallback;
import com.example.todolist.data.SpeciesDB;
import com.example.todolist.data.model.Entry;
import com.example.todolist.data.model.SpeciesTag;
import com.example.todolist.data.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class EntryFragment extends Fragment {
    private Entry entry;
    private TextView OPName;
    private TextView speciesText;
    private ImageView imageView;
    private TextView locationText;
    private Button commentsButton;
    public static final String ARG_TASK_ID = "ARG_TASK_ID";

    public static EntryFragment newInstance(String EntryID)
    {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TASK_ID, EntryID);
        EntryFragment entryFragment = new EntryFragment();
        entryFragment.setArguments(bundle);
        return entryFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String ID = getArguments().getString(ARG_TASK_ID);
        entry = EntryDB.getEntry(ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO finish rewriting this
        View view = inflater.inflate(R.layout.fragment_entry, container, false);
        if(entry == null)
        {
            return view;
        }
        OPName = view.findViewById(R.id.creatorNameComments);
        if(entry.user.username != null)
        {
            OPName.setText(entry.user.username);
        }
        else
        {
            LoginDataSource.getUserByID(entry.user._id, new MyCallback<User>() {
                @Override
                public void Callback(User result) {
                    entry.user = result;
                    OPName.setText(result.username);
                }
            });
        }
        speciesText = view.findViewById(R.id.speciesNameComments);
        if(entry.species.species != null)
        {
            speciesText.setText(entry.species.species);
        }
        else
        {
            SpeciesDB.getSpecies(entry.species._id, new MyCallback<SpeciesTag>() {
                @Override
                public void Callback(SpeciesTag result) {
                    entry.species = result;
                    speciesText.setText(result.species);
                }
            });
        }
        imageView = view.findViewById(R.id.PhotoViewComments);
        if(entry.image != null)
        {
            imageView.setImageBitmap(entry.image);
        }
        else
        {
            ImageDB.getImageById(entry.imageID, new MyCallback<Bitmap>() {
                @Override
                public void Callback(Bitmap result) {
                    entry.image = result;
                    imageView.setImageBitmap(result);
                }
            });
        }
        locationText = view.findViewById(R.id.locationTextComments);
        locationText.setText(getString(R.string.location_to_string, entry.Latitude, entry.Longitude));
        commentsButton = view.findViewById(R.id.comment_view_button);
        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CommentsActivity.class);
                intent.putExtra(CommentListFragment.KEY_COMMENT_ID_EXTRA, entry.entryID);
                startActivity(intent);
            }
        });
        return view;
    }
}
