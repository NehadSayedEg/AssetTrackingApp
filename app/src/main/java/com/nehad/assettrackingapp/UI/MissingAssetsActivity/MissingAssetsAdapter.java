package com.nehad.assettrackingapp.UI.MissingAssetsActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nehad.assettrackingapp.Database.entities.AssetModel;
import com.nehad.assettrackingapp.R;
import com.nehad.assettrackingapp.UI.AllAssetsActivity.AllAssetsAdapter;

import java.util.List;

public class MissingAssetsAdapter extends RecyclerView.Adapter<MissingAssetsAdapter.AssetsViewHolder> {
        private List<AssetModel> assetModelList ;

     public  MissingAssetsAdapter(List<AssetModel> assetModelList) {
            this.assetModelList = assetModelList;
        }

        @NonNull
        @Override
        public MissingAssetsAdapter.AssetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MissingAssetsAdapter.AssetsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.assets_item , parent ,false ));
        }

        @Override
        public void onBindViewHolder(@NonNull AssetsViewHolder holder, int position) {
            holder.setAssetItem(assetModelList.get(position));

        }

        @Override
        public int getItemCount() {
            return assetModelList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        static  class AssetsViewHolder extends RecyclerView.ViewHolder {
            TextView barcodeTxt ,  desTxt , locationTxt , statusTxt ;

            AssetsViewHolder(@NonNull View itemView) {
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

                locationTxt.setText(assetModel.getLocation());
                if(assetModel.isFound() == false){
                    statusTxt.setText(R.string.itemStatusMissing);

                }else {
                    statusTxt.setText(R.string.itemStatusfound);

                }
            }
        }
    }
