package com.example.facci.alquiler;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class apartaments_user extends AppCompatActivity {
    LinearLayoutManager mLayaoutManager;
    SharedPreferences mSharedPref;
    private RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    TextView nom;
    DatabaseReference mref;
    CircleImageView profile_ima;

    FirebaseRecyclerAdapter<Model,ViewHolder2> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Model> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_publicacion);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mis post");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSharedPref = getSharedPreferences("SortSettings",MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort","newest");
        if (mSorting.equals("newest")){
            mLayaoutManager = new LinearLayoutManager(this);

            mLayaoutManager.setReverseLayout(true);
            mLayaoutManager.setStackFromEnd(true);
        }
        else if (mSorting.equals("oldest")){
            mLayaoutManager = new LinearLayoutManager(this);

            mLayaoutManager.setReverseLayout(false);
            mLayaoutManager.setStackFromEnd(false);

        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference("Data");

       // recibir();
        showData();

    }





    private void showDeleteDataDialog(final String currentTitle, final String currentImage) {

    AlertDialog.Builder builder = new AlertDialog.Builder(apartaments_user.this);
    builder.setTitle("Eliminar");
    builder.setIcon(R.drawable.ic_action_delete);
    builder.setMessage("¿Desea eliminar este post?");
    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        Query mQuery = mref.orderByChild("title").equalTo(currentTitle);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ds.getRef().removeValue();
                }
                Toast.makeText(apartaments_user.this,"Post Eliminado",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(apartaments_user.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
           StorageReference mPictureRefe = FirebaseStorage.getInstance().getReferenceFromUrl(currentImage);
            mPictureRefe.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(apartaments_user.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }
    });
    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });
        builder.create().show();
    }

    private void showData () {
        Bundle extras = getIntent().getExtras();


            String user = extras.getString("nom");



        final Query mQueryy = mref.orderByChild("usuario").equalTo(user);
        mQueryy.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(mQueryy,Model.class).build();


                    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder2>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ViewHolder2 holder, int position, @NonNull Model model) {

                            holder.setDetails(getApplication(),model.getTitle(),model.getDescription(),model.getImage(),model.getUsuario(),model.getTelefono(),model.getEstado());

                        }

                        @NonNull
                        @Override
                        public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mis_post,parent,false);
                            ViewHolder2 viewHOlder = new ViewHolder2(itemView);
                            viewHOlder.setOnclickListener(new ViewHolder2.ClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {



                                }

                                @Override
                                public void onItemlongClick(View view, int position) {

                                    final String cTitle = getItem(position).getTitle();
                                    final String cDescr = getItem(position).getDescription();
                                    final String cImage = getItem(position).getImage();
                                    final String cUser = getItem(position).getUsuario();
                                    final String  cEsta = getItem(position).getEstado();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(apartaments_user.this);
                                    String [] options = {"Editar","Eliminar"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which==0){
                                                Intent intent  = new Intent(apartaments_user.this,Edit_aparatments.class);
                                                intent.putExtra("cTitle",cTitle);
                                                intent.putExtra("cDescr",cDescr);
                                                intent.putExtra("cImage",cImage);
                                                intent.putExtra("nom",cUser);
                                                intent.putExtra("estado",cEsta);
                                                startActivity(intent);
                                                finish();



                                            }
                                            if (which==1){
                                                showDeleteDataDialog(cTitle,cImage);
                                            }

                                        }
                                    });
                                    builder.create().show();




                                }
                            });
                            return viewHOlder;
                        }
                    };

                    mRecyclerView.setLayoutManager(mLayaoutManager);
                    firebaseRecyclerAdapter.startListening();
                    mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    @Override
    protected  void onStart(){
        super.onStart();
        if (firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
