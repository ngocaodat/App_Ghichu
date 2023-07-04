package com.example.ngocaodatbuoi5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database database;
    ListView lvCongViec;
    ArrayList<CongViec> arrayCongViec;
    CongViecAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = (ListView) findViewById(R.id.listviewCongViec);
        arrayCongViec = new ArrayList<>();

        adapter = new CongViecAdapter(this, R.layout.dong_cong_viec, arrayCongViec);
        lvCongViec.setAdapter(adapter);

        //tao database GhiChu
        database = new Database(this, "ghichu.sqlite", null, 1);

        // tao bang CongViec
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR(200))");

        //insert data
        //database.QueryData("INSERT INTO CongViec VALUES(null, 'Học JaVa Web')");
        GetDataCongViec();

    }

    public void DialogXoaCV(String tencv, final int id){
        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(this);
        dialogxoa.setMessage("Bạn có muôn xóa công việc " + tencv + " không?");
        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM CongViec WHERE Id = '"+ id +"'");
                Toast.makeText(MainActivity.this, "Đã xóa " + tencv, Toast.LENGTH_SHORT).show();
                GetDataCongViec();
            }
        });
        dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogxoa.show();
    }

    public void DialogSuaCongViec(String ten, int id){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua);

        EditText edtTenCV = (EditText) dialog.findViewById(R.id.editTextTenCvEdit);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.buttonXacNhan);
        Button btnHuy = (Button) dialog.findViewById(R.id.buttonHuyEdit);

        edtTenCV.setText(ten);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMoi = edtTenCV.getText().toString().trim();
                database.QueryData("UPDATE CongViec SET TenCV = '"+ tenMoi +"' WHERE Id = '"+ id +"'");
                Toast.makeText(MainActivity.this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataCongViec();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void GetDataCongViec(){
        //select data
        Cursor dataCongViec = database.GetData("SELECT * FROM CongViec");
        arrayCongViec.clear();
        while (dataCongViec.moveToNext()) {
            String ten = dataCongViec.getString(1);
            //Toast.makeText(this, ten, Toast.LENGTH_SHORT).show();
            int id = dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id, ten));

        }

        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuAdd){
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }

    private void DialogThem(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_cong_viec);

        EditText edtTen = dialog.findViewById(R.id.editTextTenCV);
        Button btnThem = (Button) dialog.findViewById(R.id.buttonThem);
        Button btnHuy = (Button) dialog.findViewById(R.id.buttonHuy);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tencv = edtTen.getText().toString();
                if(tencv.equals("")){
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên công việc!", Toast.LENGTH_SHORT).show();
                }else{
                    database.QueryData("INSERT INTO CongViec VALUES(null, '"+ tencv +"')");
                    Toast.makeText(MainActivity.this, "Đã thêm.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCongViec();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}