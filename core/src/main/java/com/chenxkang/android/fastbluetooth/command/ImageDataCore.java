package com.chenxkang.android.fastbluetooth.command;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImageDataCore {

    public int bitmapWidth = 0;
    public int printDataHeight = 0;
    public int halftoneMode = 1;

    public ImageDataCore() {
    }

    public byte[] printDataFormat(Bitmap bitmap) {
        byte[] bytes = null;

        try {
            if (this.halftoneMode == 0) {
                bytes = this.binaryzation(bitmap);
            } else if (this.halftoneMode == 1) {
                bytes = this.halftone(bitmap);
            } else if (this.halftoneMode == 2) {
                bytes = this.polymerizate(bitmap);
            }

            return bytes;
        } catch (Exception var11) {
            var11.printStackTrace();
            return null;
        }
    }

    // 聚合算法
    private byte[] polymerizate(Bitmap bitmap) {
        byte[] bytes;
        int var3 = bitmap.getWidth();
        int var4 = bitmap.getHeight();
        this.printDataHeight = var4;
        byte[] var12 = new byte[(var3 % 8 == 0 ? var3 / 8 : var3 / 8 + 1) * this.printDataHeight];
        int[] var5 = new int[var3 * var4];
        bitmap.getPixels(var5, 0, var3, 0, 0, var3, var4);

        int var6;
        for (int var14 = 0; var14 < var5.length; ++var14) {
            var6 = var5[var14];
            var5[var14] = 255 - (255 & (byte) ((int) ((double) Color.red(var6) * 0.29891D + (double) Color.green(var6) * 0.58661D + (double) Color.blue(var6) * 0.11448D)));
        }

        bytes = new byte[]{24, 10, 12, 26, 35, 47, 49, 37, 8, 0, 2, 14, 45, 59, 61, 51, 22, 6, 4, 16, 43, 57, 63, 53, 30, 20, 18, 28, 33, 41, 55, 39, 34, 46, 48, 36, 25, 11, 13, 27, 44, 58, 60, 50, 9, 1, 3, 15, 42, 56, 62, 52, 23, 7, 5, 17, 32, 40, 54, 38, 31, 21, 19, 29};
        var6 = (var3 + 7) / 8;
        boolean var7 = false;

        for (int var9 = 0; var9 < var4; ++var9) {
            int var15 = var6 * var9;

            for (int var8 = 0; var8 < var3; ++var15) {
                int var10 = var9 * var3;
                var12[var15] = (byte) (var12[var15] | (var5[var10 + var8] >> 2 > bytes[(var9 & 7) * 8 + (var8 & 7)] ? 128 : 0));
                ++var8;
                var12[var15] = (byte) (var12[var15] | (var5[var10 + var8] >> 2 > bytes[(var9 & 7) * 8 + (var8 & 7)] ? 64 : 0));
                ++var8;
                var12[var15] = (byte) (var12[var15] | (var5[var10 + var8] >> 2 > bytes[(var9 & 7) * 8 + (var8 & 7)] ? 32 : 0));
                ++var8;
                var12[var15] = (byte) (var12[var15] | (var5[var10 + var8] >> 2 > bytes[(var9 & 7) * 8 + (var8 & 7)] ? 16 : 0));
                ++var8;
                var12[var15] = (byte) (var12[var15] | (var5[var10 + var8] >> 2 > bytes[(var9 & 7) * 8 + (var8 & 7)] ? 8 : 0));
                ++var8;
                var12[var15] = (byte) (var12[var15] | (var5[var10 + var8] >> 2 > bytes[(var9 & 7) * 8 + (var8 & 7)] ? 4 : 0));
                ++var8;
                var12[var15] = (byte) (var12[var15] | (var5[var10 + var8] >> 2 > bytes[(var9 & 7) * 8 + (var8 & 7)] ? 2 : 0));
                ++var8;
                var12[var15] = (byte) (var12[var15] | (var5[var10 + var8] >> 2 > bytes[(var9 & 7) * 8 + (var8 & 7)] ? 1 : 0));
                ++var8;
            }
        }
        return var12;
    }

    // 半色调算法
    private byte[] halftone(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int var8 = 0;
        int newWidth = width + 7 >> 3;

        try {
            this.printDataHeight = height;
            this.bitmapWidth = newWidth;
            int oldArea = width * height;
            int newArea = newWidth * height;
            int[] oldPixels = new int[oldArea];
            bitmap.getPixels(oldPixels, 0, width, 0, 0, width, height);

            int pixel;
            for (pixel = 0; pixel < oldArea; ++pixel) {
                int var14 = oldPixels[pixel];
                oldPixels[var8++] = 255 & (byte) ((int) ((double) Color.red(var14) * 0.29891D + (double) Color.green(var14) * 0.58661D + (double) Color.blue(var14) * 0.11448D));
            }

            for (pixel = 0; pixel < height; ++pixel) {
                var8 = pixel * width;

                for (oldArea = 0; oldArea < width; ++oldArea) {
                    float var15;
                    if (oldPixels[var8] > 128) {
                        var15 = (float) (oldPixels[var8] - 255);
                        oldPixels[var8] = 255;
                    } else {
                        var15 = (float) (oldPixels[var8] - 0);
                        oldPixels[var8] = 0;
                    }

                    if (oldArea < width - 1) {
                        oldPixels[var8 + 1] += (int) (0.4375D * (double) var15);
                    }

                    if (pixel < height - 1) {
                        if (oldArea > 1) {
                            oldPixels[var8 + width - 1] += (int) (0.1875D * (double) var15);
                        }

                        oldPixels[var8 + width] += (int) (0.3125D * (double) var15);
                        if (oldArea < width - 1) {
                            oldPixels[var8 + width + 1] += (int) (0.0625D * (double) var15);
                        }
                    }

                    ++var8;
                }
            }

            byte[] var21 = new byte[newArea];

            label64:
            for (pixel = 0; pixel < height; ++pixel) {
                var8 = pixel * width;
                int var20 = pixel * newWidth;
                newArea = 0;
                oldArea = 0;

                while (true) {
                    int var19;
                    do {
                        if (oldArea >= width) {
                            continue label64;
                        }

                        var19 = oldArea % 8;
                        if (oldPixels[var8++] <= 128) {
                            newArea |= 128 >> var19;
                        }

                        ++oldArea;
                    } while (var19 != 7 && oldArea != width);

                    var21[var20++] = (byte) newArea;
                    newArea = 0;
                }
            }

            return var21;
        } catch (Exception var13) {
            var13.printStackTrace();
            return null;
        }
    }

    // 二值化算法
    private byte[] binaryzation(Bitmap bitmap) {
        int y = 0;
        int position = 0;

        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            this.printDataHeight = height;
            int newWidth = width % 8 == 0 ? width : (width / 8 + 1) * 8;
            this.bitmapWidth = newWidth / 8;
            byte[] bytes = new byte[newWidth = height * this.bitmapWidth];

            for (int i = 0; i < newWidth; ++i) {
                bytes[i] = 0;
            }

            while (y < height) {
                int[] pixels = new int[width];
                bitmap.getPixels(pixels, 0, width, 0, y, width, 1);
                int number = 0;

                for (int j = 0; j < width; ++j) {
                    ++number;
                    int color = pixels[j];
                    if (number > 8) {
                        number = 1;
                        ++position;
                    }

                    if (color != -1) {
                        newWidth = 1 << 8 - number;
                        int redComponent = Color.red(color);
                        int greenComponent = Color.green(color);
                        int blueComponent = Color.blue(color);
                        if ((redComponent + greenComponent + blueComponent) / 3 < 128) {
                            bytes[position] = (byte) (bytes[position] | newWidth);
                        }
                    }
                }

                position = this.bitmapWidth * (y + 1);
                ++y;
            }

            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
