package com.panda.littlesquirrel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Base64;

import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * @author King_wangyao
 * @version 1.0.0
 * @description Bitmap转Base64或者Base64转Bitmap公共类
 * @date 2014-04-23
 */
public class BitmapUtil {

    /**
     * Bitmap转Base64
     *
     * @param bitmap
     * @return
     */
    public static String getBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String getBase64(String filepath) {
        //File file=new File(filepath);

        return Base64.encodeToString(getBytes(filepath), Base64.DEFAULT);

    }

    ;

    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * Base64转Bitmap
     *
     * @param base64
     * @return
     */
    public static Bitmap getBase64ToBitmap(String base64) {
        byte[] bitmapArray = Base64.decode(base64, 0);
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /**
     * 保存压缩后的图片目录
     */
    public static File getSmallDir() {
        File dir = new File(Environment.getExternalStorageDirectory(), getSmallName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static Bitmap getSmallerBitmap(Bitmap bitmap) {
        int size = bitmap.getWidth() * bitmap.getHeight() / 160000;
        if (size <= 1) return bitmap; // 如果小于
        else {
            Matrix matrix = new Matrix();
            matrix.postScale((float) (1 / Math.sqrt(size)), (float) (1 / Math.sqrt(size)));
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return resizeBitmap;
        }
    }

    /**
     * 绘制文字到右上方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param
     * @param
     * @param
     * @param
     * @return
     */
//    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text,
//                                              int size, int color, int paddingLeft, int paddingBottom) {
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(color);
//        paint.setTextSize(dp2px(context, size));
//        Rect bounds = new Rect();
//        paint.getTextBounds(text, 0, text.length(), bounds);
//        return drawTextToBitmap(context, bitmap, text, paint, bounds,
//                dp2px(context, paddingLeft),
//                bitmap.getHeight() + dp2px(context, paddingBottom));
//    }


    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /**
     * @param source
     * @param text
     * @return
     */
    public static Bitmap getTextBitmap(Bitmap source, String text,int color) {

/**图片加上配文后生成的新图片*/
        Bitmap newBitmap;
/**配文的颜色*/
        int textColor = Color.BLACK;
/**配文的字体大小*/
        float textSize = 36;
/**图片的宽度*/
        int bitmapWidth;
/**图片的高度*/
        int bitmapHeight;
/**画图片的画笔*/
        Paint bitmapPaint = null;
/**画文字的画笔*/
        Paint textPaint = null;
        Paint blackPaint=null;
/**配文与图片间的距离*/
        float padding = 40;
/**配文行与行之间的距离*/
        float linePadding = 5;
        bitmapWidth = source.getWidth();
        bitmapHeight = source.getHeight();

//一行可以显示文字的个数
        int lineTextCount = (int) ((source.getWidth() - 10) / textSize);
//一共要把文字分为几行
        int line = (int) Math.ceil(Double.valueOf(text.length()) / Double.valueOf(lineTextCount));

//新创建一个新图片比源图片多出一部分，后续用来与文字叠加用
        newBitmap = Bitmap.createBitmap(bitmapWidth,
                (int) (bitmapHeight + padding + textSize * line + linePadding * line), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);
//把图片画上来
        setBitmapBorder(canvas);
        canvas.drawBitmap(source, 0, 0, bitmapPaint);
        textPaint = new Paint();
        blackPaint=new Paint();
        //bitmapPaint = new Paint();
//在图片下边画一个白色矩形块用来放文字，防止文字是透明背景，在有些情况下保存到本地后看不出来
        textPaint.setColor(Color.WHITE);

        canvas.drawRect(0, source.getHeight(), source.getWidth(),
                source.getHeight() + padding + textSize * line + linePadding * line, textPaint);

//把文字画上来
        blackPaint.setColor(textColor);
        blackPaint.setTextSize(textSize);
        blackPaint.setAntiAlias(true);
        blackPaint.setTypeface(Typeface.DEFAULT_BOLD);
        blackPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(color);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setStyle(Paint.Style.FILL);
        Rect bounds = new Rect();
//开启循环直到画完所有行的文字
        for (int i = 0; i < line; i++) {
            String s;
            if (i == line - 1) {//如果是最后一行，则结束位置就是文字的长度，别下标越界哦
                s = text.substring(i * lineTextCount, text.length());
                Logger.e("s--->"+s);

            } else {//不是最后一行
                s = text.substring(i * lineTextCount, (i + 1) * lineTextCount);
                Logger.e("s+1--->"+s);
            }
            //获取文字的字宽高以便把文字与图片中心对齐
            textPaint.getTextBounds(s, 0, s.length(), bounds);
            //画文字的时候高度需要注意文字大小以及文字行间距
            if(s.contains("费率")){

                canvas.drawText(s.substring(0,3), s.substring(0,3).length()+bounds.width() / 4 ,
                        source.getHeight() + padding + i * textSize + i * linePadding + bounds.height() / 2, blackPaint);
                canvas.drawText(s.substring(3,s.length()), (source.getWidth() / 2 - bounds.width() / 2)+bounds.width() / 4,
                        source.getHeight() + padding + i * textSize + i * linePadding + bounds.height() / 2, textPaint);


            }else{
                canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
                        source.getHeight() + padding + i * textSize + i * linePadding + bounds.height() / 2, textPaint);
            }

        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return newBitmap;
    }
    /**
     * 给bitmap设置边框
     * @param canvas
     */
    private static void setBitmapBorder(Canvas canvas){
        Rect rect = canvas.getClipBounds();
        Paint paint = new Paint();
        //设置边框颜色
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        //设置边框宽度
        paint.setStrokeWidth(100);
        canvas.drawRect(rect, paint);
    }
    /**
     * 拼接图片
     * @param bit1
     * @param bit2
     * @return 返回拼接后的Bitmap
     */
    private Bitmap newBitmap(Bitmap bit1,Bitmap bit2){
        int width = bit1.getWidth();
        int height = bit1.getHeight() + bit2.getHeight();
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bit1, 0, 0, null);
        canvas.drawBitmap(bit2, 0, bit1.getHeight(), null);
        //将canvas传递进去并设置其边框
        setBitmapBorder(canvas);
        return bitmap;
    }
    /**
     * 压缩后文件夹名字
     *
     * @return
     */
    private static String getSmallName() {
        // TODO Auto-generated method stub
        return "lssend";
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于50kb，大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        try {
            OutputStream os = new FileOutputStream(file);
            BufferedInputStream is = new BufferedInputStream(isBm);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
                os.flush();
            }
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImageSDK(Bitmap image, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 10) { // 循环判断如果压缩后图片是否大于800b，大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        try {
            OutputStream os = new FileOutputStream(file);
            BufferedInputStream is = new BufferedInputStream(isBm);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
                os.flush();
            }
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为1280x720 1920x1080
        float hh = 800f;// 这里设置高度为800f
        float ww = 400f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }

        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     *
     * @param image
     * @return
     */
    public static Bitmap compressBitmap(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M，进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 960.0f;// 这里设置高度为800f
        float ww = 960.0f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        //return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        return bitmap;
    }


}
