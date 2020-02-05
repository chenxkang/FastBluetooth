package com.chenxkang.android.fastbluetooth.command.tspl;

public class TSPLCommand implements ITSPLCommand {

    public TSPLCommand() {
    }

    @Override
    public String getInitCommand(int width, int height) {
        return "SIZE " + width + " mm," + height + " mm\r\n";
    }

    @Override
    public String getCLSCommand() {
        return "CLS\r\n";
    }

    @Override
    public String getCodePageCommand(String code) {
        return "CODEPAGE " + code + "\r\n";
    }

    @Override
    public String getGapCommand(int distance, int offset) {
        return "GAP " + distance + " mm," + offset + " mm\r\n";
    }

    @Override
    public String getBoldCommand(int bold) {
        return "BOLD " + bold + "\r\n";
    }

    @Override
    public String getDensityCommand(int density) {
        return "DENSITY " + density + "\r\n";
    }

    @Override
    public String getLineCommand(int x, int y, int width, int height) {
        return "BAR " + x + "," + y + "," + width + "," + height + "\r\n";
    }

    @Override
    public String getReverseCommand(int x, int y, int width, int height) {
        return "REVERSE " + x + "," + y + "," + width + "," + height + "\r\n";
    }

    @Override
    public String getBoxCommand(int x0, int y0, int x1, int y1, int thickness) {
        return "BOX " + x0 + "," + y0 + "," + x1 + "," + y1 + "," + thickness + "\r\n";
    }

    @Override
    public String getCircleCommand(int x, int y, int diameter, int thickness) {
        return "CIRCLE " + x + "," + y + "," + diameter + "," + thickness + "\r\n";
    }

    @Override
    public String getTextCommand(String rotation, int x, int y, int font, int x_multiple, int y_multiple, String align, String data) {
        if (align == null) {
            return "TEXT " + x + "," + y + "," + "\"" + font + "\"," + rotation + "," + x_multiple + "," + y_multiple + ",\"" + data + "\"\r\n";
        }
        return "TEXT " + x + "," + y + "," + "\"" + font + "\"," + rotation + "," + x_multiple + "," + y_multiple + "," + align + ",\"" + data + "\"\r\n";
    }

    @Override
    public String getImageCommand(int x, int y, int width, int height) {
        return "BITMAP " + x + "," + y + "," + width + "," + height + "," + 0 + ",";
    }

    @Override
    public String getImageCommand(int x, int y, int width, int height, int mode) {
        return "BITMAP " + x + "," + y + "," + width + "," + height + "," + mode + ",";
    }

    @Override
    public String getBarcodeCommand(String rotation, String type, int x, int y, int narrow, int wide, int height, int readable, String data) {
        return "BARCODE " + x + "," + y + "," + "\"" + type + "\"" + "," + height + "," + readable + "," + rotation + "," + narrow + "," + wide + "," + "\"" + data + "\"" + "\r\n";
    }

    @Override
    public String getQRCodeCommand(String rotation, int x, int y, String level, int width, String mode, String data) {
        return "QRCODE " + x + "," + y + "," + level + "," + width + "," + mode + "," + rotation + "," + "\"" + data + "\"" + "\r\n";
    }

    @Override
    public String getSelfCommand() {
        return "SELFTEST\r\n";
    }

    @Override
    public String getFormCommand() {
        return "FORMFEED\r\n";
    }

    @Override
    public String getEndPrintCommand(int m, int n) {
        return "PRINT " + m + "," + n + "\r\n";
    }
}
