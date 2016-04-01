package com.vocinno.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vocinno.utils.imageutils.imageblur.FastBlur;

public final class MethodsFile {
    // 设置应用专有文件夹名称
    public static String App_File_Folder = "/Centanet/";
    //
    public static ImageLoader mImageLoader = null;

    /**
     * 加载图片
     *
     * @param path
     * @param originSize 是否保持原有大小
     * @param canSmaller 是否可以变为原来的十分之一大小，在originSize为true的情况下，默认是原大小；
     *                   在originSize为false的情况下，默认是原来的三分之一大小
     * @return
     */
    public static Bitmap decodeFile(String path, boolean originSize,
                                    boolean canSmaller) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inJustDecodeBounds = false;
        if (originSize) {
            opt.inSampleSize = 1;
        } else if (canSmaller) {
            opt.inSampleSize = 5; // width，hight设为原来的五分一
        } else {
            opt.inSampleSize = 3; // width，hight设为原来的十分一
        }
        opt.inInputShareable = true;
        return BitmapFactory.decodeFile(path, opt);
    }

    public static byte[] decodeBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inTempStorage = new byte[16 * 1024];
        FileInputStream is = null;
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new FileInputStream(path);
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            double scale = getScaling(opts.outWidth * opts.outHeight,
                    1024 * 600);
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
                    (int) (opts.outWidth * scale),
                    (int) (opts.outHeight * scale), true);
            baos = new ByteArrayOutputStream();
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bmp.recycle();
            bmp2.recycle();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
        return baos.toByteArray();
    }

    private static double getScaling(int src, int des) {
        /**
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
         */
        double scale = Math.sqrt((double) des / (double) src);
        return scale;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * (同步下载)将url对应的网络图片下载并保存为指定路径的图片文件 注意:1、需要在子线程中调用此方法
     *
     * @param urlStr
     * @param file
     * @return
     */
    public static boolean downloadImgByUrl(String urlStr, String file) {
        try {
            MethodsFile.saveBitmapToFile(file,
                    mImageLoader.loadImageSync(urlStr), 65);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * (同步下载)将url对应的网络图片缩放为适合imageview控件大小的图片，并返回bitmap对象
     * 注意：1、imageview控件在此只是用来检测图片大小，并没有填充；2、需要在子线程中调用此方法
     *
     * @param urlStr
     * @param imageview
     * @return
     */
    public static void downloadImgByUrl(String urlStr, ImageView imageview) {
        mImageLoader.displayImage(urlStr, imageview);
    }

    /**
     * 异步下载图片并在下载完后自动绑定到ImageView控件(带有线程池自动管理)
     *
     * @param url
     * @param imageview
     */
    public static void downloadAsynicImageByUrl(Activity activity, String url,
                                                ImageView imageview) {
        mImageLoader.displayImage(url, imageview);
    }

    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName (如果以‘/’开头,则视为完整目录进行检查；否则,在应用目录下检查)
     * @return
     */
    public static boolean isFileExists(String fileName) {
        File file = null;
        if (fileName.startsWith("/")) {
            file = new File(fileName);
        } else {
            file = new File(getAutoFileDirectory() + fileName);
        }
        boolean isExist = file.exists();
        file = null;
        return isExist;
    }

    /**
     * 获取文件的大小
     *
     * @param fileName
     * @return
     */
    public static long getFileSize(String fileName) {
        long lgth = 0;
        File file = new File(fileName);
        if (file.exists()) {
            lgth = file.length();
        }
        file = null;
        return lgth;
    }

    /**
     * 获取文件路径
     *
     * @param fileName （不包含目录）
     * @return
     */
    public static String getFilePath(String fileName) {
        String strFilePath = null;
        File file = new File(getAutoFileDirectory() + fileName);
        if (file.exists()) {
            strFilePath = getAutoFileDirectory() + fileName;
        }
        file = null;
        return strFilePath;
    }

    /**
     * 清理应用文件夹目录
     */
    public static void clearAppFolder() {
        try {
            File dirFile = new File(getAutoFileDirectory());
            if (!dirFile.exists()) {
                return;
            }
            if (dirFile.isDirectory()) {
                deleteFileOrFolder(dirFile);
            }

            dirFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("FileUtil", e.toString());
        }

    }

    /**
     * 删除文件或目录
     *
     * @param file
     */
    public static void deleteFileOrFolder(File file) {
        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFileOrFolder(f);// 递归删除每一个文件
                f.delete();// 删除该文件夹
            }
        }
    }

    /**
     * 获取应用文件夹大小
     *
     * @return
     */
    public static String getAppFolderSize() {
        File dirFile = new File(getAutoFileDirectory());
        try {
            long size = getFileSize(dirFile);
            String fileSize = FormetFileSize(size);
            return fileSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        dirFile = null;
        return "0MB";
    }

    public static long getFileSize(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        flist = null;
        return size;
    }

    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = 0 + "M";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 从文件路径获取文件名(包含后缀)
     *
     * @param filePath
     * @return
     */
    public static String getFileNameFromPath(String filePath) {
        String result = filePath;
        if (filePath.indexOf("/") > 0) {
            String[] fileArr = filePath.split("/");
            result = fileArr[fileArr.length - 1];
        }
        return result;
    }

    /**
     * 根据是否有sdcard自动返回最佳可保存文件的路径
     *
     * @return
     */
    public static String getAutoFileDirectory() {
        String strPath = null;
        if (hasSDCard()) {
            strPath = Environment.getExternalStorageDirectory().getPath()
                    + App_File_Folder;
        } else {
            strPath = Environment.getDownloadCacheDirectory() + App_File_Folder;
        }
        mkdirs(strPath);
        return strPath;
    }

    public static void mkdirs(String strpath) {
        File file = new File(strpath);
        if (file.isDirectory() && !file.exists()) {
            file.mkdirs();
        } else {
            file.getParentFile().mkdirs();
        }

    }

    /**
     * 读取文件内容(避免中文乱码)
     *
     * @param strFile 带路径的文件名
     * @return
     */
    public static String readFileText(String strFile) {
        return TxtUtil.convertCodeAndGetText(strFile);
    }

    /**
     * 写文件到sdcard
     *
     * @param fileName
     * @param message
     */
    public static void writeFile(String fileName, String message) {
        TxtUtil.writeFileSdcard(fileName, message);
    }

    /**
     * 将指定路径的图片保存到手机相册
     *
     * @param context
     * @param strPath
     * @return
     */
    public static boolean savePictureToAlbum(Context context, String strPicture) {
        ContentResolver cr = context.getContentResolver();
        try {
            MediaStore.Images.Media.insertImage(cr, strPicture,
                    strPicture.substring(strPicture.lastIndexOf("/") + 1),
                    "this is a Photo");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 将bitmap图片保存到手机相册
     *
     * @param activity
     * @param bitmap
     */
    public static boolean saveBitmapToAlum(Activity act, Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        try {
            // 存储到相册
            ContentResolver cr = act.getContentResolver();
            MediaStore.Images.Media.insertImage(cr, bitmap, null, "IMG_"
                    + timeStamp + ".jpg");
        } catch (Exception e) {
            return false;
        }
        // 发送广播给系统刷新相册缓存图片
        act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                .parse("file://" + Environment.getExternalStorageDirectory())));
        return true;
    }

    /**
     * 自动保存图片到应用文件夹下面
     *
     * @param bitmap
     * @return
     */
    public static String saveBitmapToFile(Bitmap bitmap) {
        String strPath = MethodsFile.getAutoFileDirectory()
                + new SimpleDateFormat("yyyyMMdd_HHmmss") + ".png";
        boolean tf = false;
        try {
            tf = saveBitmapToFile(strPath, bitmap, 100);
        } catch (Exception e) {
        }
        if (!tf) {
            return null;
        }
        return strPath;
    }

    /**
     * 将bitmap对象保存到bitName文件中
     *
     * @param bitName  保存的文件(含路径)
     * @param mBitmap
     * @param mQuality
     */
    public static boolean saveBitmapToFile(String fileName, Bitmap mBitmap,
                                           int mQuality) throws IOException {
        if (mBitmap == null)
            return false;
        File f = new File(fileName);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            return false;
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, mQuality >= 100 ? 100
                : mQuality, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            return false;
        }
        try {
            fOut.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.RGB_565
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 异步上传文件
     *
     * @param strURL
     * @param strPath
     */
    public static void uploadFileAsynic(String strURL, String strPath,
                                        final UploadFileCallback callback) {
        System.out.println("上传路径" + strURL);
        final File fileTemp = new File(strPath);
        final String strUrl = strURL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(strUrl);
                    HttpURLConnection httpUrlConnection = (HttpURLConnection) url
                            .openConnection();
                    httpUrlConnection.setDoOutput(true);
                    httpUrlConnection.setDoInput(true);
                    httpUrlConnection.setRequestMethod("POST");
                    OutputStream os = httpUrlConnection.getOutputStream();
                    BufferedInputStream fis = new BufferedInputStream(
                            new FileInputStream(fileTemp));
                    int bufSize = 0;
                    byte[] buffer = new byte[1024];
                    while ((bufSize = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bufSize);
                    }
                    fis.close();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(httpUrlConnection
                                    .getInputStream()));
                    StringBuffer rtnBuffer = new StringBuffer("");
                    String str = null;
                    while ((str = reader.readLine()) != null) {
                        rtnBuffer.append(str);
                    }
                    callback.callback(true, rtnBuffer.toString());
                } catch (Exception e) {
                    callback.callback(false, "");
                }
            }
        }).start();
    }

    /**
     * 上传文件
     *
     * @param strURL
     * @param strPath
     * @return "-1"代表上传失败
     */
    /* 上传文件至Server的方法 */
    public static String uploadFile(String strURL, String strPath) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(strURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            /* 设置传送的method=POST */
            con.setRequestMethod("POST");
			/* setRequestProperty */
//            con.setConnectTimeout(240000);
//            con.setReadTimeout(240000);   //读取超时
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file1\";filename=\"" + strPath + "\"" + end);
            ds.writeBytes(end);
			/* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(new File(strPath));
			/* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
			/* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
            fStream.close();
            ds.flush();
            ds.close();
			/* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
			/* 关闭DataOutputStream */
            ds.close();
            return b.toString();
        } catch (Exception e) {
            return "-1";
        }
    }

    /**
     * 将图片变为圆形
     *
     * @param x
     * @param y
     * @param image
     * @param outerRadiusRat
     * @return
     */
    public static Bitmap createFramedPhoto(int x, int y, Bitmap image,
                                           float outerRadiusRat) {
        // 根据源文件新建一个darwable对象
        Drawable imageDrawable = new BitmapDrawable(image);

        // 新建一个新的输出图片
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(output);

        // 新建一个矩形
        RectF outerRect = new RectF(0, 0, x, y);

        // 产生一个白色的圆角矩形
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

        // 将源图片绘制到这个圆角矩形上 // 详解见http://lipeng88213.iteye.com/blog/1189452
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        paint.setTextSize(10);
        canvas.restore();
        return output;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 将图片变为星形
     *
     * @param x
     * @param y
     * @param image
     * @return
     */
    public static Bitmap createStarPhoto(int x, int y, Bitmap image) {
        // 根据源文件新建一个darwable对象
        Drawable imageDrawable = new BitmapDrawable(image);

        // 新建一个新的输出图片
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(output);

        // 新建一个矩形
        RectF outerRect = new RectF(0, 0, x, y);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        Path path = new Path();

        // 绘制三角形
        // path.moveTo(0, 0);
        // path.lineTo(320, 250);
        // path.lineTo(400, 0);

        // 绘制正无边形
        long tmpX, tmpY;
        path.moveTo(200, 200);// 此点为多边形的起点
        for (int i = 0; i <= 5; i++) {
            tmpX = (long) (200 + 200 * Math.sin((i * 72 + 36) * 2 * Math.PI
                    / 360));
            tmpY = (long) (200 + 200 * Math.cos((i * 72 + 36) * 2 * Math.PI
                    / 360));
            path.lineTo(tmpX, tmpY);
        }
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);
        // canvas.drawCircle(100, 100, 100, paint);

        // 将源图片绘制到这个圆角矩形上
        // 产生一个红色的圆角矩形

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();
        return output;
    }

    /**
     * 按正方形裁剪图片
     *
     * @param bitmap
     * @param newX
     * @param newY
     * @param newW
     * @param newH
     * @return
     */
    public static Bitmap cropImage(Bitmap bitmap, int newX, int newY, int newW,
                                   int newH) {

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        // newX、newY基于原图，取长方形左上角x、y坐标
        // newW、newH基于原图，裁剪的宽度和高度
        // 下面这句是关键
        return Bitmap.createBitmap(bitmap, newX, newY, newW, newH, null, false);
    }

    /**
     * 读取图片旋转的角度
     *
     * @param filename
     * @return
     */
    public static int readPictureDegree(String filename) {
        int rotate = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filename);
            int result = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotate;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return resizedBitmap;
    }

    // 将图片变成带圆边的圆形图片
    public static Bitmap getRoundBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        // 将图片变成圆角
        Bitmap roundBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas canvas = new Canvas(roundBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int len = (width > height) ? height : width;
        canvas.drawCircle(width / 2, height / 2, len / 2 - 8, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, len, len, true);
        canvas.drawBitmap(scaledBitmap, 0, 0, paint);
        // 将图片加圆边
        Bitmap outBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        canvas = new Canvas(outBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        canvas.drawCircle(width / 2, height / 2, len / 2 - 4, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
        canvas.drawBitmap(roundBitmap, 0, 0, paint);
        bitmap.recycle();
        bitmap = null;
        roundBitmap.recycle();
        roundBitmap = null;
        scaledBitmap.recycle();
        scaledBitmap = null;
        return outBitmap;
    }

    // 将图片变成带圆边的圆形图片
    public static Bitmap getRoundBitmap(Bitmap bitmap, int width, int height,
                                        int color) {
        if (bitmap == null) {
            return null;
        }
        // 将图片变成圆角
        Bitmap roundBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas canvas = new Canvas(roundBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int len = (width > height) ? height : width;
        canvas.drawCircle(width / 2, height / 2, len / 2 - 8, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, len, len, true);
        canvas.drawBitmap(scaledBitmap, 0, 0, paint);
        // 将图片加圆边
        Bitmap outBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        canvas = new Canvas(outBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawCircle(width / 2, height / 2, len / 2 - 4, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
        canvas.drawBitmap(roundBitmap, 0, 0, paint);
        bitmap.recycle();
        bitmap = null;
        roundBitmap.recycle();
        roundBitmap = null;
        scaledBitmap.recycle();
        scaledBitmap = null;
        return outBitmap;
    }

    /**
     * function:图片转圆角
     *
     * @param bitmap 需要转的bitmap
     * @param pixels 转圆角的弧度
     * @return 转圆角的bitmap
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.RGB_565);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return output;
    }

    /**
     * 屏幕截图
     *
     * @param activity
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity, int x, int y,
                                        int width, int height) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);
        // 获取屏幕长和高
        if (width == 0) {
            width = activity.getWindowManager().getDefaultDisplay().getWidth();
        }
        if (height == 0) {
            height = activity.getWindowManager().getDefaultDisplay()
                    .getHeight();
        }

        // 去掉标题栏
        Bitmap b = getScaledBitmap(b1, width, height - statusBarHeight, 0,
                statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * 给图片作高斯模糊处理
     *
     * @param sentBitmap
     * @param radius
     * @param canReuseInBitmap
     * @return
     */
    public static Bitmap doBlurWithBitmap(Bitmap sentBitmap, int radius,
                                          boolean canReuseInBitmap) {
        return FastBlur.doBlurJniBitMap(sentBitmap, radius, canReuseInBitmap);
    }

    /**
     * 给控件下面的背景图片作高斯模糊处理
     *
     * @param context
     * @param bkg
     * @param radius
     * @param view        控件
     * @param scaleFactor 默认1
     */
    @SuppressWarnings({"deprecation"})
    public static void doBlurForViewBkgBitmap(Context context, Bitmap bkg,
                                              int radius, View view, float scaleFactor) {
        if (scaleFactor < 0) {
            scaleFactor = 1;
        }
        if (radius <= 1) {
            radius = 2;
        }
        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
                / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = doBlurWithBitmap(overlay, radius, true);
        view.setBackgroundDrawable(new BitmapDrawable(context.getResources(),
                overlay));
    }

    /**
     * 给控件下面的背景图片作高斯模糊处理
     *
     * @param context
     * @param bkg
     * @param radius
     * @param view        控件
     * @param scaleFactor 默认1
     */
    @SuppressWarnings({"deprecation"})
    public static void doBlurForViewBkgBitmap(Context context, Bitmap bkg,
                                              int radius, View view, int viewWidth, int viewHeight, int viewLeft,
                                              int viewTop, float scaleFactor) {
        if (scaleFactor <= 0) {
            scaleFactor = 1;
        }
        if (radius <= 1) {
            radius = 2;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Config.RGB_565;
        opts.inDither = true;
        Bitmap overlay = Bitmap.createBitmap((int) (viewWidth / scaleFactor),
                (int) (viewHeight / scaleFactor), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-viewLeft / scaleFactor, -viewTop / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = doBlurWithBitmap(overlay, radius, true);
        view.setBackgroundDrawable(new BitmapDrawable(context.getResources(),
                overlay));
    }

    /**
     * Bitmap to Drawable
     *
     * @param bitmap
     * @param mcontext
     * @return
     */
    public static Drawable bitmapToDrawble(Bitmap bitmap, Context mcontext) {
        Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
        return drawable;
    }

    /**
     * 图片缩放到目标大小
     *
     * @param bitmap
     * @param destWidth
     * @param destHeight
     * @param startOriginX 默认0
     * @param startOriginY 默认0
     * @return
     */
    public static Bitmap getScaledBitmap(Bitmap bitmap, int destWidth,
                                         int destHeight, int startOriginX, int startOriginY) {
        Bitmap bpRtn = Bitmap.createBitmap(destWidth, destHeight,
                Bitmap.Config.RGB_565);
        try {
            Canvas mCanvas = new Canvas(bpRtn);
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mCanvas.drawBitmap(bitmap, new Rect(
                            startOriginX >= 0 ? startOriginX : 0,
                            startOriginY >= 0 ? startOriginY : 0, bitmap.getWidth(),
                            bitmap.getHeight()), new Rect(0, 0, destWidth, destHeight),
                    mPaint);
        } catch (Exception e) {
            bpRtn = null;
        }
        return bpRtn;
    }

    public static Bitmap getBitmapWithHorizontalMirror(Bitmap sourceBp) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Bitmap bpRtn = Bitmap.createBitmap(sourceBp.getWidth() * 2,
                sourceBp.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bpRtn);
        canvas.drawBitmap(sourceBp, 0, 0, mPaint);
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, sourceBp.getWidth() / 2,
                sourceBp.getHeight() / 2);// 前两个是xy变换，后两个是对称轴中心点
        matrix.postTranslate(sourceBp.getWidth(), 0);
        canvas.drawBitmap(sourceBp, matrix, mPaint);
        return bpRtn;
    }

    public static Bitmap getBitmapOnlyHorizontalMirror(Bitmap sourceBp) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Bitmap bpRtn = Bitmap.createBitmap(sourceBp.getWidth(),
                sourceBp.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bpRtn);
        Matrix m = new Matrix();
        m.postScale(-1, 1, sourceBp.getWidth() / 2, sourceBp.getHeight() / 2);// 前两个是xy变换，后两个是对称轴中心点
        canvas.drawBitmap(sourceBp, m, mPaint);
        return bpRtn;
    }

    public static Bitmap getBitmapWithVerticalMirror(Bitmap sourceBp) {
        Bitmap bpRtn = null;
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        bpRtn = Bitmap.createBitmap(sourceBp.getWidth(),
                sourceBp.getHeight() * 2, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bpRtn);
        canvas.drawBitmap(sourceBp, 0, 0, mPaint);
        Matrix matrix = new Matrix();
        matrix.postScale(1, -1, sourceBp.getWidth() / 2,
                sourceBp.getHeight() / 2);// 前两个是xy变换，后两个是对称轴中心点
        matrix.postTranslate(0, sourceBp.getHeight());
        canvas.drawBitmap(sourceBp, matrix, mPaint);
        return bpRtn;
    }

    public static Bitmap getBitmapOnlyVerticalMirror(Bitmap sourceBp) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Bitmap bpRtn = Bitmap.createBitmap(sourceBp.getWidth(),
                sourceBp.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bpRtn);
        Matrix m = new Matrix();
        m.postScale(1, -1, sourceBp.getWidth() / 2, sourceBp.getHeight() / 2);// 前两个是xy变换，后两个是对称轴中心点
        canvas.drawBitmap(sourceBp, m, mPaint);
        return bpRtn;
    }

}