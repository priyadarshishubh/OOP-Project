package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class KitchenApp extends AppCompatActivity  {

    private EditText itemET;
    private Button Btn;
    private ListView itemsList;

    private ArrayList<String> KitchenItems;
    private ArrayAdapter<String> KitchenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_app);

        itemET = findViewById(R.id.item_edit_text);
        Btn = findViewById(R.id.addBtn);
        itemsList = findViewById(R.id.items_list);

        KitchenItems =  FileHelper.readData(this);

        KitchenAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, KitchenItems);
        itemsList.setAdapter(KitchenAdapter);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.addBtn:
                        String itemEntered = itemET.getText().toString();
                        KitchenAdapter.add(itemEntered);
                        itemET.setText("");

                        FileHelper.writeData(KitchenItems, KitchenApp.this);
                        Toast.makeText(KitchenApp.this,"Item Added", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

       itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               KitchenItems.remove(position);
               KitchenAdapter.notifyDataSetChanged();
               FileHelperHM.writeData(KitchenItems, KitchenApp.this);
               Toast.makeText(KitchenApp.this,"Delete", Toast.LENGTH_SHORT).show();
               
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