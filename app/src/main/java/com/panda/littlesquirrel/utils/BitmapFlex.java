package com.panda.littlesquirrel.utils;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by jinjing on 2018/11/12.
 */

public class BitmapFlex {
    /**
     * 等间隔采样的图像缩放
     * @param bitmap     要缩放的图像对象
     * @param dstWidth   缩放后图像的宽
     * @param dstHeight 缩放后图像的高
     * @return 返回处理后的图像对象
     */
    public static Bitmap flex(Bitmap bitmap, int dstWidth, int dstHeight) {
        float wScale = (float) dstWidth / bitmap.getWidth();
        float hScale = (float) dstHeight / bitmap.getHeight();
        return flex(bitmap, wScale, hScale);
    }
    public static byte[] rgb2YUV(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;

                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                yuv[i * width + j] = (byte) y;
//                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
//                yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }

    /**
     * 等间隔采样的图像缩放
     * @param bitmap 要缩放的bitap对象
     * @param wScale 要缩放的横列（宽）比列
     * @param hScale 要缩放的纵行（高）比列
     * @return 返回处理后的图像对象
     */
    public static Bitmap flex(Bitmap bitmap, float wScale, float hScale) {
        if (wScale <= 0 || hScale <= 0){
            return null;
        }
        float ii = 1 / wScale;    //采样的行间距
        float jj = 1 / hScale; //采样的列间距

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int dstWidth = (int) (wScale * width);
        int dstHeight = (int) (hScale * height);

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] dstPixels = new int[dstWidth * dstHeight];

        for (int j = 0; j < dstHeight; j++) {
            for (int i = 0; i < dstWidth; i++) {
                dstPixels[j * dstWidth + i] = pixels[(int) (jj * j) * width + (int) (ii * i)];
            }
        }
        System.out.println((int) ((dstWidth - 1) * ii));
        Log.d(">>>",""+"dstPixels:"+dstWidth+" x "+dstHeight);

        Bitmap outBitmap = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
        outBitmap.setPixels(dstPixels, 0, dstWidth, 0, 0, dstWidth, dstHeight);

        return outBitmap;
    }

}
