package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Dashboard1 extends AppCompatActivity {
    CardView goToGroceries;
    CardView goToHomeMaintenance;
    CardView goToKitchenAppliances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard1);
        goToGroceries = findViewById(R.id.goToGroceryList);
        goToKitchenAppliances = findViewById(R.id.goToKitchenAppliances);
        goToHomeMaintenance = findViewById(R.id.goToHomeMaintenance);



        goToGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard1.this,"Opening Groceries Section", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Groceries_list.class));
            }
        });
        goToKitchenAppliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard1.this,"Opening Kitchen Appliances Section", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), KitchenApp.class));
            }
        });
        goToHomeMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard1.this,"Opening Home Maintenance Section", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomeMaintenance.class));
            }
        });


    }
    public void goToToDo(View view){
        Intent launchintent = getPackageManager().getLaunchIntentForPackage("net.penguincoders.doit");

        if(launchintent != null){
            startActivity(launchintent);
        }
        else{
            Toast.makeText(Dashboard1.this, "Package not available! ", Toast.LENGTH_SHORT).show();
        }
    }
    public void goToReminder(View view){
        Intent launchintent = getPackageManager().getLaunchIntentForPackage("com.example.reminderapp");

        if(launchintent != null){
            startActivity(launchintent);
        }
        else{
            Toast.makeText(Dashboard1.this, "Package not available! ", Toast.LENGTH_SHORT).show();
        }
    }
    public void goToWhatsapp(View view){
        Intent launchintent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");

        if(launchintent != null){
            startActivity(launchintent);
        }
        else{
            Toast.makeText(Dashboard1.this, "Package not available! ", Toast.LENGTH_SHORT).show();
        }
    }
}