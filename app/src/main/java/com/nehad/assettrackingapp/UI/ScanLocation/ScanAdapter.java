package com.nehad.assettrackingapp.UI.ScanLocation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScanAdapter   extends RecyclerView.Adapter<ScanAdapter.ScanViewHolder>{
    List<AssetModel> AssetsList ;
    private int selectedItem = 0;
    private AssetsDatabase database;
    private Activity activity ;
    Context context ;



    public ScanAdapter(List<AssetModel> AssetsList ,Activity activity ,Context context ) {
        this.AssetsList = AssetsList;
        this.activity = activity;
        this.context = context;
        notifyDataSetChanged();

    }



    @NonNull
    @Override
    public ScanAdapter.ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assets_item, parent,false);
        final  ScanViewHolder  scanViewHolder  = new ScanViewHolder(v);
        return scanViewHolder ;

    }

    @Override
    public void onBindViewHolder(@NonNull ScanAdapter.ScanViewHolder holder, int position) {
        database = AssetsDatabase.getAssetsDatabase(activity);

        holder.setAssetItem(AssetsList.get(position));




    }


    @Override
    public int getItemCount() {
        return AssetsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    static  class ScanViewHolder extends RecyclerView.ViewHolder{
        TextView barcodeTxt ,  desTxt , locationTxt , statusTxt ;


        ScanViewHolder(@NonNull View itemView) {
            super(itemView);
            barcodeTxt = itemView.findViewById(R.id.itemBarcode);
            desTxt = itemView.findViewById(R.id.itemDes);
            locationTxt = itemView.findViewById(R.id.itemLoc);
            statusTxt = itemView.findViewById(R.id.itemStatus);
        }

        void setAssetItem(AssetModel assetModel){
            barcodeTxt.setText(assetModel.getBarcode());
            desTxt.setText(assetModel.getDescription());
            locationTxt.setText(assetModel.getLocation());
            if(assetModel.isFound() == false){
                statusTxt.setText(R.string.itemStatusMissing);

            }else {
                statusTxt.setText(R.string.itemStatusfound);

            }

        }
    }
}
