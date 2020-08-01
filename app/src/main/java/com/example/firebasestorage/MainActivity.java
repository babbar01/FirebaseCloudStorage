package com.example.firebasestorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMG_REQUEST = 1;
    Button btnUpload, btnChoose, btnShow;
    ImageView ivSelected;
    Uri imageUri;
    ProgressBar mProgressBar;
    EditText etFileName;

    DatabaseReference mdatabaseref;
    StorageReference mstorageref;
    StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        btnShow = findViewById(R.id.btnShow);
        ivSelected = findViewById(R.id.ivSelected);
        mProgressBar=findViewById(R.id.mProgressBar);
        etFileName=findViewById(R.id.etFileName);

        mdatabaseref=FirebaseDatabase.getInstance().getReference("users");
        mstorageref=FirebaseStorage.getInstance().getReference("users");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMG_REQUEST);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri!=null)
                {
                    if(etFileName.getText().toString().trim().equals("")){
                        Toast.makeText(MainActivity.this, "choose Filename", Toast.LENGTH_SHORT).show();}
                    else if(mUploadTask!=null && mUploadTask.isInProgress())
                        Toast.makeText(MainActivity.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                    else
                        upload();



                }
                else
                    Toast.makeText(MainActivity.this, "File not selected!", Toast.LENGTH_SHORT).show();

            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RecyclerImages.class));
            }
        });


    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMG_REQUEST && data != null && data.getData() != null) {
            imageUri=data.getData();
            Picasso.get().load(imageUri).into(ivSelected);

        }
    }
    public void upload()
    {
        StorageReference fileref=mstorageref.child(System.currentTimeMillis()+getFileExtension(imageUri));

        mUploadTask=fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Model upload=new Model(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(),
                        etFileName.getText().toString().trim());

                mdatabaseref.push().setValue(upload);

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setProgress(0);
                        Toast.makeText(MainActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    }
                },500);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Upload failed!Try Again", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=taskSnapshot.getBytesTransferred()*100/taskSnapshot.getTotalByteCount();
                mProgressBar.setProgress((int)progress);


            }
        });
    }
}
