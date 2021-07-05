package com.example.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddNewItem extends AppCompatActivity {
    ImageButton addImagebtn;
    EditText addName,addQuantity, addDesc;
    TextView  addCategory;
    Button Addbtn;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri image_uri;

    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        addName = findViewById(R.id.NameofProduct);
        addDesc = findViewById(R.id.DescOfproduct);
        addQuantity = findViewById(R.id.Quantity);
        addCategory = findViewById(R.id.Category);
        Addbtn = findViewById(R.id.AddBtn);
        addImagebtn = findViewById(R.id.addImagebtn);

        fAuth = FirebaseAuth.getInstance();

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        addImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog();
            }
        });

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input, validate, add data
                inputData();
            }
        });

    }
    private String itemTitle, itemDescription, itemCategory_var, itemQuantity;
    private void inputData(){
        //input
        itemTitle = addName.getText().toString().trim();
        itemDescription = addDesc.getText().toString().trim();
        itemCategory_var = addCategory.getText().toString().trim();
        itemQuantity = addQuantity.getText().toString().trim();

        //validate
        if (TextUtils.isEmpty(itemTitle)) {
            Toast.makeText(this,"Item Name is required!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(itemDescription)) {
            Toast.makeText(this,"Item description is required!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(itemCategory_var)) {
            Toast.makeText(this,"Item Category is required!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(itemQuantity)) {
            Toast.makeText(this,"Item Quantity is required!", Toast.LENGTH_SHORT).show();
        }

        addItem();
    }

    private void addItem(){
        //add item to database
        String timestamp = ""+System.currentTimeMillis();

        if(image_uri == null){
            HashMap<String, Object> hashmap = new HashMap<>();
            hashmap.put("itemID", ""+timestamp);
            hashmap.put("itemName", ""+itemTitle);
            hashmap.put("itemDescription", ""+itemDescription);
            hashmap.put("itemCategory", ""+itemCategory_var);
            hashmap.put("itemQuantity", ""+itemQuantity);
            hashmap.put("itemImage", "");
            hashmap.put("timestamp", ""+timestamp);
            hashmap.put("Userid", ""+fAuth.getUid());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(fAuth.getUid()).child("Items").child(timestamp).setValue(hashmap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddNewItem.this, "Item added.", Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    });
        }
        else{
            String filePathAndName = "Item_images/"+""+timestamp;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful());
                    Uri downloadImageUri = uriTask.getResult();

                    if(uriTask.isSuccessful()){
                        HashMap<String, Object> hashmap = new HashMap<>();
                        hashmap.put("itemID", ""+timestamp);
                        hashmap.put("itemName", ""+itemTitle);
                        hashmap.put("itemDescription", ""+itemDescription);
                        hashmap.put("itemCategory", ""+itemCategory_var);
                        hashmap.put("itemQuantity", ""+itemQuantity);
                        hashmap.put("itemImage", ""+downloadImageUri);
                        hashmap.put("timestamp", ""+timestamp);
                        hashmap.put("Userid", ""+fAuth.getUid());

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(fAuth.getUid()).child("Items").child(timestamp).setValue(hashmap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AddNewItem.this, "Item added.", Toast.LENGTH_SHORT).show();
                                        clearData();
                                    }
                                });

                    }
                }
            })
             .addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(AddNewItem.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                 }
             });

        }
    }

    private void clearData(){
        addName.setText("");
        addDesc.setText("");
        addQuantity.setText("");
        addCategory.setText("");
        addImagebtn.setImageResource(R.drawable.addtocart);
        image_uri = null;
    }

    private void categoryDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Item Category").setItems(itemCategoriesClass.itemCategories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = itemCategoriesClass.itemCategories[which];

                addCategory.setText(category);

            }
        })
         .show();
    }
    private void showImagePickDialog(){
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    //camera clicked
                    if (checkCameraPermission()){
                        pickFromCamera();
                    }
                    else{
                        requestCameraPermission();
                    }
                }
                else{
                    //gallery clicked
                    if(checkSToragePermission()){
                        pickFromGallery();
                    }
                    else{
                        requestStoragePermission();
                    }
                }
            }
        })
        .show();
    }


    private void pickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkSToragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //handling permission result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Camera and Storage permissions not provided.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Storage permissions not provided.", Toast.LENGTH_SHORT).show();
                    }
                }

                }
            }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //handling image picking
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //save picked image uri
                image_uri = data.getData();
                addImagebtn.setImageURI(image_uri);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                addImagebtn.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}