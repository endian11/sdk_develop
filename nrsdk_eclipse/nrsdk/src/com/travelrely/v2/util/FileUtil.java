package com.travelrely.v2.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.travelrely.core.Engine;
import com.travelrely.v2.response.GroupMsg;
import com.travelrely.v2.response.TraMessage;

public class FileUtil
{
    Context context;

    public FileUtil(Context context)
    {
        this.context = context;
    }

    public void save(String fileName, Object o) throws Exception
    {
        String path = context.getFilesDir() + "/";

        File dir = new File(path);
        dir.mkdirs();

        File f = new File(dir, fileName);
        if (f.exists())
        {
            f.delete();
        }

        FileOutputStream os = new FileOutputStream(f);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(o);
        objectOutputStream.close();
        os.close();
    }

    public Object readObject(String fileName) throws Exception
    {
        String path = context.getFilesDir() + "/";

        File dir = new File(path);
        dir.mkdirs();
        File file = new File(dir, fileName);
        if (!file.exists())
        {
            return null;
        }
        else
        {
            InputStream is = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(is);
            Object o = objectInputStream.readObject();
            return o;
        }
    }

    public boolean saveImg(TraMessage message, Bitmap bitmap)
    {
        String imgPath = getStorePath(message);

        File imageFile = null;
        String url = null;
        if (message.isLocation())
        {

            imageFile = new File(imgPath, "location" + message.getId() + ".jpg");
            url = imgPath + "/" + "location" + message.getId() + ".jpg";
        }
        else if (message.isJPG())
        {
            imageFile = new File(imgPath, message.getContent() + ".jpg");
            url = imgPath + "/" + message.getContent() + ".jpg";
        }

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        message.setUrl(url);
        return true;
    }

    public boolean saveVoice(TraMessage message, InputStream inputStream)
    {
        String voicePath = getStorePath(message);
        File voiceFile = new File(voicePath, message.getContent() + ".amr");
        String url = voicePath + "/" + message.getContent() + ".amr";
        FileOutputStream fileOutputStream = null;
        if (voiceFile.exists())
        {
            voiceFile.delete();
        }

        try
        {
            int i = -1;
            fileOutputStream = new FileOutputStream(voiceFile);
            while ((i = inputStream.read()) != -1)
            {
                fileOutputStream.write(i);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        message.setUrl(url);
        return true;
    }

    public boolean saveVoice(TraMessage message, String systemPath)
    {
        String voicePath = getStorePath(message);
        File voiceFile = null;
        voiceFile = new File(voicePath, message.getContent() + ".amr");
        String url = voicePath + "/" + message.getContent() + ".amr";
        FileOutputStream fileOutputStream = null;
        FileInputStream fileInputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(voiceFile);
            fileInputStream = new FileInputStream(systemPath);
            byte[] bytes = new byte[256];
            while (fileInputStream.read(bytes) > 0)
            {
                fileOutputStream.write(bytes);
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        // 把原来系统里的语音文件删除
        File file = new File(systemPath);
        file.delete();
        message.setUrl(url);
        return true;
    }

    private String getStorePath(TraMessage message)
    {
        String voicePath;
        if (checkSDCard())
        {
            // String dirPath = "/mnt/sdcard";
            File dirPath = Environment.getExternalStorageDirectory();

            String path = dirPath + "/travelrely/voice";
            File file = new File(path);
            if (!file.exists())
            {
                file.mkdirs();
            }

            voicePath = path;
            message.setStore_type(0);
        }
        else
        {
            String path = context.getFilesDir() + "/";

            File dir = new File(path);
            dir.mkdirs();
            // files/img
            File file = new File(dir, "voice");
            if (!file.exists())
            {
                file.mkdirs();
            }

            voicePath = file.getAbsolutePath();
            message.setStore_type(1);
        }

        return voicePath;
    }

    public boolean saveHeadImg(TraMessage message, Bitmap bitmap)
            throws IOException
    {
        String imgPath = "";
        if (checkSDCard())
        {
            String path = getImagePath("head_img");
            File file = new File(path);
            if (!file.exists())
            {
                file.mkdirs();
            }

            imgPath = path;
            message.setStore_type(0);
        }
        else
        {
            String path = context.getFilesDir() + "/";

            File dir = new File(path);
            dir.mkdirs();
            // files/img
            File file = new File(dir, "img");
            if (!file.exists())
            {
                file.mkdirs();
            }

            imgPath = file.getAbsolutePath();
            message.setStore_type(1);
        }

        File imageFile = null;
        String url = null;

        imageFile = new File(imgPath, message.getHead_portrait() + "_s"
                + ".jpg");
        url = imgPath + "/" + message.getHead_portrait() + "_s" + ".jpg";

        try
        {
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        message.setHead_portrait_url(url);
        return true;
    }

    public String saveHeadImg(String imgName, Bitmap bitmap) throws IOException
    {
        String imgPath;
        if (checkSDCard())
        {
            String path = getImagePath("head_img");

            File file = new File(path);
            if (!file.exists())
            {
                file.mkdirs();
            }
            imgPath = path;
        }
        else
        {
            String path = context.getFilesDir() + "/";

            File dir = new File(path);
            dir.mkdirs();
            // files/img
            File file = new File(dir, "img");
            if (!file.exists())
            {
                file.mkdirs();
            }

            imgPath = file.getAbsolutePath();
        }

        File imageFile = null;
        String url = null;

        imageFile = new File(imgPath, imgName + "_s" + ".jpg");

        try
        {
            FileOutputStream out = new FileOutputStream(imageFile);
            if (bitmap != null)
            {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

            }
            else
            {

            }
            out.flush();
            out.close();
            url = imageFile.getAbsolutePath();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        // message.setHead_portrait_url(url);
        return url;
    }

    public boolean saveGroupHeadImg(GroupMsg groupMsg, Bitmap bitmap)
            throws IOException
    {
        String imgPath = "";
        if (checkSDCard())
        {

            String path = getImagePath("head_img");

            File file = new File(path);
            if (!file.exists())
            {
                file.mkdirs();
            }
            imgPath = path;
        }
        else
        {
            String path = context.getFilesDir() + "/";

            File dir = new File(path);
            dir.mkdirs();
            // files/img
            File file = new File(dir, "img");
            if (!file.exists())
            {
                file.mkdirs();
            }

            imgPath = file.getAbsolutePath();
        }
        File imageFile = null;
        String url = null;

        imageFile = new File(imgPath, groupMsg.getHead_portrait() + "_s"
                + ".jpg");

        try
        {
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public boolean saveUserHeadImg(String headImgName, String type,
            Bitmap bitmap) throws IOException
    {
        String imgPath = "";
        if (checkSDCard())
        {

            String path = getImagePath("head_img");

            File file = new File(path);
            if (!file.exists())
            {
                file.mkdirs();
            }
            imgPath = path;
        }
        else
        {
            String path = context.getFilesDir() + "/";

            File dir = new File(path);
            dir.mkdirs();
            // files/img
            File file = new File(dir, "img");
            if (!file.exists())
            {
                file.mkdirs();
            }

            imgPath = file.getAbsolutePath();
        }
        File imageFile = null;
        String url = null;

        imageFile = new File(imgPath + "/" + headImgName + type + ".jpg");
        url = imgPath + "/" + headImgName + type + ".jpg";

        try
        {
            // 创建.nomedia文件，防止系统扫描此文件夹内容
            File noFile = new File(imgPath + "/" + ".nomedia");
            noFile.createNewFile();

            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 保存头像
     * 
     * @throws IOException
     */
    public boolean saveBitmap(Bitmap bitmap, String imageDir, String headName)
            throws IOException
    {
        String imgPath = "";
        if (checkSDCard())
        {
            String path = getImagePath(imageDir);
            File file = new File(path);
            if (!file.exists())
            {
                file.mkdirs();
            }
            imgPath = path;
        }
        else
        {
            String path = context.getFilesDir() + "/" + imageDir;
            File dir = new File(path);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            imgPath = path;
        }
        File imageFile = null;
        imageFile = new File(imgPath + "/" + headName);

        try
        {
            // 创建.nomedia文件，防止系统扫描此文件夹内容
            File noFile = new File(imgPath + "/" + ".nomedia");
            noFile.createNewFile();

            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public Bitmap readImg(TraMessage message)
    {
        Bitmap b = null;
        try
        {
            b = BitmapFactory.decodeFile(message.getUrl());
        }
        catch (Exception e)
        {

            LOGManager.d("没有在存储地方拿到图片，则拿默认图片");
        }

        if (b == null)
        {
            b = Engine.getInstance().getDefaultBitmap();
        }
        return b;
    }

    @SuppressLint("NewApi")
    public Bitmap readHeadImg(TraMessage message)
    {
        Bitmap b = null;
        try
        {
            if(!TextUtils.isEmpty(message.getHead_portrait()))
            {
                b = BitmapFactory.decodeFile(message.getHead_portrait_url());
            }
        }
        catch (Exception e)
        {
            LOGManager.d("没有在存储地方拿到图片，则拿默认图片");
            e.printStackTrace();
        }
        if (b == null)
        {
            b = Engine.getInstance().getHeadDefaultBitmap();
        }
        return b;
    }

    public Bitmap readGroupHeadImg(GroupMsg groupMsg)
    {
        Bitmap b = null;
        try
        {
            b = BitmapFactory.decodeFile(getImagePath("head_img") + "/"
                    + groupMsg.getHead_portrait() + "_s" + ".jpg");
        }
        catch (Exception e)
        {
            LOGManager.d("没有在存储地方拿到图片，则拿默认图片");
        }
        if (b == null)
        {
            b = Engine.getInstance().getHeadDefaultBitmap();
        }
        return b;
    }

    public static Bitmap readUserHeadImg(String message)
    {
        Bitmap b = null;
        try
        {
            b = BitmapFactory.decodeFile(getImagePath("head_img") + "/"
                    + message + ".jpg");
        }
        catch (Exception e)
        {
            LOGManager.d("没有在存储地方拿到图片，则拿默认图片");
        }
        if (b == null)
        {
            b = Engine.getInstance().getHeadDefaultBitmap();
        }
        return b;
    }

    // Parcelable encountered IOException writing serializable object (name =
    // com.travelrely.v2.response.Message)

    public void deleteFile(String path)
    {

        File file = new File(path);
        if (file.exists())
        {
            file.delete();
        }
    }

    public static boolean checkSDCard()
    {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static String getImagePath(String dir)
    {
        // String dirPath = "/mnt/sdcard";
        File file = Environment.getExternalStorageDirectory();
        String dirPath = file.getAbsolutePath();
        String path = dirPath + "/travelrely/" + dir;
        return path;
    }

    public static boolean fileIsExists(String fileName)
    {
        File f = new File(getImagePath("head_img") + "/" + fileName + ".jpg");
        if (!f.exists())
        {
            return false;
        }
        return true;
    }

    public static byte[] bitmapToByte(String filePath)
    {
        File file = new File(filePath);
        Bitmap bmp = BitmapFactory.decodeFile(getImagePath("head_img") + "/"
                + file.getAbsolutePath());
        byte[] b = null;

        if (bmp != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            b = baos.toByteArray();
        }
        return b;
    }

    public static Bitmap getBitmpHead(String path)
    {
        Bitmap headBitmap = null;
        Bitmap bitmap = readUserHeadImg(path);
        Bitmap small = Utils.headBitmap(bitmap);
        if (small == bitmap)
        {
        }
        else
        {
            bitmap.recycle();
        }
        headBitmap = small;
        return headBitmap;
    }

    public static Bitmap getBitmpAdv(Context context, String filename)
    {
        Bitmap bitmap = null;
        String path;
        if (checkSDCard())
        {
            path = getImagePath("adv_img") + "/" + filename;
        }
        else
        {
            path = context.getFilesDir() + "/" + "adv_img" + "/" + filename;
        }

        try
        {
            bitmap = BitmapFactory.decodeFile(path);
        }
        catch (Exception e)
        {
            LOGManager.d("没有在存储地方拿到图片，则拿默认图片");
        }

        if (bitmap == null)
        {
            if (Utils.isNetworkAvailable(context))
            {
                return null;
            }
            else
            {
                bitmap = Engine.getInstance().getAdvDefaultBitmap();
            }
        }
        return bitmap;
    }
    
    public static File readFileIs(String fileName)
    {
        File f = new File(getImagePath("") + "/" + fileName + ".jpg");
        if (!f.exists())
        {
            return null;
        }
        return f;
    }
    
    
    
    @SuppressLint("NewApi") public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	
	
}
