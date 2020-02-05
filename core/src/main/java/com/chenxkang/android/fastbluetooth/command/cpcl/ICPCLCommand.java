package com.chenxkang.android.fastbluetooth.command.cpcl;

/**
 * author: chenxkang
 * time  : 2019-11-12
 * desc  : CPCL指令集，单位dot，1mm = 8dot
 */
public interface ICPCLCommand {

    /**
     * 打印开始命令
     *
     * @param offset 整个标签的横向偏置
     * @param height 标签的最大高度
     * @param qty    打印的标签数量，最大值为1024
     * @return
     */
    String getInitCommand(int offset, int height, int qty);

    /**
     * 设置地区字符集
     *
     * @param code 字符类型
     * @return
     */
    String getCodePageCommand(String code);

    /**
     * 设置对齐方式
     *
     * @param align 对齐方式
     * @return
     */
    String getAlignCommand(String align);

    /**
     * 对齐方式
     *
     * @param align 对齐方式
     * @param end   对齐的结束点
     * @return
     */
    String getAlignCommand(String align, int end);

    /**
     * 设置字体的宽度、高度的放大倍数
     *
     * @param width  宽度的放大倍数，1～16
     * @param height 高度的放大倍数，1～16
     * @return
     */
    String getEnlargeCommand(int width, int height);

    /**
     * 设置字体加粗
     *
     * @param bold 加粗系数，0～5
     * @return
     */
    String getBoldCommand(int bold);

    /**
     * 设置字体浓度
     *
     * @param density 浓度系数，0～3
     * @return
     */
    String getDensityCommand(int density);

    /**
     * 打印线条
     *
     * @param x0        起始点x坐标
     * @param y0        起始点y坐标
     * @param x1        结束点x坐标
     * @param y1        结束点y坐标
     * @param thickness 线条的单位宽度
     * @return
     */
    String getLineCommand(int x0, int y0, int x1, int y1, int thickness);

    /**
     * 打印反转区域
     *
     * @param x0    左上角x坐标
     * @param y0    左上角y坐标
     * @param x1    右下角x坐标
     * @param y1    右下角y坐标
     * @param width 反转线的单位宽度
     * @return
     */
    String getReverseCommand(int x0, int y0, int x1, int y1, int width);

    /**
     * 打印矩形框
     *
     * @param x0        左上角x坐标
     * @param y0        左上角y坐标
     * @param x1        右下角x坐标
     * @param y1        右下角y坐标
     * @param thickness 线条的单位宽度
     * @return
     */
    String getBoxCommand(int x0, int y0, int x1, int y1, int thickness);

    /**
     * 打印下划线
     *
     * @param underLine 是否显示下划线
     * @return
     */
    String getUnderLineCommand(boolean underLine);

    /**
     * 设置打印宽度
     *
     * @param width 页面的单位宽度
     * @return
     */
    String getPageWidth(int width);

    /**
     * 打印文本
     *
     * @param rotation 文本方向
     * @param x        起始点x坐标
     * @param y        起始点y坐标
     * @param font     字体样式
     * @param size     字体大小
     * @param data     文本内容
     * @return
     */
    String getTextCommand(String rotation, int x, int y, int font, int size, String data);

    /**
     * 打印图片
     *
     * @param x      起始点x坐标
     * @param y      起始点y坐标
     * @param width  图片的单位宽度
     * @param height 图片的单位高度
     * @return
     */
    String getImageCommand(int x, int y, int width, int height);

    /**
     * 打印条码
     *
     * @param rotation  条码方向
     * @param type      条码类型
     * @param x         起始点x坐标
     * @param y         起始点y坐标
     * @param wide      窄条的单位宽度
     * @param ratio     宽条与窄条的比例
     * @param height    条码的单位高度
     * @param underText 是否显示条码下的文本
     * @param font      字体类型
     * @param size      字体大小
     * @param offset    文本与条码的单位偏移量
     * @param data      条码数据
     * @return
     */
    String getBarcodeCommand(String rotation, String type, int x, int y, int wide, int ratio, int height,
                             boolean underText, int font, int size, int offset, String data);

    String getBarcodeCommand(String rotation, String type, float x, int y, int wide, int ratio, int height,
                             boolean underText, int font, int size, int offset, String data);

    /**
     * 打印二维码
     *
     * @param rotation 二维码方向
     * @param x        起始点x坐标
     * @param y        起始点y坐标
     * @param mode     二维码类型
     * @param width    模块的单位宽度，1～32
     * @param data     二维码数据
     * @return
     */
    String getQRCodeCommand(String rotation, int x, int y, int mode, int width, String data);

    /**
     * 打印自检页
     *
     * @return
     */
    String getSelfCommand();

    /**
     * 打印结束后走纸到下一标签的起始位置
     *
     * @return
     */
    String getFormCommand();

    /**
     * 打印结束命令
     *
     * @return
     */
    String getEndPrintCommand();
}
