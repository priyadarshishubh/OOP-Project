package com.example.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapteritem  extends RecyclerView.Adapter<Adapteritem.Holderitem> implements Filterable {

    private Context context;
    public ArrayList<ModelItems> itemsList, filterList;
    private FilterItems filter;
    public Adapteritem(Context context, ArrayList<ModelItems> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public Holderitem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_view, parent, false);
        return new Holderitem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holderitem holder, int position) {
        ModelItems modelItem = itemsList.get(position);
        String id = modelItem.getItemID();
        String uid = modelItem.getUid();
        String itemCategory = modelItem.getItemCategory();
        String itemDesc = modelItem.getItemDescription();
        String itemName = modelItem.getItemName();
        String itemImage = modelItem.getItemImage();
        String itemQuantity = modelItem.getItemQuantity();
        String timestamp = modelItem.getTimestamp();

        holder.QuantityTv.setText(itemQuantity);
        holder.DescriptionTv.setText(itemDesc);
        //holder.ItemcategoryTv.setText(itemCategory);
        holder.ItemNameTv.setText(itemName);
        try{
            Picasso.get().load(itemImage).placeholder(R.drawable.addtocart).into(holder.itemIconTv);
        }
        catch (Exception e){
            holder.itemIconTv.setImageResource(R.drawable.addtocart);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsBottomSheet(modelItem);

            }
        });
        }

    private void detailsBottomSheet(ModelItems modelItem) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.bs_item_details, null);

        bottomSheetDialog.setContentView(view);

        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        ImageButton editBtn = view.findViewById(R.id.editBtn);
        ImageView itemIconIv = view.findViewById(R.id.itemIconTv);
        TextView titleTv = view.findViewById(R.id.titleTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        TextView categoryTv = view.findViewById(R.id.categoryTv);
        TextView quantityTv = view.findViewById(R.id.quantityTv);

        String id = modelItem.getItemID();
        String uid = modelItem.getUid();
        String itemCategory = modelItem.getItemCategory();
        String itemDesc = modelItem.getItemDescription();
        String itemName = modelItem.getItemName();
        String itemImage = modelItem.getItemImage();
        String itemQuantity = modelItem.getItemQuantity();
        String timestamp = modelItem.getTimestamp();

        titleTv.setText(itemName);
        descriptionTv.setText(itemDesc);
        categoryTv.setText(itemCategory);
        quantityTv.setText(itemQuantity);

        bottomSheetDialog.show();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditItemActivity.class);
                intent.putExtra("itemId", id);
                context.startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete " +itemName+" ?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(id);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void deleteItem(String id) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Items").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Item deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter = new FilterItems(this, filterList);
        }
        return filter;
    }

class Holderitem extends RecyclerView.ViewHolder{

        private ImageView itemIconTv;
        private TextView QuantityTv, DescriptionTv, ItemNameTv;
        public Holderitem(@NonNull View itemView) {
            super(itemView);

            itemIconTv = itemView.findViewById(R.id.itemIconTv);
            ItemNameTv = itemView.findViewById(R.id.ItemNameTv);
            QuantityTv = itemView.findViewById(R.id.QuantityTv);
            DescriptionTv = itemView.findViewById(R.id.DescpritionTv);
        }
    }
}

