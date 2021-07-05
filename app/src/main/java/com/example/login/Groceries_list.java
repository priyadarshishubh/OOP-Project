package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;

import java.io.File;
import java.util.ArrayList;

public class Groceries_list extends AppCompatActivity {
    private ImageButton mAddnewItem,filterItemsBtn;
    private EditText SearchItemEt;
    private TextView filteredItemsTv;
    private RecyclerView itemsRv;
    private FirebaseAuth fAuth;
    private RelativeLayout productsRl;

    private ArrayList<ModelItems> itemList;
    private Adapteritem adapteritem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_list);
        mAddnewItem = findViewById(R.id.addNewItem);
        filterItemsBtn = findViewById(R.id.filterItemsBtn);
        productsRl = findViewById(R.id.productsRl);
        SearchItemEt = findViewById(R.id.SearchItemEt);
        filteredItemsTv = findViewById(R.id.filteredItemsTv);
        itemsRv = findViewById(R.id.itemsRv);
        fAuth = FirebaseAuth.getInstance();

        loadAllItems();

        SearchItemEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapteritem.getFilter().filter(s);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mAddnewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Groceries_list.this,"Opening add new item section", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AddNewItem.class));
            }
        });
    //show itemsUI
     productsRl.setVisibility(View.VISIBLE);

     filterItemsBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             AlertDialog.Builder builder = new AlertDialog.Builder(Groceries_list.this);
             builder.setTitle("Choose Category:").setItems(itemCategoriesClass.itemCategories1, new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                    String selected = itemCategoriesClass.itemCategories1[which];
                    filteredItemsTv.setText(selected);
                    if(selected.equals("All")){
                        loadAllItems();
                    }
                    else{
                        loadFilteredItems(selected);
                    }

                 }
             })
                     .show();

         }
     });
    }

    private void loadFilteredItems(String selected) {
        itemList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(fAuth.getUid()).child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){

                            String itemCategory = ""+ds.child("itemCategory").getValue();
                            if(selected.equals(itemCategory)){
                                ModelItems modelItems = ds.getValue(ModelItems.class);
                                itemList.add(modelItems);
                            }

                        }
                        adapteritem = new Adapteritem(Groceries_list.this, itemList);
                        itemsRv.setAdapter(adapteritem);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllItems() {
        itemList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(fAuth.getUid()).child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            ModelItems modelItems = ds.getValue(ModelItems.class);
                            itemList.add(modelItems);

                        }
                        adapteritem = new Adapteritem(Groceries_list.this, itemList);
                        itemsRv.setAdapter(adapteritem);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share){
            ApplicationInfo api = getApplicationContext().getApplicationInfo();
            String apkpath = api.sourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/vnd.android.package-archive");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
            startActivity(Intent.createChooser(intent, "ShareVia"));
        }
        return true;
    }
}