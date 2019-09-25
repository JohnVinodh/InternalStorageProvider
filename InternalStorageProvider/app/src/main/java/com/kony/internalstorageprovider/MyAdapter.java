package com.kony.internalstorageprovider;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.konylabs.android.KonyMain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by KH2195 on 19-Jun-18.
 */

public class MyAdapter {

    public MyAdapter() {

    }

    byte[] byteArray = null;
    private static final String mFileName = "JohnVinodh2.txt";//"JohnVinodh.txt";

    public void onBtnSaveDataToInternalStorage(String emailaddress, String mailSubject, String pcontent) {
        KonyMain context = KonyMain.getActivityContext();
        File file = new File(context.getFilesDir(), mFileName);
        if (!writeDataToFile(file, pcontent)) {
            return;
        }
        Uri uriPath = FileProvider.getUriForFile(context, "com.kony.internalstorageprovider", file);

        Log.i("JohnVinodh", "URI of the file written is ::" + uriPath);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // set the type to 'email'
        emailIntent.setType("application/*|text/*|*/email|*/*");
        String to[] = {emailaddress};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, uriPath);
        // the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
    private boolean writeDataToFile(File file, String content) {
        FileOutputStream outputStream = null;
        try {
            if (file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    return false;
                }
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            //outputStream.write(byteArray);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            Log.e("JohnVinodh","Error writing file"+e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if(outputStream !=null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("JohnVinodh","Error closing the output stream"+e.getMessage());
            }
        }
        return false;

    }
}
