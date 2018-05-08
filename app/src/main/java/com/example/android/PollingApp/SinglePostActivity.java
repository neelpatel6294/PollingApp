package com.example.android.PollingApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class SinglePostActivity extends AppCompatActivity {

    private ImageView singelImage1,singleImage2;
    private TextView singleTitle, singleDesc;
    String post_key = null;
    private DatabaseReference mDatabase;
    private Button deleteBtn;
    private FirebaseAuth mAuth;
    private ImageView image1,image2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        singelImage1 = (ImageView)findViewById(R.id.singleImageview1);
        singleImage2 = (ImageView)findViewById(R.id.singleImageview2);

        image1 = findViewById(R.id.checked_image1);
        image2 = findViewById(R.id.checked_image2);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(SinglePostActivity.this, "60!", Toast.LENGTH_SHORT, true).show();

            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(SinglePostActivity.this, "40!", Toast.LENGTH_SHORT, true).show();

            }
        });

        singleTitle = (TextView)findViewById(R.id.singleTitle);
        singleDesc = (TextView)findViewById(R.id.singleDesc);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Pojo");
        post_key = getIntent().getExtras().getString("PostID");
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        mAuth = FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();
                Toasty.success(SinglePostActivity.this, "Deleted Successfully!!", Toast.LENGTH_SHORT, true).show();
                Intent mainintent = new Intent(SinglePostActivity.this, MainActivity.class);
                startActivity(mainintent);
            }
        });


        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image1 = (String) dataSnapshot.child("imageUrl1").getValue();
                String post_image2 = (String) dataSnapshot.child("imageUrl2").getValue();

                String post_uid = (String) dataSnapshot.child("uid").getValue();

                singleTitle.setText(post_title);
                singleDesc.setText(post_desc);
                Picasso.with(SinglePostActivity.this).load(post_image1).into(singelImage1);
                if (mAuth.getCurrentUser().getUid().equals(post_uid)){

                    deleteBtn.setVisibility(View.VISIBLE);
                }
                Picasso.with(SinglePostActivity.this).load(post_image2).into(singleImage2);
                if (mAuth.getCurrentUser().getUid().equals(post_uid)){

                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
