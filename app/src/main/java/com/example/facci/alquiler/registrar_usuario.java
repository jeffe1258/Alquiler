package com.example.facci.alquiler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registrar_usuario extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    Uri mFilePathUri;
    private CircleImageView foto_perfil;
    EditText edtemail,edtpass,edttel,edtnom,reppass,pref;

    private StorageReference mStorageRef;
    Button btnres,btnat;
    private usuarios usua;
    boolean flag = false;
    FirebaseDatabase database;
    DatabaseReference ref;
    int IMAGE_REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        btnres = (Button) findViewById(R.id.regis_usu);
        edtemail = (EditText) findViewById(R.id.email_usu);
        edttel = (EditText) findViewById(R.id.tel_usu);
        edtpass = (EditText) findViewById(R.id.pass_usu);
        edtnom = (EditText) findViewById(R.id.edtnom);
        btnat = (Button) findViewById(R.id.btn_at);
        reppass = (EditText) findViewById(R.id.edtRep);
        foto_perfil = (CircleImageView) findViewById(R.id.edt_imgusu);
        pref = (EditText) findViewById(R.id.prefijo);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mProgressDialog = new ProgressDialog(registrar_usuario.this);


        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),IMAGE_REQUEST_CODE);
            }
        });
        usua = new usuarios();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Usuarios_alq");
        try{
            btnres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try{
                        checkUser();

                    }catch (Exception e){

                        Toast.makeText(registrar_usuario.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }


                }
            });
            //boton atrás
            btnat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent login = new Intent(registrar_usuario.this,ingresar_usuario.class);
                    finish();
                }
            });

        }catch (Exception e){
            Toast.makeText(registrar_usuario.this,"Ocurrio un error,ntente nuevamente",Toast.LENGTH_SHORT).show();

        }



    }



    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }
    /////////////////////////////////
    private void checkUser() {

            if (mFilePathUri != null && edtemail.getText().toString().isEmpty()== false && edtnom.getText().toString().isEmpty()== false && edttel.getText().toString().isEmpty()==false&& edtpass.getText().toString().isEmpty()==false&& reppass.getText().toString().isEmpty()==false ) {

            mProgressDialog.setTitle("Registrando...");
            mProgressDialog.show();
            StorageReference storageReferencee = mStorageRef.child("Imagenes/").child(mFilePathUri.getLastPathSegment());
            storageReferencee.putFile(mFilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference reference = database.getReference("Usuarios_alq");


                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (edtpass.getText().toString().length() >= 6 ){
                                        if (edtpass.getText().toString().equals(reppass.getText().toString())){

                                            if(!dataSnapshot.child(edtnom.getText().toString()).exists()){

                                                String mEmail = edtemail.getText().toString().trim();
                                                String mTele = pref.getText().toString().trim() +edttel.getText().toString().trim();
                                                String mPass = edtpass.getText().toString().trim();
                                                String mNombre = edtnom.getText().toString().trim();

                                                mProgressDialog.dismiss();
                                                Toast.makeText(registrar_usuario.this,"Registro Correcto", Toast.LENGTH_SHORT).show();
                                                usuarios imageUploadInfo = new usuarios(mEmail,mNombre, mTele,mPass,taskSnapshot.getDownloadUrl().toString());

                                                ref.child(edtnom.getText().toString()).setValue(imageUploadInfo);
                                                Intent regis = new Intent(registrar_usuario.this,ingresar_usuario.class);
                                                startActivity(regis);
                                                finish();
                                            }else{
                                                mProgressDialog.dismiss();
                                                Toast.makeText(registrar_usuario.this,"Usuario ya existe",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        else{
                                            mProgressDialog.dismiss();
                                            Toast.makeText(registrar_usuario.this,"Contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        mProgressDialog.dismiss();
                                        Toast.makeText(registrar_usuario.this,"Contraseña debe tener mínimo 6 caracteres",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(registrar_usuario.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.setTitle("Registrando...");
                        }
                    });

        }
        else {
            Toast.makeText(registrar_usuario.this,"Por favor,seleciona una imagen \n o rellena los campos",Toast.LENGTH_SHORT).show();

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
                foto_perfil.setImageBitmap(bitmap);
            }
            catch (Exception e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }

}
