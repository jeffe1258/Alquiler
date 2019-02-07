package com.example.facci.alquiler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ingresar_usuario extends AppCompatActivity {

    Button btnregistrar,btningresar;
    EditText edtemail,edtpass;
    TextView nom_pro;
    private usuarios usu;
    FirebaseDatabase database;
    DatabaseReference ref;
    String pass;
    ProgressDialog mProgressDialog;
    CircleImageView profile_ima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnregistrar = (Button) findViewById(R.id.btn_registrar);
        btningresar = (Button) findViewById(R.id.btn_ingresar);
        edtemail = (EditText) findViewById(R.id.etemail);
        edtpass = (EditText) findViewById(R.id.etpass);
        //profile_ima = (CircleImageView) findViewById(R.id.profile_img);
        nom_pro = (TextView) findViewById(R.id.nom_profile);

        usu = new usuarios();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Usuarios_alq");
        mProgressDialog = new ProgressDialog(ingresar_usuario.this);

        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user =edtemail.getText().toString();
                 pass =edtpass.getText().toString();
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setTitle("Comprobando");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.show();
                try {
                    if (edtemail.getText().toString().isEmpty()|| edtpass.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Verifique que los campos no esten vacíos", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();

                    }else{
                        try{
                            ref.child(user).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    usuarios usu = dataSnapshot.getValue(usuarios.class);
                                    try{
                                    if (pass.equals(usu.getPass().toString())){

                                           Intent intent = new Intent(ingresar_usuario.this,apartaments_main.class);
                                           intent.putExtra("info",usu.getNombre().toString());
                                           intent.putExtra("info2",usu.getImagen().toString());
                                           intent.putExtra("info3",usu.getTelefono().toString());
                                           intent.putExtra("info4",usu.getEmail().toString());


                                           startActivity(intent);

                                            Toast.makeText(getApplicationContext(),"Ingreso Correcto", Toast.LENGTH_SHORT).show();
                                            mProgressDialog.dismiss();
                                            finish();



                                    }else{
                                        Toast.makeText(getApplicationContext(),"Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();

                                    }
                                    }catch (NullPointerException e){
                                        mProgressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Usuario no existe", Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }catch (NullPointerException e){
                            //mProgressDialog.dismiss();
                            e.printStackTrace();
                        }


                    }


                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();


                }
            }
        });



        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registro = new Intent(ingresar_usuario.this,registrar_usuario.class);
                startActivity(registro);
            }
        });




    }


}
