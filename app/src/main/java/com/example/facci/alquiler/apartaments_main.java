package com.example.facci.alquiler;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class apartaments_main extends AppCompatActivity {
    LinearLayoutManager mLayaoutManager;
    SharedPreferences mSharedPref;
    private RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    TextView nom;
    DatabaseReference mref;
    CircleImageView profile_ima;

    FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Model> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apartamentos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista");


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

        recibir();
        showData();

    }






    private void showData () {
        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(mref,Model.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Model model) {

                holder.setDetails(getApplication(),model.getTitle(),model.getDescription(),model.getImage(),model.getUsuario(),model.getTelefono(),model.getEstado());

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
                ViewHolder viewHOlder = new ViewHolder(itemView);
                viewHOlder.setOnclickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String mTitle = getItem(position).getTitle();
                        String mUSe = getItem(position).getUsuario();
                        String mDesc = getItem(position).getDescription();
                        String mImage = getItem(position).getImage();
                        String mTele = getItem(position).getTelefono();
                        String mEmail= getItem(position).getEmail();
                        String mEstado = getItem(position).getEstado();
                        Bundle extraaas = getIntent().getExtras();

                        String email2 = extraaas.getString("info4");
                        String teleusua_actual = extraaas.getString("info3");
                        Intent intent = new Intent(view.getContext(),Detalles.class);

                        intent.putExtra("Image",mImage);
                        intent.putExtra("Usuario",mUSe);
                        intent.putExtra("Título",mTitle);
                        intent.putExtra("Descripción",mDesc);
                        intent.putExtra("Telefono",mTele);
                        intent.putExtra("Email",mEmail);
                        intent.putExtra("Email2",email2);
                        intent.putExtra("Estado",mEstado);
                        intent.putExtra("Telefono2",teleusua_actual);
                        startActivity(intent);

                    }

                    @Override
                    public void onItemlongClick(View view, int position) {



                    }
                });
                return viewHOlder;
            }
        };

        mRecyclerView.setLayoutManager(mLayaoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseSearch(String textSearch) {


        Query firebaseSearchQuery = mref.orderByChild("search").startAt(textSearch.toLowerCase()).endAt(textSearch.toLowerCase() + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(firebaseSearchQuery,Model.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Model model) {
                holder.setDetails(getApplication(),model.getTitle(),model.getDescription(),model.getImage(),model.getUsuario(),model.getTelefono(),model.getEstado());

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
                ViewHolder viewHOlder = new ViewHolder(itemView);
                viewHOlder.setOnclickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String mTitle = getItem(position).getTitle();
                        String mUSe = getItem(position).getUsuario();
                        String mDesc = getItem(position).getDescription();
                        String mImage = getItem(position).getImage();
                        String mTele = getItem(position).getTelefono();
                        String mEmail= getItem(position).getEmail();
                        String mEstado = getItem(position).getEstado();
                        Bundle extraaas = getIntent().getExtras();

                        String email2 = extraaas.getString("info4");
                        Intent intent = new Intent(view.getContext(),Detalles.class);

                        intent.putExtra("Image",mImage);
                        intent.putExtra("Usuario",mUSe);
                        intent.putExtra("Título",mTitle);
                        intent.putExtra("Descripción",mDesc);
                        intent.putExtra("Telefono",mTele);
                        intent.putExtra("Email",mEmail);
                        intent.putExtra("Email2",email2);
                        intent.putExtra("Estado",mEstado);
                        startActivity(intent);

                    }

                    @Override
                    public void onItemlongClick(View view, int position) {


                    }
                });
                return viewHOlder;
            }
        };

        mRecyclerView.setLayoutManager(mLayaoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    protected  void onStart(){
        super.onStart();
        if (firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String query) {
        firebaseSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        firebaseSearch(s);
        return false;
    }
});
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort){
            showSortDialog();
            return true;
        }
        if (id == R.id.action_add){
            Bundle extraas = getIntent().getExtras();
            String d11 = extraas.getString("info3");
            String d12 = extraas.getString("info4");
            Intent inten = new Intent(apartaments_main.this,Add_aparatments.class);
            inten.putExtra("in",nom.getText().toString());
            inten.putExtra("in2",d11);
            inten.putExtra("in3",d12);
            startActivity(inten);
            return true;
        }
        if (id == R.id.action_salir){
            startActivity(new Intent(apartaments_main.this,ingresar_usuario.class));
            apartaments_main.this.finish();
            return true;
        }
        if (id == R.id.action_post){
            Bundle b =  getIntent().getExtras();
            String d = b.getString("info2");
            Intent post = new Intent(apartaments_main.this,apartaments_user.class);
            post.putExtra("nom",nom.getText().toString());
            post.putExtra("ima",d);
            startActivity(post);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        String[] sortOptions ={"Más reciente ","Más antiguo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ordenar")
                .setIcon(R.drawable.ic_action_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","newest");
                            editor.apply();
                            recreate();
                        }
                        else if (which == 1){{
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","oldest");
                            editor.apply();
                            recreate();

                        }}
                    }
                });
        builder.show();

    }
    private void recibir(){
        try{
            Bundle extras = getIntent().getExtras();
            String d1 = extras.getString("info");
            String d2 = extras.getString("info2");

            nom = (TextView) findViewById(R.id.nom_profile);
            nom.setText(d1);
            profile_ima = (CircleImageView) findViewById(R.id.profile_img);
            Glide.with(getApplicationContext()).load(d2).into(profile_ima);
        }catch (NullPointerException e){
            e.getMessage();
        }




    }
}
