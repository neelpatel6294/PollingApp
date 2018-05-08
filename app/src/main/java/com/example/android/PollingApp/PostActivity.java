package com.example.android.PollingApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import es.dmoral.toasty.Toasty;

public class PostActivity extends AppCompatActivity {
    // imports
    private ImageButton imageBtn1, imageBtn2;
    private static final int GALLERY_REQUEST_CODE1 = 2;
    private static final int GALLERY_REQUEST_CODE2 = 3;

    private Uri uri = null;
    private EditText textTitle;
    private EditText textDesc;
    private Button postBtn;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private ImageView mUserImage,image1,image2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        // initializing objects
        postBtn = (Button) findViewById(R.id.postBtn);
        textDesc = (EditText) findViewById(R.id.textDesc);
        textTitle = (EditText) findViewById(R.id.textTitle);




        storage = FirebaseStorage.getInstance().getReference();
        databaseRef = database.getInstance().getReference().child("Pojo");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        imageBtn1 = (ImageButton) findViewById(R.id.imageBtn1);

        //picking image from gallery
        imageBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE1);
            }
        });


        imageBtn2 = (ImageButton) findViewById(R.id.imageBtn2);
        //picking image from gallery
        imageBtn2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE2);
            }
        });


        // posting to Firebase
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.normal(PostActivity.this, "POSTING..", Toast.LENGTH_SHORT).show();
                //Toast.makeText(PostActivity.this, "POSTING...", Toast.LENGTH_LONG).show();
                final String PostTitle = textTitle.getText().toString().trim();
                final String PostDesc = textDesc.getText().toString().trim();


                // do a check for empty fields
                if (!TextUtils.isEmpty(PostDesc) && !TextUtils.isEmpty(PostTitle)) {
                    StorageReference filepath = storage.child("post_images").child(uri.getPath());
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            @SuppressWarnings("VisibleForTests")
                            //getting the post image download url
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            Toasty.success(PostActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();

                           // Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
                            final DatabaseReference newPost = databaseRef.push();


                            //adding post contents to database reference
                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    newPost.child("title").setValue(PostTitle);
                                    newPost.child("desc").setValue(PostDesc);
                                    newPost.child("imageUrl1").setValue(downloadUrl.toString());
                                    newPost.child("imageUrl2").setValue(downloadUrl.toString());
                                    newPost.child("uid").setValue(mCurrentUser.getUid());
                                    newPost.child("username").setValue(dataSnapshot.child("name").getValue())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        Intent intent = new Intent(PostActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    });
                }
            }
        });
    }


    @Override
    // image from gallery result
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE1 && resultCode == RESULT_OK) {
            uri = data.getData();
            imageBtn1.setImageURI(uri);
        }

        if (requestCode == GALLERY_REQUEST_CODE2 && resultCode == RESULT_OK) {
            uri = data.getData();
            imageBtn2.setImageURI(uri);
        }
    }

}


