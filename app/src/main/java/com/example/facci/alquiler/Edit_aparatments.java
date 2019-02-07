package com.example.facci.alquiler;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.UUID;

public class Edit_aparatments extends AppCompatActivity {
    EditText mTitleEt,mDescrEt;
    ImageView mPostIv;
    Button mUploadBtn,mMarcar;
    String mDatabasePath = "Data";
    Uri mFilePathUri;
    private StorageReference mStorageRef;
    DatabaseReference mDatabaseReference;
    ProgressDialog mProgressDialog;
    FirebaseDatabase database;
    int IMAGE_REQUEST_CODE = 5;
    String cTitle,cDescr,cImage,cEstad;
    private Spinner mSelectDevice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_aparatments);
        check();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database= FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference(mDatabasePath);
        mProgressDialog = new ProgressDialog(Edit_aparatments.this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Editar Publicación");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTitleEt = findViewById(R.id.eTitleEt);
        mDescrEt = findViewById(R.id.eDescrEt);
        mPostIv = findViewById(R.id.eImageIv);
        mUploadBtn = findViewById(R.id.eUploadBtn);
        mSelectDevice = (Spinner) findViewById(R.id.spinner);



        Bundle intent = getIntent().getExtras();
        if (intent!=null){
            cTitle = intent.getString("cTitle");
            cDescr = intent.getString("cDescr");
            cImage = intent.getString("cImage");
            cEstad = intent.getString("estado");

            mTitleEt.setText(cTitle);
            mDescrEt.setText(cDescr);
            Picasso.get().load(cImage).into(mPostIv);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Edit_aparatments.this,
                    R.array.estado, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSelectDevice.setAdapter(adapter);
            int position = adapter.getPosition(cEstad);
            mSelectDevice.setSelection(position);
        }

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


                    begingUpdate();

                }catch (Exception e){

                    Toast.makeText(Edit_aparatments.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void begingUpdate() {
        mProgressDialog.setMessage("Editando...");
        mProgressDialog.show();
        deletePreviousImage();
    }

    private void deletePreviousImage() {

    StorageReference mpictureRef = FirebaseStorage.getInstance().getReferenceFromUrl(cImage);
    mpictureRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            //Toast.makeText(Edit_aparatments.this,"Imagen previa eliminada",Toast.LENGTH_LONG).show();
            UploadNewImage();
        }
    })
    .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(Edit_aparatments.this,e.getMessage(),Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }
    })
    ;
    }

    private void UploadNewImage() {

        Bitmap bitmap;
        bitmap = ((BitmapDrawable)mPostIv.getDrawable()).getBitmap();
        Uri uri = getImageUri(getApplicationContext(),bitmap);

        if (mFilePathUri!= null){
            StorageReference storagereference2 = mStorageRef.child("Imagenes/").child(mFilePathUri.getLastPathSegment());

            UploadTask uploadTask = storagereference2.putFile(mFilePathUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                   // Toast.makeText(Edit_aparatments.this,"nueva imagen subida",Toast.LENGTH_LONG).show();
                   Uri url =  taskSnapshot.getDownloadUrl();
                   String ima = url.toString();
                    updateDatabase(ima);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Edit_aparatments.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });
        }else{ if (uri!= null){


            StorageReference storagereference2 = mStorageRef.child("Imagenes/").child(uri.getLastPathSegment());

            UploadTask uploadTask = storagereference2.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // Toast.makeText(Edit_aparatments.this,"nueva imagen subida",Toast.LENGTH_LONG).show();
                    Uri url =  taskSnapshot.getDownloadUrl();
                    String ima = url.toString();
                    updateDatabase(ima);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Edit_aparatments.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });

        }}



    }

    private void updateDatabase(final String s) {

    final String title = mTitleEt.getText().toString();
    final String descr = mDescrEt.getText().toString();
    final String estad =mSelectDevice.getSelectedItem().toString();
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mFirebaseDatabase.getReference("Data");
    Query query = mRef.orderByChild("title").equalTo(cTitle);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot ds: dataSnapshot.getChildren()){
                ds.getRef().child("title").setValue(title);
                ds.getRef().child("search").setValue(title.toLowerCase());
                ds.getRef().child("description").setValue(descr);
                ds.getRef().child("image").setValue(s)  ;
                ds.getRef().child("estado").setValue(estad)  ;


            }
            Toast.makeText(Edit_aparatments.this, "Edición hecha", Toast.LENGTH_SHORT).show();
            Bundle extra = getIntent().getExtras();
            String use = extra.getString("nom");
            Intent intent = new Intent(Edit_aparatments.this,apartaments_user.class);
            intent.putExtra("nom",use);
            startActivity(intent);
            finish();
            mProgressDialog.dismiss();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
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
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(Edit_aparatments.this.getContentResolver(), inImage, UUID.randomUUID().toString() + ".png", "drawing");
        return Uri.parse(path);
    }

    private void check (){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Edit_aparatments.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Edit_aparatments.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Edit_aparatments.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1
                        );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
