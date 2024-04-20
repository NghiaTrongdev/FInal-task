package com.example.baitap1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Thisinh> listThisinh;
    private ListView lstmain;
    private Button addBtn , testBtn;
    private EditText search;
    private myAdapter adapter;
    private DatabaseManager database;
    private int selectedItem;
    private ContentProvider content;
    private Notifications notifications;

    private NetworkNotification networkNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        notifications = new Notifications();

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(notifications,filter);

        networkNotification = new NetworkNotification();
        IntentFilter networkfilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkNotification, networkfilter);

        loadData();


        adapter = new myAdapter(this,listThisinh);
        lstmain.setAdapter(adapter);
        listener();

    }
    private void init()
    {
        database = new DatabaseManager(this);
        lstmain = findViewById(R.id.lstMain);
        addBtn = findViewById(R.id.addButton);
        testBtn = findViewById(R.id.test);
        search = findViewById(R.id.search);
        listThisinh = new ArrayList<>();
        registerForContextMenu(lstmain);

    }
    private void listener(){
        addBtn.setOnClickListener( v ->{
            startActivity(new Intent(this,AddActivity.class));

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadData(){
        listThisinh = database.getAllThisinh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    private void reloadData(){

        listThisinh.clear();
        listThisinh.addAll(database.getAllThisinh());
        adapter.notifyDataSetChanged();
        database.close();

    }
    private void sortByTotalPoint(){
        Collections.sort(listThisinh, new Comparator<Thisinh>() {
            @Override
            public int compare(Thisinh o1, Thisinh o2) {
                return Double.compare(o1.getToan() + o1.getLy() + o1.getHoa(),o2.getToan() + o2.getLy() + o2.getHoa());
            }
        });
        adapter.notifyDataSetChanged();
    }
    private void sortById(){
        Collections.sort(listThisinh, new Comparator<Thisinh>() {
            @Override
            public int compare(Thisinh o1, Thisinh o2) {
                return o1.getSbd().compareTo(o2.getSbd());
            }
        });
        adapter.notifyDataSetChanged();
    }
    private void sortByAveragePoint(){
        Collections.sort(listThisinh, new Comparator<Thisinh>() {
            @Override
            public int compare(Thisinh o1, Thisinh o2) {
                return Double.compare((o1.getToan() + o1.getLy() + o1.getHoa())/3,(o2.getToan() + o2.getLy() + o2.getHoa())/3);
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.DiemTong){
            sortByTotalPoint();
            return true;
        } else if (item.getItemId() == R.id.Sbd){
            sortById();
            return true;
        } else if (item.getItemId() == R.id.DiemTrungBinh){
            sortByAveragePoint();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        if (item.getItemId() == R.id.menuedit){
            editItem(position);
            return true;
        } else if (item.getItemId() == R.id.menudelete) {
            deleteItem(position);
            return true;
        } else if (item.getItemId() == R.id.menusearch){
//            searchItem(position);
            searchItem2(position);
            return true;
        }
        return super.onContextItemSelected(item);

    }
    private void editItem(int position){
        Thisinh selectedThisinh = listThisinh.get(position);
        if (selectedThisinh != null){
            Toast.makeText(this, "Vi tri "+ selectedThisinh.getSbd(), Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this,AddActivity.class);
        intent.putExtra("Data",selectedThisinh);
        startActivity(intent);

    }
    private void deleteItem(int position){
        Thisinh deletedthisinh = listThisinh.get(position);
        if (deletedthisinh!=null){
            listThisinh.remove(deletedthisinh);
            adapter.notifyDataSetChanged();
        }
    }
    private void searchItem(int position){
        Thisinh thisinh = listThisinh.get(position);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);

        }

        ContentResolver contentResolver = getContentResolver();
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null,null,null,null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = ContactsContract.Contacts.DISPLAY_NAME;
                String phone = ContactsContract.Contacts.HAS_PHONE_NUMBER;

                // Kiểm tra nếu tên liên lạc trùng với tên của đối tượng Thisinh
                if (name.equals(thisinh.getName())) {
                    Toast.makeText(this, "Tên: " + name + ", Số điện thoại: " + phone, Toast.LENGTH_SHORT).show();

                }
//                else {
//                    Toast.makeText(this, "Khong tim duoc", Toast.LENGTH_SHORT).show();
//                }
            }
            cursor.close();
        }

    }
    private void searchItem2(int position) {
        Thisinh thisinh = listThisinh.get(position);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
            return;
        }

        // Mảng chuỗi , chứa tên cột mà mình muốn lấy dữ liệu
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

        // selection là một chuỗi SQL đại diện cho điều kiện WHERE của truy vấn. Trong trường hợp này,
        // bạn muốn tìm kiếm các liên lạc trong danh bạ có tên giống với tên của thisinh.
        // Do đó, bạn đặt điều kiện là "DISPLAY_NAME = ?".

        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?";

        //selectionArgs là một mảng các giá trị được sử dụng để thay thế các placeholder trong điều
        // kiện WHERE (selection). Trong trường hợp này, bạn muốn thay thế ? bằng tên của thisinh,
        // nên bạn đặt giá trị của selectionArgs là một mảng gồm một phần tử, là tên của thisinh
        String[] selectionArgs = new String[]{thisinh.getName()};

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, selectionArgs, null);
        String name="",phone="";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                if (nameIndex != -1 && phoneIndex != -1) {
                     name = cursor.getString(nameIndex);
                     phone = cursor.getString(phoneIndex);
                    Toast.makeText(this, "Tên: " + name + ", Số điện thoại: " + phone, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Không tìm thấy thông tin cho: " + thisinh.getName(), Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(this, "Tên: " + name + ", Số điện thoại: " + phone, Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Không tìm thấy thông tin cho: " + thisinh.getName(), Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notifications);
    }
}