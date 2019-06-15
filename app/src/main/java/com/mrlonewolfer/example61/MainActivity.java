package com.mrlonewolfer.example61;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 Android Requesting Permission:
  -Give Runtime Permission for Internet and READ and WRITE External Storage.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int My_External_storege_permision =1 ;
    private static final String FOLDER_NAME ="Storage" ;
    private static final String My_FILE_NAME ="myFileName" ;
    Button btnRead,btnWrite;
    EditText edtMsg;
    TextView txtResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRead=findViewById(R.id.readData);
        btnWrite=findViewById(R.id.writeData);
        edtMsg=findViewById(R.id.edtMsg);
        txtResult=findViewById(R.id.txtResult);
        btnRead.setOnClickListener(this);
        btnWrite.setOnClickListener(this);


        checkStoragePermision();
    }

    private void checkStoragePermision() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},My_External_storege_permision);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==My_External_storege_permision){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Thank You", Toast.LENGTH_SHORT).show();
            }
            else{
                btnRead.setVisibility(View.GONE);
                btnWrite.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.readData){
          
            ReadDataFromPublicMemory();
        }
        if(v.getId()==R.id.writeData){

            WriteDataToPublicMemory();
        }

    }

    private void ReadDataFromPublicMemory() {

        File myFolder = Environment.getExternalStorageDirectory();
        myFolder= new File(myFolder,FOLDER_NAME);
        if(!myFolder.exists()){

            Toast.makeText(this, "Directory is Not Availabele", Toast.LENGTH_SHORT).show();
            return;
        }
        //After Checking Folder it will visit FIle
        myFolder=new File(myFolder,My_FILE_NAME);


        if(!myFolder.exists()){

            Toast.makeText(this, "Their is nothing To Display", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            FileInputStream fileInputStream= new FileInputStream(myFolder);
            byte b[]= new byte[fileInputStream.available()];
            fileInputStream.read(b);
            String msg=new String(b);
            txtResult.setText(msg);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void WriteDataToPublicMemory() {

        File myFolder = Environment.getExternalStorageDirectory();
        myFolder=new File(myFolder,FOLDER_NAME);
        if(!myFolder.exists()){
            if(myFolder.mkdir()){
                Toast.makeText(this, "Folder created Succesfully", Toast.LENGTH_SHORT).show();
            }
            else{

                Toast.makeText(this, "Folder is not Cretaed Succesfully", Toast.LENGTH_SHORT).show();
            }
        }
        myFolder= new File(myFolder,My_FILE_NAME);
        String msg=edtMsg.getText().toString();


        try {
            FileOutputStream fileOutputStream=new FileOutputStream(myFolder);
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "our Message Save Succesfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        edtMsg.setText("");
    }
}
