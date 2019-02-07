package com.example.facci.alquiler;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class Add_aparatments extends AppCompatActivity {
    EditText mTitleEt,mDescrEt;
    ImageView mPostIv;
    Button mUploadBtn;
    String mDatabasePath = "Data";
    Uri mFilePathUri;
    private StorageReference mStorageRef;
    DatabaseReference mDatabaseReference;
    ProgressDialog mProgressDialog;
    FirebaseDatabase database;
    int IMAGE_REQUEST_CODE = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aparatments);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        database= FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(Add_aparatments.this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Nueva Publicación");

        mTitleEt = findViewById(R.id.pTitleEt);
        mDescrEt = findViewById(R.id.pDescrEt);
        mPostIv = findViewById(R.id.pImageIv);
        mUploadBtn = findViewById(R.id.pUploadBtn);




        mPostIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),IMAGE_REQUEST_CODE);
            }
        });
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    uploadDataFirebase();

                }catch (Exception e){

                    Toast.makeText(Add_aparatments.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void uploadDataFirebase() {


            if (mFilePathUri != null && mTitleEt.getText().toString().isEmpty()==false && mDescrEt.getText().toString().isEmpty()==false) {
                mProgressDialog.setTitle("Subiendo...");
                mProgressDialog.show();
                StorageReference storageReferencee = mStorageRef.child("Imagenes/").child(mFilePathUri.getLastPathSegment());
                storageReferencee.putFile(mFilePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Bundle extra = getIntent().getExtras();
                                String mPostTitle = mTitleEt.getText().toString().trim();
                                String mPostDescr = mDescrEt.getText().toString().trim();
                                String mEstado = "Libre";
                               // String mPostUser = "miuuu";
                                String mPostUser = extra.getString("in");
                                String mPostTele = extra.getString("in2");
                                String mPostEmail = extra.getString("in3");
                                mProgressDialog.dismiss();
                                Toast.makeText(Add_aparatments.this, "Publicación hecha", Toast.LENGTH_SHORT).show();
                                ImageUploadInfo imageUploadInfo = new ImageUploadInfo(mPostTitle, mPostDescr,taskSnapshot.getDownloadUrl().toString(), mPostTitle.toLowerCase(),mPostUser,mPostTele,mPostEmail,mEstado);
                                String imageUploadID = mDatabaseReference.push().getKey();
                                mDatabaseReference.child(imageUploadID).setValue(imageUploadInfo);
                                mTitleEt.setText("");
                                mDescrEt.setText("");
                                mPostIv.setImageResource(R.drawable.attach_image);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgressDialog.dismiss();
                                Toast.makeText(Add_aparatments.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                mProgressDialog.setTitle("Subiendo...");
                            }
                        });

            }
            else {
                Toast.makeText(Add_aparatments.this,"Por favor,seleciona una imagen \n o rellena los campos",Toast.LENGTH_SHORT).show();

            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){
            mFilePathUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mFilePathUri);
                mPostIv.setImageBitmap(bitmap);
            }
            catch (Exception e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }
}
