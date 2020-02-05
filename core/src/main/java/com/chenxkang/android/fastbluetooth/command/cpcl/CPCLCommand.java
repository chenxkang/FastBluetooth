package com.chenxkang.android.fastbluetooth.command.cpcl;

public class CPCLCommand implements ICPCLCommand {

    public CPCLCommand() {
    }

    @Override
    public String getInitCommand(int offset, int height, int qty) {
        return "! " + offset + " " + 200 + " " + 200 + " " + (height - 12) + " " + qty + "\r\n";
    }

    @Override
    public String getCodePageCommand(String language) {
        return "COUNTRY " + language + "\r\n";
    }

    @Override
    public String getAlignCommand(String align) {
        return align + "\r\n";
    }

    @Override
    public String getAlignCommand(String align, int end) {
        return align + " " + end + "\r\n";
    }

    @Override
    public String getEnlargeCommand(int width, int height) {
        return "SETMAG " + width + " " + height + "\r\n";
    }

    @Override
    public String getBoldCommand(int bold) {
        return "!U1 " + "SETBOLD " + bold + "\r\n";
    }

    @Override
    public String getDensityCommand(int density) {
        return "CONTRAST " + density + "\r\n";
    }

    @Override
    public String getLineCommand(int x0, int y0, int x1, int y1, int thickness) {
        return "LINE " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + thickness + "\r\n";
    }

    @Override
    public String getReverseCommand(int x0, int y0, int x1, int y1, int width) {
        return "INVERSE-LINE " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + width + "\r\n";
    }

    @Override
    public String getBoxCommand(int x0, int y0, int x1, int y1, int thickness) {
        return "BOX " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + thickness + "\r\n";
    }

    @Override
    public String getUnderLineCommand(boolean underLine) {
        if (underLine) {
            return "UNDERLINE ON\r\n";
        }
        return "UNDERLINE OFF\r\n";
    }

    @Override
    public String getPageWidth(int width) {
        return "PW " + width + "\r\n";
    }

    @Override
    public String getTextCommand(String rotation, int x, int y, int font, int size, String data) {
        return rotation + " " + font + " " + size + " " + x + " " + y + " " + data + "\r\n";
    }

    @Override
    public String getImageCommand(int x, int y, int width, int height) {
        return "CG " + width + " " + height + " " + x + " " + y + " ";
    }

    @Override
    public String getBarcodeCommand(String rotation, String type, int x, int y, int wide, int ratio, int height, boolean underText, int font, int size, int offset, String data) {
        String command = rotation + " " + type + " " + wide + " " + ratio + " " + height + " " + x + " " + y + " " + data + "\r\n";
        if (underText) {
            command = "BARCODE-TEXT " + font + " " + size + " " + offset + "\r\n" + command + "BARCODE-TEXT OFF\r\n";
        }
        return command;
    }

    @Override
    public String getBarcodeCommand(String rotation, String type, float x, int y, int wide, int ratio, int height, boolean underText, int font, int size, int offset, String data) {
        String command = rotation + " " + type + " " + wide + " " + ratio + " " + height + " " + x + " " + y + " " + data + "\r\n";
        if (underText) {
            command = "BARCODE-TEXT " + font + " " + size + " " + offset + "\r\n" + command + "BARCODE-TEXT OFF\r\n";
        }
        return command;
    }

    @Override
    public String getQRCodeCommand(String rotation, int x, int y, int mode, int width, String data) {
        return rotation + " QR " + x + " " + y + " M " + 2 + " U " + width + "\r\nMA," + data + "\r\nENDQR\r\n";
    }

    @Override
    public String getSelfCommand() {
        return "29, 40, 65, 2, 0, 0, 2";
    }

    @Override
    public String getFormCommand() {
        return "FORM\r\n";
    }

    @Override
    public String getEndPrintCommand() {
        return "PRINT\r\n";
    }
}
