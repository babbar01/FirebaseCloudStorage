package com.example.firebasestorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RecyclerImages extends AppCompatActivity {
    RecyclerView rvImages;
//    RecyclerView.Adapter Adapter;
    StorageReference mStorageRef;
    DatabaseReference mDataBaseRef;
    ArrayList<Model> listImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_images);

        rvImages=findViewById(R.id.rvImages);
        listImages= new ArrayList<>();
        mStorageRef= FirebaseStorage.getInstance().getReference("users");
        mDataBaseRef= FirebaseDatabase.getInstance().getReference("users");

        rvImages.setHasFixedSize(true);
        rvImages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final ImagesAdapter adapter=new ImagesAdapter(listImages,getApplicationContext());
        rvImages.setAdapter(adapter);



        mDataBaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot mdataSnapshot:dataSnapshot.getChildren())
                {
                    Model model=dataSnapshot.getValue(Model.class);
                    listImages.add(model);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RecyclerImages.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }
}
