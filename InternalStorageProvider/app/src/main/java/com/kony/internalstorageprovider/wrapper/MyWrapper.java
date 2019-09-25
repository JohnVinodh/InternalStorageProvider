package com.kony.internalstorageprovider.wrapper;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.konylabs.android.KonyMain;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MyWrapper {

    private static String mFileName = "";//"JohnVinodh.txt";
    private static String mBase64Data;
    private static byte[] byteArrayXls = null, byteArrayPdf = null, byteArrayText;
    private static String mFileFormat = "";
    private static KonyMain mContext;

    public static void onBtnSaveDataToInternalStorage(String pBase64Data, String pFileName, String pFileFormat, String pContent) {
        mFileName = pFileName;
        mFileFormat = pFileFormat;
        mContext = KonyMain.getActivityContext();
        mBase64Data = pBase64Data;
        String tempBase64Data = mBase64Data;
        if (pFileFormat.equalsIgnoreCase("pdf")) {
            byteArrayPdf = Base64.decodeBase64(tempBase64Data.getBytes());
            File file = new File(mContext.getFilesDir(), mFileName + "." + mFileFormat);
            saveDataToInternalStorage(file, byteArrayPdf);
        } else if (pFileFormat.equalsIgnoreCase("xls")) {
            File file = new File(mContext.getFilesDir(), mFileName + "." + mFileFormat);
            byteArrayXls=Base64.decodeBase64(tempBase64Data.getBytes());
            saveDataToInternalStorage(file, byteArrayXls);
            // do your logic here for further modification same for other formats too.
        } else if (pFileFormat.equalsIgnoreCase("text") || pFileFormat.equalsIgnoreCase("txt")) {
            File file = new File(mContext.getFilesDir(), mFileName + ".txt");
            saveDataToInternalStorage(file, pContent.getBytes());
        } else {
            Toast.makeText(mContext, "File Format is not matching", Toast.LENGTH_LONG).show();
        }
    }

    public static void saveDataToInternalStorage(File file, byte[] byteArrayData) {
        mContext = KonyMain.getActivityContext();
        if (file.exists()) {
            Toast.makeText(mContext, "File already Saved Successfully", Toast.LENGTH_LONG).show();
        }
        else {
            if (!writeDataToFile(file, byteArrayData)) {
                Toast.makeText(mContext, "File is not saved", Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(mContext, "File Saved Successfully", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static boolean writeDataToFile(File file, byte[] byteArray) {

        FileOutputStream outputStream = null;
        try {
            if (file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    return false;
                }
            }
            outputStream = new FileOutputStream(file);
            if (byteArray != null)
                outputStream.write(byteArray);
            else
                Log.i("JohnVinodh", "Empty String is being written to a file");
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            Log.e("JohnVinodh", "Error writing file" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("JohnVinodh", "Error closing the output stream" + e.getMessage());
            }
        }
        return false;
    }

    public static void retrieveDataFromInternalStorage(String pFileName, String pFileFormat) {
            String mimeType = "";
            if (pFileFormat.equalsIgnoreCase("pdf")) {
                mimeType = "application/pdf";
            } else if (pFileFormat.equalsIgnoreCase("text") || pFileFormat.equalsIgnoreCase("txt")) {
                mimeType ="text/plain";
            } else if (pFileFormat.equalsIgnoreCase("xls")) {
                mimeType = "application/xls";
            } else {
                mimeType = "application/*";
            }
        openFileFromInternalStorage(mimeType,pFileName,pFileFormat);
    }

    public static void deleteFileFromInternalStorage(String pFileName,String pFileFormat) {
        File file = new File(mContext.getFilesDir(), pFileName + "." + pFileFormat);
        mContext = KonyMain.getActivityContext();
        if (file.exists()) {
            Toast.makeText(mContext, "File is already saved and currently deleting the File", Toast.LENGTH_LONG).show();
            boolean deleteStatus = file.delete();
            if(deleteStatus) {
                Log.i("JohnVinodh", "Deletion of the File is successful");
                Toast.makeText(mContext,"",Toast.LENGTH_LONG).show();
            } else {
                Log.i("JohnVinodh", "Deletion of the File is unsuccessful");
                Toast.makeText(mContext,"Deletion of the File is unsuccessful",Toast.LENGTH_LONG).show();
            }
        } else {
            Log.i("JohnVinodh", "File is not created yet");
            Toast.makeText(mContext,"File is not created yet",Toast.LENGTH_LONG).show();
        }
    }

    private static void openFileFromInternalStorage(String mimeType, String pFileName, String pFileFormat)
    {
        mContext = KonyMain.getActivityContext();
        File file = new File(mContext.getFilesDir(), pFileName+"."+pFileFormat);
        if (file.exists()) {
            Intent viewIntent = new Intent();
            viewIntent.setAction(Intent.ACTION_VIEW);
            viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Uri uripath = FileProvider.getUriForFile(mContext, mContext.getPackageName(), file);
            viewIntent.setDataAndType(uripath, mimeType);
            viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // viewIntent.setDataAndType(Uri.fromFile(file), mimeType);
            // using the packagemanager to query is faster than trying startActivity
            // and catching the activity not found exception, which causes a stack unwind.
            List<ResolveInfo> resolved = mContext.getPackageManager().queryIntentActivities(viewIntent, 0);
            if (resolved != null && resolved.size() > 0) {
                mContext.startActivity(viewIntent);
            } else {
                Toast.makeText(mContext, "Could not find any applications to open the file", Toast.LENGTH_LONG).show();
                Log.i("JohnVinodh", "Could not find any applications to open the file");
            }
        } else {
            Log.e("JohnVinodh", "File you are trying to access doesn't exist");
            Toast.makeText(mContext, "File you are trying to access doesn't exist", Toast.LENGTH_LONG).show();
        }
    }
}
