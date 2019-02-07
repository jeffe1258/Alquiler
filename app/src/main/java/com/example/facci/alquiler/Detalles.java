package com.example.facci.alquiler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import okhttp3.internal.Util;

public class Detalles extends AppCompatActivity {
    int REQUEST_PHONE_CALL = 1;
    TextView mTitleTv,mDetailTv,mUsu,mEsta;
    ImageView mImageTv;
    FloatingActionButton ws,call;
    private AlertDialog _dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detalles");
        actionBar.setDisplayHomeAsUpEnabled(true);


      mTitleTv = findViewById(R.id.titleTv);
      mDetailTv = findViewById(R.id.descriptionTv);
      mImageTv = findViewById(R.id.imageView);
      mEsta = findViewById(R.id.mEstado);
      ws = (FloatingActionButton) findViewById(R.id.btn_cont);

      call = (FloatingActionButton) findViewById(R.id.btn_llamar);
      mUsu = findViewById(R.id.userTv);


             String image = getIntent().getStringExtra("Image");
             String title = getIntent().getStringExtra("Título");
             String usuu = getIntent().getStringExtra("Usuario");
             String desc = getIntent().getStringExtra("Descripción");
            final  String tel = getIntent().getStringExtra("Telefono");
            final String ema = getIntent().getStringExtra("Email");
            final String ema2 = getIntent().getStringExtra("Email2");
            String esta = getIntent().getStringExtra("Estado");

            final  String tel2 = getIntent().getStringExtra("Telefono2");

        final char[] aCaracteres = tel.toCharArray();
        //Toast.makeText(Detalles.this,"email: "+ema,Toast.LENGTH_SHORT).show();
        //Toast.makeText(Detalles.this,"email2: "+ema2,Toast.LENGTH_SHORT).show();
            mTitleTv.setText(title);
            mDetailTv.setText(desc);
            mUsu.setText(usuu);
            mEsta.setText(esta);
        Picasso.get().load(image).into(mImageTv);
        check();
      /*  ws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent("android.intent.action.Detalles");
                    intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    intent.putExtra("jid", tel + "@s.whatsapp.net");
                    intent.putExtra("displayname", "+" + tel);
                    startActivity(intent);

                }catch (Exception e){
                    Toast.makeText(Detalles.this,"No tiene instalado Whatsapp",Toast.LENGTH_SHORT).show();
                }

            }
        });*/

      ws.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              final AlertDialog.Builder builder = new AlertDialog.Builder(Detalles.this);

              LayoutInflater inflater = getLayoutInflater();

              final View dialoglayout = inflater.inflate(R.layout.email, null);
              builder.setView(dialoglayout);

              final EditText etAsunto = (EditText) dialoglayout.findViewById(R.id.ettele);
              final TextView etCorreo = (TextView) dialoglayout.findViewById(R.id.etcorreo);
              final TextView etMensaje = (TextView) dialoglayout.findViewById(R.id.etmensaje);
              Button btnEnviarMail = (Button) dialoglayout.findViewById(R.id.btenvia);
              etCorreo.setText(ema2);
              etAsunto.setText(tel2);
              btnEnviarMail.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      try {

                          String to = etCorreo.getText().toString().trim();

                          String subject2 = "Alquiler de propiedad";
                          String subject = etAsunto.getText().toString();
                          String message = etMensaje.getText().toString();

                              new SimpleMail().sendEmail(ema, subject2, message+"\n Correo: "+ema2+" \n Teléfono:"+subject);
                              Toast.makeText(Detalles.this,"Mensaje enviado",Toast.LENGTH_LONG).show();
                              CloseDialog();
                      } catch (Exception e) {
                          e.printStackTrace();

                      }
                  }
              });


              _dialog = builder.show();


          }
      });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(newPhoneCallIntent(tel));


            }
        });



    }




    private void check (){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Detalles.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Detalles.this,
                    Manifest.permission.CALL_PHONE)) {


            } else {


                ActivityCompat.requestPermissions(Detalles.this,
                        new String[]{Manifest.permission.CALL_PHONE},1
                );

            }
        }


    }
    public static Intent newPhoneCallIntent(String phoneNumber){
        Intent callintent = new Intent(Intent.ACTION_DIAL);
        callintent.setData(Uri.parse("tel:"+phoneNumber));
        return callintent;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void CloseDialog()
    {
        if(_dialog != null) _dialog.dismiss();
    }
}
