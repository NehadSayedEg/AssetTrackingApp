package com.nehad.assettrackingapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.nehad.assettrackingapp.Database.AssetsDatabase;
import com.nehad.assettrackingapp.R;
import com.nehad.assettrackingapp.UI.AllAssetsActivity.AllAssetsActivity;
import com.nehad.assettrackingapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    public static final String TAG = HomeActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_home);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        binding.AllAssetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this , AllAssetsActivity.class);
                startActivity(intent);


            }
        });


        String [] options = {"Software Dep ", " CEO", "General manger", "Sales"};
        ArrayAdapter <String> arrayAdapter = new ArrayAdapter( this , R.layout.option_item_location ,options);

        binding.autoComplete.setAdapter(arrayAdapter);
        binding.autoComplete.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent , View view , int position , long id){
                String item  = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext() , "Item :" + item , Toast.LENGTH_LONG).show();

            }


        });


        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearAssetsDB();
                Toast.makeText(getApplicationContext() , "Bach to Main" , Toast.LENGTH_LONG).show();

                Intent intent = new Intent(HomeActivity.this , MainActivity.class);
                startActivity(intent);
                finish();


            }
        });


    }



    private void clearAssetsDB() {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: Clear database Table ...");

            AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().DeleteAllAssets();

            Log.i("DatabaseSize",
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size()
                            + "");

        }).start();

    }



}