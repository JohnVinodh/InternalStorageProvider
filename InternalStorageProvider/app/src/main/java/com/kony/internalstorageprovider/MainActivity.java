package com.kony.internalstorageprovider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kony.internalstorageprovider.utils.FileUtils;
import com.moonlit.jarvis.fileproviderstoragelibrary.FileProviderUtils;

public class MainActivity extends AppCompatActivity {

    String content = "";
    private static String mFileName = "JohnVinodh123";//"JohnVinodh.txt";
    private static String base64Data;
    //byte[] byteArrayXls = null, byteArrayPdf = null, byteArrayText;
    private String fileFormat = "";
    FileProviderUtils mFileUtilsObj = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFileUtilsObj= new FileProviderUtils();
        /*mFileUtilsObj.deleteFileFromInternalStorage(getApplicationContext(),mFileName,"txt");
        mFileUtilsObj.deleteFileFromInternalStorage(getApplicationContext(),mFileName,"xls");
        mFileUtilsObj.deleteFileFromInternalStorage(getApplicationContext(),mFileName,"pdf");*/
        content = "Hello John";

    }

    public void onBtnRetrieveDataFromInternalStoragepdf(View view) {

        mFileUtilsObj.retrieveFileFromInternalStorage(getApplicationContext(),mFileName,"pdf");
            }

    public void onBtnRetrieveDataFromInternalStoragexls(View view) {
        mFileUtilsObj.retrieveFileFromInternalStorage(getApplicationContext(),mFileName,"xls");


    }

    public void onBtnRetrieveDataFromInternalStorageText(View view) {


        mFileUtilsObj.retrieveFileFromInternalStorage(getApplicationContext(),mFileName,"txt");


    }




    public void onBtnSaveDataToInternalStoragepdf(View view) {
        fileFormat = "pdf";
        base64Data = getString(R.string.base64pdf);
        Log.i("JohnVinodh", "File name is ::" + mFileName);
        //File file = new File(getFilesDir(), mFileName + "." + fileFormat);
        //saveDataToInternalStorage(file, byteArrayPdf);
        mFileUtilsObj.saveFileToInternalStorage(getApplicationContext(),base64Data,mFileName,fileFormat,"");

    }


    public void onBtnSaveDataToInternalStorageText(View view) {
        fileFormat = "txt";
                mFileUtilsObj.saveFileToInternalStorage(getApplicationContext(),"",mFileName,fileFormat,content);

    }

    public void onBtnSaveDataToInternalStoragexls(View view) {
        fileFormat = "xls";
         base64Data = getResources().getString(R.string.base64pdf);
        mFileUtilsObj.saveFileToInternalStorage(getApplicationContext(),base64Data,mFileName,fileFormat,"");
    }



}
