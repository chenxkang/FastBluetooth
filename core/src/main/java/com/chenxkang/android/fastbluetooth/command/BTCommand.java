package com.chenxkang.android.fastbluetooth.command;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chenxkang.android.fastbluetooth.command.cpcl.CPCLCommand;
import com.chenxkang.android.fastbluetooth.command.cpcl.ICPCLCommand;
import com.chenxkang.android.fastbluetooth.command.tspl.ITSPLCommand;
import com.chenxkang.android.fastbluetooth.command.tspl.TSPLCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * author: chenxkang
 * time  : 2020-01-20
 * desc  :
 */
public class BTCommand {

    static volatile BTCommand commandInstance;

    private static String mCommand;
    private static ICPCLCommand cpclCommand;
    private static ITSPLCommand tsplCommand;
    private static int mOffset = 0;
    private static int enlargeWidth = 1, enlargeHeight = 1;

    private static List<byte[]> commands;

    public BTCommand() {

    }

    public static BTCommand getDefault() {
        if (commandInstance == null) {
            synchronized (BTCommand.class) {
                if (commandInstance == null) {
                    commandInstance = new BTCommand();
                }
            }
        }
        return commandInstance;
    }

    public BTCommand init(String command, int width, int height) throws Exception {
        return init(command, 0, width, height);
    }

    public BTCommand init(String command, int offset, int width, int height) throws Exception {
        onCheckCommand(command);
        commands = new ArrayList<>();
        if (command.equals(CPCL)) {
            commands.add(cpclCommand.getInitCommand(offset, height * 8, 1).getBytes(LANGUAGE_GB));
        }

        if (command.equals(TSPL)) {
            mOffset = offset;
            commands.add(tsplCommand.getInitCommand(width, height).getBytes(LANGUAGE_GB));
            commands.add(tsplCommand.getCLSCommand().getBytes(LANGUAGE_GB));
        }
        return this;
    }

    public BTCommand printLine(int x0, int y0, int x1, int y1, int thickness) throws Exception{
        if (mCommand.equals(CPCL)) {
            commands.add(cpclCommand.getLineCommand(x0, y0, x1, y1, thickness).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            int width = 0;
            int height = 0;
            if (x1 > x0 && y0 == y1 && thickness > 0) {
                width = x1 - x0;
                height = thickness;
            } else if (x0 == x1 && y1 > y0 && thickness > 0) {
                width = thickness;
                height = y1 - y0;
            }
            commands.add(tsplCommand.getLineCommand(x0 + mOffset, y0, width, height).getBytes(LANGUAGE_GB));
        }
        return this;
    }

    public BTCommand printText(@NonNull String rotation, int x, int y, int font, String data) throws Exception {
        if (mCommand.equals(CPCL)) {
            if (font == TEXT_SIZE_16) {
                font = 20;
            } else {
                font = 8;
            }

            if (rotation.equals(TEXT)) {
                rotation = "TEXT";
            } else if (rotation.equals(TEXT90)) {
                rotation = "TEXT270";
            } else if (rotation.equals(TEXT180)) {
                rotation = "TEXT180";
            } else if (rotation.equals(TEXT270)) {
                rotation = "TEXT90";
            }
            commands.add(cpclCommand.getTextCommand(rotation, x, y, font, 0, data).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            if (font == TEXT_SIZE_16) {
                font = 1;
            } else {
                font = 8;
            }
            if (rotation.equals(TEXT_WHITE)) {
                rotation = TEXT;
                commands.add(tsplCommand.getTextCommand(rotation, x, y, font, enlargeWidth, enlargeHeight, getAlign(null), data).getBytes(LANGUAGE_GB));
                commands.add(tsplCommand.getReverseCommand(x, y, getReverseWidth(font, enlargeWidth, data), getReverseHeight(font, enlargeHeight)).getBytes(LANGUAGE_GB));
            } else {
                commands.add(tsplCommand.getTextCommand(rotation, x, y, font, enlargeWidth, enlargeHeight, getAlign(null), data).getBytes(LANGUAGE_GB));
            }
        }
        return this;
    }

    public BTCommand printText(@NonNull String rotation, int x, int y, int font, boolean bold, String data) throws Exception {
        if (bold) {
            if (mCommand.equals(CPCL)) {
                setBold(2);
            } else if (mCommand.equals(TSPL)) {
                setBold(1);
            }
        }
        printText(rotation, x, y, font, data);
        setBold(0);
        return this;
    }

    public BTCommand printText(@NonNull String rotation, int x, int y, int font, int density, String data) throws Exception {
        if (mCommand.equals(CPCL)) {
            setDensity(density);
        } else if (mCommand.equals(TSPL)) {
            setDensity(density * 3);
        }
        printText(rotation, x, y, font, data);
        setDensity(0);
        return this;
    }

    public BTCommand printText(@NonNull String rotation, int x, int y, int font, boolean bold, int density, String data) throws Exception {
        if (bold) {
            if (mCommand.equals(CPCL)) {
                setBold(2);
            } else if (mCommand.equals(TSPL)) {
                setBold(1);
            }
        }

        if (mCommand.equals(CPCL)) {
            setDensity(density);
        } else if (mCommand.equals(TSPL)) {
            setDensity(density * 3);
        }

        printText(rotation, x, y, font, data);
        setBold(0);
        setDensity(0);
        return this;
    }

    public BTCommand printText(@NonNull String rotation, int x, int y, int font, int width, int height, String data) throws Exception {
        if (width > 1 || height > 1) {
            setEnlarge(width, height);
        }
        printText(rotation, x, y, font, data);
        setEnlarge(1, 1);
        return this;
    }

    public BTCommand printText(@NonNull String rotation, int x, int y, int font, int width, int height, boolean bold, String data) throws Exception {
        if (width > 1 || height > 1) {
            setEnlarge(width, height);
        }

        if (bold) {
            if (mCommand.equals(CPCL)) {
                setBold(2);
            } else if (mCommand.equals(TSPL)) {
                setBold(1);
            }
        }

        printText(rotation, x, y, font, data);

        setEnlarge(1, 1);
        setBold(0);
        return this;
    }

    public BTCommand printText(@NonNull String rotation, int x, int y, int font, int width, int height, int density, String data) throws Exception {
        if (width > 1 || height > 1) {
            setEnlarge(width, height);
        }

        if (mCommand.equals(CPCL)) {
            setDensity(density);
        } else if (mCommand.equals(TSPL)) {
            setDensity(density * 3);
        }

        printText(rotation, x, y, font, data);

        setEnlarge(1, 1);
        setDensity(0);
        return this;
    }

    public BTCommand printText(@NonNull String rotation, int x, int y, int font, int width, int height, boolean bold, int density, String data) throws Exception {
        if (width > 1 || height > 1) {
            setEnlarge(width, height);
        }

        if (bold) {
            if (mCommand.equals(CPCL)) {
                setBold(2);
            } else if (mCommand.equals(TSPL)) {
                setBold(1);
            }
        }

        if (mCommand.equals(CPCL)) {
            setDensity(density);
        } else if (mCommand.equals(TSPL)) {
            setDensity(density * 3);
        }

        printText(rotation, x, y, font, data);

        setEnlarge(1, 1);
        setDensity(0);
        setBold(0);
        return this;
    }

    private BTCommand printReverse(int x, int y, int width, int height) throws Exception {
        if (mCommand.equals(CPCL)) {
            int x1 = 0, y1 = 0;
            if (width > 0) {
                x1 = x + width;
            }
            if (height > 0) {
                y1 = y + height;
            }
            commands.add(cpclCommand.getReverseCommand(x, y, x1, y1, height).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            commands.add(tsplCommand.getReverseCommand(x, y, width, height).getBytes(LANGUAGE_GB));
        }
        return this;
    }

    public BTCommand printBarcode(String rotation, String type, int x, int y, int narrow, int ratio, int height, boolean underText, String data) throws Exception {
        if (mCommand.equals(CPCL)) {
            if (rotation.equals(BARCODE)) {
                rotation = "BARCODE";
            }
            commands.add(cpclCommand.getBarcodeCommand(rotation, type, x, y, narrow, ratio, height, underText, 7, 2, 5, data).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            if (TextUtils.equals(rotation, VBARCODE)) {
                rotation = BARCODE270;
            }
            commands.add(tsplCommand.getBarcodeCommand(rotation, type, x, y, narrow, getWide(narrow, ratio), height, underText ? 1 : 0, data).getBytes(LANGUAGE_GB));
        }
        return this;
    }

    public BTCommand printQRCode(String rotation, int x, int y, int width, String data) throws Exception {
        if (mCommand.equals(CPCL)) {
            if (rotation.equals(BARCODE)) {
                rotation = "BARCODE";
            }
            commands.add(cpclCommand.getQRCodeCommand(rotation, x, y, 2, width, data).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            printQRCode(rotation, x, y, LEVEL_H, MODE_AUTO, width, data);
        }
        return this;
    }

    public BTCommand printQRCode(String rotation, int x, int y, String level, int width, String data) throws Exception {
        if (mCommand.equals(CPCL)) {
            if (rotation.equals(BARCODE)) {
                rotation = "BARCODE";
            }
            commands.add(cpclCommand.getQRCodeCommand(rotation, x, y, 2, width, data).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            printQRCode(rotation, x, y, level, MODE_AUTO, width, data);
        }
        return this;
    }

    public BTCommand printQRCode(String rotation, int x, int y, String level, String mode, int width, String data) throws Exception {
        if (mCommand.equals(TSPL)) {
            if (TextUtils.equals(rotation, VBARCODE)) {
                rotation = BARCODE270;
            }
            commands.add(tsplCommand.getQRCodeCommand(rotation, x, y, level, width, mode, data).getBytes(LANGUAGE_GB));
        }
        return this;
    }

    public BTCommand printBox(int x0, int y0, int x1, int y1, int thickness) throws Exception {
        if (mCommand.equals(CPCL)) {
            commands.add(cpclCommand.getBoxCommand(x0, y0, x1, y1, thickness).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            commands.add(tsplCommand.getBoxCommand(x0, y0, x1, y1, thickness).getBytes(LANGUAGE_GB));
        }
        return this;
    }

    public BTCommand printImage(int x, int y, Bitmap bitmap) throws Exception {
        return printImage(x, y, bitmap, 0, 0);
    }

    public BTCommand printImage(int x, int y, Bitmap bitmap, int width, int height) throws Exception {
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        Bitmap newBitmap;
        if (width > 0 && height > 0) {
            float scaleWidth = ((float) width) / bWidth;
            float scaleHeight = ((float) height) / bHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bWidth, bHeight, matrix, true);
        } else {
            newBitmap = bitmap;
        }

        int newWidth;
        if (newBitmap.getWidth() % 8 == 0) {
            newWidth = newBitmap.getWidth() / 8;
        } else {
            newWidth = newBitmap.getWidth() / 8 + 1;
        }

        int newHeight = newBitmap.getHeight();
        if (newWidth > 999 | newHeight > 65535) {
            throw new IllegalArgumentException("The image is too big to be used.");
        }

        if (mCommand.equals(CPCL)) {
            commands.add(cpclCommand.getImageCommand(x, y, newWidth, newHeight).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            commands.add(tsplCommand.getImageCommand(x, y, newWidth, newHeight).getBytes(LANGUAGE_GB));
        }

        byte[] bytes = bitmap2bytes(newBitmap, 0);
        if (mCommand.equals(TSPL)) {
            for (int i = 0; i < bytes.length; ++i) {
                bytes[i] = (byte) (~bytes[i]);
            }
        }
        commands.add(bytes);
        commands.add(("\r\n").getBytes());
        return this;
    }

    public List<byte[]> commit() throws Exception {
        if (mCommand.equals(CPCL)) {
            commands.add(cpclCommand.getFormCommand().getBytes(LANGUAGE_GB));
            commands.add(cpclCommand.getEndPrintCommand().getBytes(LANGUAGE_GB));
        }

        if (mCommand.equals(TSPL)) {
            commands.add(tsplCommand.getEndPrintCommand(1, 1).getBytes(LANGUAGE_GB));
        }
        return commands;
    }

    private int getReverseHeight(int font, int enlargeHeight) {
        return (font != 1 ? 24 : 16) * enlargeHeight;
    }

    private int getReverseWidth(int font, int enlargeWidth, String data) {
        int width = 0;
        if (!TextUtils.isEmpty(data)) {
            width = (font != 1 ? 24 : 16) * enlargeWidth * data.length();
            char[] ca = data.toCharArray();
            for (char c : ca) {
                if (isNumber(c) || isLetter(c)) {
                    width = width - (font != 1 ? 12 : 8) * enlargeWidth;
                }
            }
        }
        return width;
    }

    private boolean isLetter(char chr) {
        return (chr >= 65 && chr <= 90) || (chr >= 97 && chr <= 122);
    }

    private boolean isNumber(char c) {
        return c >= 48 && c <= 57;
    }

    private static int getWide(int narrow, int ratio) {
        if (ratio == 1)
            return narrow;

        if (ratio == 2)
            return narrow * 2;

        if (ratio == 3)
            return narrow * 3;

        return narrow;
    }

    private static void onCheckCommand(String command) {
        if (TextUtils.isEmpty(command)) {
            throw new IllegalArgumentException("The command should not be null.");
        }

        if (!command.equals(CPCL) && !command.equals(TSPL)) {
            throw new IllegalArgumentException("The command is not accepted.");
        }

        mCommand = command;

        if (command.equals(CPCL)) {
            if (cpclCommand == null) {
                cpclCommand = new CPCLCommand();
            }
        }

        if (command.equals(TSPL)) {
            if (tsplCommand == null) {
                tsplCommand = new TSPLCommand();
            }
        }
    }

    private String getAlign(String align) {
        if (TextUtils.equals(align, LEFT)) {
            return "1";
        }

        if (TextUtils.equals(align, CENTER)) {
            return "2";
        }

        if (TextUtils.equals(align, RIGHT)) {
            return "3";
        }
        return align;
    }

    private void setEnlarge(int width, int height) throws Exception {
        enlargeWidth = width;
        enlargeHeight = height;
        if (mCommand.equals(CPCL)) {
            commands.add(cpclCommand.getEnlargeCommand(width, height).getBytes(LANGUAGE_GB));
        }
    }

    private void setBold(int bold) throws Exception {
        if (mCommand.equals(TSPL) && bold != 0) {
            bold = 1;
        }

        if (mCommand.equals(CPCL)) {
            commands.add(cpclCommand.getBoldCommand(bold).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            commands.add(tsplCommand.getBoldCommand(bold).getBytes(LANGUAGE_GB));
        }
    }

    private void setDensity(int density) throws Exception {
        if (mCommand.equals(CPCL)) {
            commands.add(cpclCommand.getDensityCommand(density).getBytes(LANGUAGE_GB));
        } else if (mCommand.equals(TSPL)) {
            commands.add(tsplCommand.getDensityCommand(density).getBytes(LANGUAGE_GB));
        }
    }

    private static byte[] bitmap2bytes(Bitmap bitmap, int type) {
        ImageDataCore dataCore;
        (dataCore = new ImageDataCore()).halftoneMode = type;
        return dataCore.printDataFormat(bitmap);
    }

    public static final String CPCL = "CPCL";
    public static final String TSPL = "TSPL";

    public static final String CENTER = "CENTER";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";

    public static final String CHINA = "CHINA";
    public static final String USA = "USA";
    public static final String CP874 = "CP874";

    public static final String LANGUAGE_GB = "gb2312";

    public static final String TEXT = "0";
    public static final String TEXT90 = "90";
    public static final String TEXT180 = "180";
    public static final String TEXT270 = "270";
    public static final String TEXT_WHITE = "TR";

    public static final int TEXT_SIZE_16 = 16;
    public static final int TEXT_SIZE_24 = 24;

    public static final String BARCODE = "0";
    public static final String BARCODE90 = "90";
    public static final String BARCODE180 = "180";
    public static final String BARCODE270 = "270";
    public static final String VBARCODE = "VBARCODE";

    public static final String LEFT_TO_RIGHT = "LEFT_TO_RIGHT";
    public static final String RIGHT_TO_LEFT = "RIGHT_TO_LEFT";

    public static String UPCA = "UPCA";
    public static String UPCA2 = "UPCA2";
    public static String UPCA5 = "UPCA5";
    public static String UPCE = "UPCE";
    public static String UPCE2 = "UPCE2";
    public static String UPCE5 = "UPCE5";
    public static String EAN13 = "EAN13";
    public static String EAN132 = "EAN132";
    public static String EAN135 = "EAN135";
    public static String EAN8 = "EAN8";
    public static String EAN82 = "EAN82";
    public static String EAN85 = "EAN85";
    public static String CODE39 = "39";
    public static String CODE39C = "39C";
    public static String F39 = "F39";
    public static String F39C = "F39C";
    public static String CODE93 = "93";
    public static String I2OF5 = "I2OF5";
    public static String I2OF5C = "I2OF5C";
    public static String I2OF5G = "I2OF5G";
    public static String CODE128 = "128";
    public static String UCCEAN128 = "UCCEAN128";
    public static String CODABAR = "CODABAR";
    public static String CODABAR16 = "CODABAR16";
    public static String MSI = "MSI";
    public static String MSI10 = "MSI10";
    public static String MSI1010 = "MSI1010";
    public static String MSI1110 = "MSI1110";
    public static String POSTNET = "POSTNET";
    public static String FIM = "FIM";

    public static String CODE128M = "128M";
    public static String EAN128 = "EAN128";
    public static String ITF14 = "ITF14";

    public static final String LEVEL_L = "L";// 7%
    public static final String LEVEL_M = "M";// 15%
    public static final String LEVEL_Q = "Q";// 25%
    public static final String LEVEL_H = "H";// 30%

    public static final String MODE_AUTO = "A";// 自动模式
    public static final String MODE_MANUAL = "M";// 手动模式
}
