package com.moonlit.jarvis.fileproviderstoragelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FileProviderUtils {


    private static final String TAG = FileProviderUtils.class.getSimpleName();
    private String mFileName = "";
    private String mBase64Data;
    private byte[] byteArrayXls = null, byteArrayPdf = null, byteArrayText = null;
    private String mFileFormat = "";
    private Context mContext;
    private File mFile=null;
    public String saveFileToInternalStorage(Context pActivityContext, String pBase64Data, String pFileName, String pFileFormat, String pContent) {
        mFileName = pFileName;
        mFileFormat = pFileFormat;
        mContext = pActivityContext;
        mBase64Data = pBase64Data;
        String isFileSaved="";
        String tempBase64Data = mBase64Data;

        if (pFileFormat.equalsIgnoreCase("pdf")) {
            byteArrayPdf = Base64.decodeBase64(tempBase64Data.getBytes());
            mFile = new File(mContext.getFilesDir(), mFileName + "." + mFileFormat);
            isFileSaved =saveDataToInternalStorage(mFile, byteArrayPdf);
        } else if (pFileFormat.equalsIgnoreCase("xls")) {
            mFile = new File(mContext.getFilesDir(), mFileName + "." + mFileFormat);
            byteArrayXls = Base64.decodeBase64(tempBase64Data.getBytes());
            isFileSaved =saveDataToInternalStorage(mFile, byteArrayXls);
            // do your logic here for further modification same for other formats too.
        } else if (pFileFormat.equalsIgnoreCase("text") || pFileFormat.equalsIgnoreCase("txt")) {
            mFile = new File(mContext.getFilesDir(), mFileName + ".txt");
            isFileSaved =saveDataToInternalStorage(mFile, pContent.getBytes());
        } else {
            isFileSaved="The File Format is not matching";
            Toast.makeText(mContext, isFileSaved, Toast.LENGTH_LONG).show();
        }
        return isFileSaved;
    }

    public String saveDataToInternalStorage(File file, byte[] byteArrayData) {
        String isFileSaved="";
        if (file.exists()) {
            isFileSaved= "File already Exists";
            Toast.makeText(mContext, "File already Exists", Toast.LENGTH_LONG).show();
        } else {
            if (!writeDataToFile(file, byteArrayData)) {
                Toast.makeText(mContext, "File is not saved", Toast.LENGTH_LONG).show();
                isFileSaved= "File is not saved";

            } else {
                isFileSaved= "File Saved Successfully";
                Toast.makeText(mContext, "File Saved Successfully", Toast.LENGTH_LONG).show();

            }
        }
        return isFileSaved;
    }

    private boolean writeDataToFile(File file, byte[] byteArray) {

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
                Log.i("TAG", "Empty String is being written to a file");
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            Log.e("TAG", "Error writing file" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Error closing the output stream" + e.getMessage());
            }
        }
        return false;
    }

    public void retrieveFileFromInternalStorage(Context pActivityContext, String pFileName, String pFileFormat) {
        mContext = pActivityContext;
        String mimeType = "";
        if (pFileFormat.equalsIgnoreCase("pdf")) {
            mimeType = "application/pdf";
        } else if (pFileFormat.equalsIgnoreCase("text") || pFileFormat.equalsIgnoreCase("txt")) {
            mimeType = "text/plain";
        } else if (pFileFormat.equalsIgnoreCase("xls")) {
            mimeType = "application/xls";
        } else {
            mimeType = "application/*";
        }
        openFileFromInternalStorage(mimeType, pFileName, pFileFormat);
    }

    public void deleteFileFromInternalStorage(Context pActivityContext, String pFileName, String pFileFormat) {
        mFile = new File(mContext.getFilesDir(), pFileName + "." + pFileFormat);

        if (mFile.exists()) {
            Toast.makeText(mContext, "File is already saved and currently deleting the File", Toast.LENGTH_LONG).show();
            boolean deleteStatus = mFile.delete();
            if (deleteStatus) {
                Log.i(TAG, "Deletion of the File is successful");
                Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
            } else {
                Log.i("TAG", "Deletion of the File is unsuccessful");
                Toast.makeText(mContext, "Deletion of the File is unsuccessful", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.i("TAG", "File is not created yet");
            Toast.makeText(mContext, "File is not created yet", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isFileExistsInInternalStorage(Context pActivityContext, String pFileName, String pFileFormat) {
        mFile = new File(mContext.getFilesDir(), pFileName + "." + pFileFormat);

        if (mFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void openFileFromInternalStorage(String mimeType, String pFileName, String pFileFormat) {
        mFile = new File(mContext.getFilesDir(), pFileName + "." + pFileFormat);
        if (mFile.exists()) {
            Intent viewIntent = new Intent();
            viewIntent.setAction(Intent.ACTION_VIEW);
            viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Uri uripath = FileProvider.getUriForFile(mContext, mContext.getPackageName(), mFile);
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
                Log.i("TAG", "Could not find any applications to open the file");
            }
        } else {
            Log.e("TAG", "File you are trying to access doesn't exist");
            Toast.makeText(mContext, "File you are trying to access doesn't exist", Toast.LENGTH_LONG).show();
        }
    }
}
