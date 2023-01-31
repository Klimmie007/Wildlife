package com.example.todolist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.todolist.data.EntryDB;
import com.example.todolist.data.ImageDB;
import com.example.todolist.data.LoginRepository;
import com.example.todolist.data.MyCallback;
import com.example.todolist.data.SpeciesDB;
import com.example.todolist.data.model.Entry;
import com.example.todolist.data.model.SpeciesTag;
import com.example.todolist.data.model.User;
import com.example.todolist.ui.login.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class CreateEntryActivity extends AppCompatActivity {
    public static final String TOOK_PHOTO = "TOOK_PHOTO";
    public static final String SPECIES_TAG = "SPECIES_TAG";
    private static final int MAKE_PHOTO = 13;
    private static final int REQUEST_LOCATION_PERMISSION = 2137;

    private boolean tookPhoto = false;
    private Bitmap bmp;
    private ImageView imagePreview;
    private Spinner tagSpinner;
    private List<SpeciesTag> species;
    private SpeciesTag speciesChosen;
    private TextView locationText;
    private FusedLocationProviderClient client;
    private Location locationFound;
    private Button sendButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);
        if(!LoginRepository.isLoggedIn())
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if(savedInstanceState != null)
        {
            tookPhoto = savedInstanceState.getBoolean(TOOK_PHOTO);
            speciesChosen = (SpeciesTag) savedInstanceState.getSerializable(SPECIES_TAG);
            Log.d("species", speciesChosen.toString());
        }
        client = LocationServices.getFusedLocationProviderClient(this);
        imagePreview = findViewById(R.id.image_preview);
        if(!tookPhoto)
        {
            Intent intent = new Intent(this, TakePhotoActivity.class);
            startActivityForResult(intent, MAKE_PHOTO);
        }
        else
        {
            bmp = ImageDB.getLastTaken();
            if(bmp == null)
            {
                finish();
            }
            imagePreview.setImageBitmap(bmp);
        }
        tagSpinner = findViewById(R.id.speciesChoice);
        SpeciesDB.getSpecies(new MyCallback<List<SpeciesTag>>() {
            @Override
            public void Callback(List<SpeciesTag> tag) {
                species = tag;
                tagSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, species));
            }
        });

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                speciesChosen = species.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        locationText = findViewById(R.id.locationPreview);
        setLocationText();
        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageDB.sendToDB(new MyCallback<String>() {
                    @Override
                    public void Callback(String result) {
                        Entry entry = new Entry();
                        entry.imageID = result;
                        User user = new User();
                        user._id = LoginRepository.getUserID();
                        entry.user = user;
                        entry.species = speciesChosen;
                        entry.Latitude = locationFound.getLatitude();
                        entry.Longitude = locationFound.getLongitude();
                        EntryDB.InsertEntry(entry);
                        finish();
                    }
                });
            }
        });
    }

    public void setLocationText()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        else
        {
            client.getLastLocation().addOnSuccessListener(location -> {
                if(location != null)
                {
                    locationFound = location;
                    locationText.setText(getString(R.string.location_to_string, location.getLatitude(), location.getLongitude()));
                }
                else
                {
                    locationText.setText("Something went wrong");
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TOOK_PHOTO, tookPhoto);
        outState.putSerializable(SPECIES_TAG, speciesChosen);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MAKE_PHOTO) {
            if(resultCode == RESULT_OK){
                tookPhoto = true;
                bmp = ImageDB.getLastTaken();
                imagePreview.setImageBitmap(bmp);
            }
            else if(resultCode == RESULT_CANCELED)
            {
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case REQUEST_LOCATION_PERMISSION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    setLocationText();
                } else {
                    locationText.setText("PERMISSION NOT GRANTED");
                }
            }
            break;
        }
    }
}
