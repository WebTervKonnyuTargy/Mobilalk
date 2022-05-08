package com.example.gyumi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    private static final String LOG_TAG=ShopActivity.class.getName();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private static final int reqcode=69;
    private NotificationHandler mNotificationHandler;
    private RecyclerView recycle;
    private ArrayList<ShoppingItem> mItemlist;
    private ShoppingitemAdapter mAdapter;
    private int gridN=1;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        checkUserPermission();
        int secretkey=getIntent().getIntExtra("secret",0);
        if(secretkey!=69){
            finish();
        }
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user!=null){
            Log.e(LOG_TAG,user.getEmail());
        }else{
            Log.e(LOG_TAG,"anon");
            finish();
        }
        mNotificationHandler=new NotificationHandler(this);


        recycle=findViewById(R.id.recyclerview);
        recycle.setLayoutManager(new GridLayoutManager(this,gridN));
        mItemlist=new ArrayList<>();
        mAdapter=new ShoppingitemAdapter(this,mItemlist);

        recycle.setAdapter(mAdapter);

        mFirestore=FirebaseFirestore.getInstance();
        mItems=mFirestore.collection("items");

        queryData();
    }

    private void queryData(){
                mItemlist.clear();

                mItems.orderBy("rating", Query.Direction.DESCENDING).limit(3).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                        ShoppingItem item=document.toObject(ShoppingItem.class);
                        mItemlist.add(item);
                    }

                    if(mItemlist.size()==0){
                        initalizeData();
                        queryData();
                    }
                    mAdapter.notifyDataSetChanged();
                });

    }



    public void initalizeData(){
        String[] itemlist=getResources().getStringArray(R.array.shopping_item_names);
        String[] iteminfo=getResources().getStringArray(R.array.shopping_item_info);
        String[] itemxd=getResources().getStringArray(R.array.shopping_item_cryisfree);

        TypedArray itemsImgResource=getResources().obtainTypedArray(R.array.shopping_item_img);
        TypedArray itemsRating=getResources().obtainTypedArray(R.array.shopping_item_rating);

        for(int i=0;i<itemlist.length;i++){
            mItems.add(new ShoppingItem(itemlist[i],iteminfo[i],itemxd[i],itemsRating.getFloat(i,0),itemsImgResource.getResourceId(i,0)));
        }
        itemsImgResource.recycle();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void checkUserPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},reqcode);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            auth.signOut();
            mNotificationHandler.cancel();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}