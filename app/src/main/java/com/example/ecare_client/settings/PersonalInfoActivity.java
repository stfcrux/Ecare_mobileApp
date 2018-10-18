package com.example.ecare_client.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.Target;
import com.example.ecare_client.MainActivity;
import com.example.ecare_client.R;
import com.example.ecare_client.TitleLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.subinkrishna.widget.CircularImageView;

import java.io.IOException;
import java.util.UUID;


public class PersonalInfoActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button btnSaveInfo;
    private TextInputEditText inputPhone;
    private TextInputEditText inputName;
    private Button btnChoose, btnUpload;
    private CircularImageView imageView;

    private Uri filePath;
    private String picPath;

    private final int PICK_IMAGE_REQUEST = 71;
    private  UserInfo getUserForm(){
        return new UserInfo(inputPhone.getText().toString().trim(),inputName.getText().toString().trim(), picPath);

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private String uploadImage() {
        String path = null;
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            path = "images/"+ UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(path);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(PersonalInfoActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PersonalInfoActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
        return path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.personalinfo_title);
        titleLayout.setTitleText("Personal Info");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        //Initialize Views
        inputPhone = (TextInputEditText) findViewById(R.id.phone_input_et);
        inputName = (TextInputEditText) findViewById(R.id.full_name_et);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (CircularImageView) findViewById(R.id.profile_image);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final FirebaseUser currentUser = auth.getCurrentUser();

        final DatabaseReference userRef = database.getReference().child("Users").child(currentUser.getUid());
        picPath = "Null";
        Context context = this;
        DatabaseReference infoRef = userRef.child("Info");
        // Attach a listener to read the data at our posts reference
        infoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String contactEmail = "Null";
                    String phoneNo = "Null";

                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("email")) {
                            contactEmail = child.getValue(String.class);
                            inputName.setText(contactEmail);

                        }else if (child.getKey().equals("phone")) {
                            phoneNo = child.getValue(String.class);
                            inputPhone.setText(phoneNo);
                        }else if (child.getKey().equals("picPath")) {
                            picPath = child.getValue(String.class);
                            GlideApp.with(getApplicationContext())
                                    .load(storageReference.child(picPath))
                                    .override(96, Target.SIZE_ORIGINAL)
                                    .into(imageView);


                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing.
            }
        });




        btnSaveInfo = (Button) findViewById(R.id.btn_save);
        btnSaveInfo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo userinfo = getUserForm();

                        userRef.child("Info").setValue(userinfo);

                    }
                }
        );


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPath = uploadImage();
                // update user
            }
        });


    }

}