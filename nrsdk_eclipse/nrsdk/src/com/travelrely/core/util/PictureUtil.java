
package com.travelrely.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.MeasureSpec;

import com.travelrely.sdk.R;

public class PictureUtil {

    /**
     * 把bitmap转换成Byte
     * 
     * @param filePath
     * @return
     */
    public static byte[] bitmapToByte(String filePath) {

        File file = new File(filePath);
        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return b;
        }
        return null;
    }

    /**
     * 根据路径删除图片
     * 
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录
     * 
     * @return
     */
    public static File getAlbumDir(String pash) {
        File dir = new File(pash);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取保存 隐患检查的图片文件夹名称
     * 
     * @return
     */
    public static String getAlbumName() {
        return "sheguantong";
    }

    /**
     * 显示选择对话框
     */

    /* 请求码 */
    public static final int IMAGE_REQUEST_CODE = 0;

    public static final int CAMERA_REQUEST_CODE = 1;

    public static final int RESULT_REQUEST_CODE = 2;

    /* 头像名称 */
    public static final String IMAGE_FILE_NAME = "faceImage.jpg";

    public static void showDialog(final Activity mContext) {

        String[] items = new String[] {
                mContext.getString(R.string.selLocalPhoto),
                mContext.getString(R.string.takingPicture)
        };

        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.setPhoto))
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                            		
                            		Intent intentFromGallery = new Intent();
                            		intentFromGallery.setType("image/*"); // 设置文件类型
                            		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                            		mContext.startActivityForResult(intentFromGallery,
                            				IMAGE_REQUEST_CODE);
                                break;
                            case 1:

                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (Utils.hasSdcard()) {

                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                                }

                                mContext.startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton(mContext.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    /**
     * 裁剪图片方法实现
     * 
     * @param uri
     */
    public static void startPhotoZoom(final Activity mContext, Handler handler, final Uri uri) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                // 设置裁剪
                intent.putExtra("crop", "true");
                // aspectX aspectY 是宽高的比例
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                // outputX outputY 是裁剪图片宽高
                intent.putExtra("outputX", 480);
                intent.putExtra("outputY", 480);
                // 三星i9100拍照需要"return-data", true
                intent.putExtra("return-data", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true); // no face detection
                mContext.startActivityForResult(intent, PictureUtil.RESULT_REQUEST_CODE);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    /**
     * 保存裁剪之后的图片数据
     * 
     * @param picdata
     * @throws IOException
     */
    @SuppressLint({ "SimpleDateFormat", "NewApi" })
    public static String getImageToView(Intent data, Activity mContext, Uri defaultUri)
            throws IOException {
        Bundle extras = data.getExtras();
        String headFilePath = null;
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");

            if (photo == null) {
                Uri url = data.getData();
                if (url == null && data.getAction() != null) {
                    url = Uri.parse(data.getAction());
                }
                if (url == null) {
                    url = defaultUri;
                }
                photo = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(
                        url));
                
                
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timeStamp = format.format(new Date());
            headFilePath = "headName_" + timeStamp;

            FileUtil fUtil = new FileUtil(mContext);
            fUtil.saveBitmap(photo, "head_img", headFilePath + ".jpg");
        }
        return headFilePath;
    }

    public static Bitmap readImg(String path) {
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 读取图片属性：旋转的角度
     * 
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap loadImageFromUrl(String src)
    {

        Options options = new Options();
        options.inJustDecodeBounds = true;// 设置解码只是为了获取图片的width和height值,而不是真正获取图片
        // options.inDither=false; /*不进行图片抖动处理*/
        // options.inPreferredConfig=null; /*设置让解码器以最佳方式解码*/
        // options.inPurgeable = true;
        // options.inInputShareable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(src, options);// 解码后可以options.outWidth和options.outHeight来获取图片的尺寸
        int widthRatio = (int) Math.ceil(options.outWidth / Utils.big_bitmap_width);// 获取宽度的压缩比率
        int heightRatio = (int) Math.ceil(options.outHeight / Utils.big_bitmap_height);// 获取高度的压缩比率

        if (widthRatio > 1 || heightRatio > 1) {// 只要其中一个的比率大于1,说明需要压缩
            if (widthRatio >= heightRatio) {// 取options.inSampleSize为宽高比率中的最大值
                options.inSampleSize = widthRatio;
            } else {
                options.inSampleSize = heightRatio;
            }
        }
        options.inJustDecodeBounds = false;// 设置为真正的解码图片
        bitmap = BitmapFactory.decodeFile(src, options);// 解码图片

        return bitmap;
    }
    
    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
