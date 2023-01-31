package com.example.todolist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.data.EntryDB;
import com.example.todolist.data.ImageDB;
import com.example.todolist.data.LoginDataSource;
import com.example.todolist.data.MyCallback;
import com.example.todolist.data.SpeciesDB;
import com.example.todolist.data.model.Entry;
import com.example.todolist.data.model.SpeciesTag;
import com.example.todolist.data.model.User;

import java.util.List;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private EntryAdapter adapter = null;
    private List<Entry> entries;
    public static final String KEY_EXTRA_ENTRY_ID = "KEY_EXTRA_ENTRY_ID";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.new_task:
                Intent intent = new Intent(getActivity(), CreateEntryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView(){
        EntryDB.fetchEntries(new MyCallback<List<Entry>>() {
            @Override
            public void Callback(List<Entry> result) {
                Log.d("TASKLISTFRAGMENT:91", "Entries found: "+ result.size());
                entries = result;
            }
        });

        if(adapter == null)
        {
            if(entries != null)
            {
                adapter = new EntryAdapter(entries);
                recyclerView.setAdapter(adapter);
            }
        }
        else
            adapter.notifyDataSetChanged();
    }
    private class EntryHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Entry entry;
        private TextView userText, speciesText, locationText;
        private ImageView thePhoto;
        private int position;
        public EntryHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_entry, parent, false));
            itemView.setOnClickListener(this);

            userText = itemView.findViewById(R.id.creatorName);
            speciesText = itemView.findViewById(R.id.speciesName);
            locationText = itemView.findViewById(R.id.locationText);
            thePhoto = itemView.findViewById(R.id.PhotoView);
        }

        public void bind(Entry entry, int bindedPosition){
            if(entry == null)
                Log.v("entry", "Null entry");
            this.entry = entry;
            position = bindedPosition;
            if(entry.user.username != null)
            {
                userText.setText(entry.user.username);
            }
            else
            {
                LoginDataSource.getUserByID(entry.user._id, new MyCallback<User>() {
                    @Override
                    public void Callback(User result) {
                        entry.user = result;
                        userText.setText(result.username);
                    }
                });
            }
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
            locationText.setText(getString(R.string.location_to_string, entry.Latitude, entry.Longitude));
            if(entry.image != null)
            {
                thePhoto.setImageBitmap(entry.image);
            }
            else
            {
                ImageDB.getImageById(entry.imageID, new MyCallback<Bitmap>() {
                    @Override
                    public void Callback(Bitmap result) {
                        entry.image = result;
                        thePhoto.setImageBitmap(result);
                    }
                });
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_ENTRY_ID, entry.entryID);
            startActivity(intent);
        }

        public int getBindingAdapterPosition() {
            return position;
        }

        //        public CheckBox getCheckBox() {
//            return checkBox;
//        }
    }
    private class EntryAdapter extends RecyclerView.Adapter<EntryHolder>
    {
        private List<Entry> entryList;

        public EntryAdapter(List<Entry> entries)
        {
            this.entryList = entries;
        }

        @NonNull
        @Override
        public EntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater= LayoutInflater.from(getActivity());
            return new EntryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EntryHolder holder, int position) {
            Entry entry = entryList.get(position);
            holder.bind(entry, position);
        }

        @Override
        public int getItemCount() {
            return entryList.size();
        }
    }
}
