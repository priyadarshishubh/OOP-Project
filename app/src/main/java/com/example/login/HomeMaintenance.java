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

public class HomeMaintenance extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText itemETHM;
    private Button BtnHM;
    private ListView itemsListHM;

    private ArrayList<String> HMItems;
    private ArrayAdapter<String> HMAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maintenance);

        itemETHM = findViewById(R.id.item_edit_text_HM);
        BtnHM = findViewById(R.id.addBtn_HM);
        itemsListHM = findViewById(R.id.items_list_HM);

        HMItems =  FileHelperHM.readData(this);

        HMAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, HMItems);
        itemsListHM.setAdapter(HMAdapter);

        BtnHM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.addBtn_HM:
                        String itemEnteredHM = itemETHM.getText().toString();
                        HMAdapter.add(itemEnteredHM);
                        itemETHM.setText("");

                        FileHelperHM.writeData(HMItems, HomeMaintenance.this);
                        Toast.makeText(HomeMaintenance.this,"Item Added", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        itemsListHM.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HMItems.remove(position);
        HMAdapter.notifyDataSetChanged();
        FileHelperHM.writeData(HMItems, this);
        Toast.makeText(this,"Delete", Toast.LENGTH_SHORT).show();
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