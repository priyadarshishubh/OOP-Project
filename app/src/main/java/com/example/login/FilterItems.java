package com.example.login;

import android.media.midi.MidiOutputPort;
import android.widget.Filter;
import java.util.ArrayList;

public class FilterItems extends Filter {
    private Adapteritem adapter;
    private ArrayList<ModelItems> filterList;

    public FilterItems(Adapteritem adapter, ArrayList<ModelItems> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if(constraint != null && constraint.length()>0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelItems> filteredModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getItemName().toUpperCase().contains(constraint)||
                filterList.get(i).getItemCategory().toUpperCase().contains(constraint)){
                    filteredModels.add(filterList.get(i));

                }
            }

        }
        else{
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.itemsList = (ArrayList<ModelItems>) results.values;
        adapter.notifyDataSetChanged();
    }
}
