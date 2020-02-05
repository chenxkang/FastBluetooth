package com.chenxkang.android.fastbluetooth.command.tspl;

/**
 * author: chenxkang
 * time  : 2019-11-13
 * desc  : TSPL指令集，单位dot，1mm = 8dot
 */
public interface ITSPLCommand {

    /**
     * 打印开始指令
     *
     * @param width  标签的宽度，单位mm
     * @param height 标签的高度，单位mm
     * @return
     */
    String getInitCommand(int width, int height);

    /**
     * 清除图像缓存区
     *
     * @return
     */
    String getCLSCommand();

    /**
     * 设置地区字符集
     *
     * @param code 字符类型
     * @return
     */
    String getCodePageCommand(String code);

    /**
     * 设置两张标签纸之间的垂直距离
     *
     * @param distance 标签纸之间的垂直距离，单位mm
     * @param offset   垂直间距的偏移量，单位mm
     * @return
     */
    String getGapCommand(int distance, int offset);

    /**
     * 设置字体加粗
     *
     * @param bold 0不加粗，1加粗
     * @return
     */
    String getBoldCommand(int bold);

    /**
     * 设置打印浓度
     *
     * @param density 浓度系数，0～15
     * @return
     */
    String getDensityCommand(int density);

    /**
     * 打印线条
     *
     * @param x      起始点x坐标
     * @param y      起始点y坐标
     * @param width  线条的单位宽度
     * @param height 线条的单位高度
     * @return
     */
    String getLineCommand(int x, int y, int width, int height);

    /**
     * 打印反转区域
     *
     * @param x      起始点x坐标
     * @param y      起始点y坐标
     * @param width  水平方向的单位宽度
     * @param height 垂直方向的单位高度
     * @return
     */
    String getReverseCommand(int x, int y, int width, int height);

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
     * 绘制一个圆
     *
     * @param x         水平方向左上角的起始位置
     * @param y         垂直方向左上角的起始位置
     * @param diameter  直径的单位长度
     * @param thickness 线条的单位厚度
     * @return
     */
    String getCircleCommand(int x, int y, int diameter, int thickness);

    /**
     * 打印文本
     *
     * @param rotation   文本方向
     * @param x          起始点x坐标
     * @param y          起始点y坐标
     * @param font       字体类型
     * @param x_multiple 水平方向的放大倍数，1～10
     * @param y_multiple 垂直方向的放大倍数，1～10
     * @param align      对齐方式
     * @param data       文本内容
     * @return
     */
    String getTextCommand(String rotation, int x, int y, int font, int x_multiple, int y_multiple, String align, String data);

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
     * 打印图片
     *
     * @param x      起始点x坐标
     * @param y      起始点y坐标
     * @param width  图片的单位宽度
     * @param height 图片的单位高度
     * @param mode   图片的绘制模式：0-OVERWRITE 1-OR 2-XOR
     * @return
     */
    String getImageCommand(int x, int y, int width, int height, int mode);

    /**
     * 打印条码
     *
     * @param rotation 条码方向
     * @param type     条码类型
     * @param x        起始点x坐标
     * @param y        起始点y坐标
     * @param narrow   窄条的单位宽度
     * @param wide     宽条的单位宽度
     * @param height   条码的单位高度
     * @param readable 是否显示底部文本
     * @param data     条码数据
     * @return
     */
    String getBarcodeCommand(String rotation, String type, int x, int y, int narrow, int wide, int height, int readable, String data);

    /**
     * 打印二维码
     *
     * @param rotation 二维码的方向
     * @param x        起始点x坐标
     * @param y        起始点y坐标
     * @param level    容错等级（L:7% M:15% Q:25% H:30%）
     * @param width    模块的单位宽度，1～10
     * @param mode     A：自动译码；M：手动译码
     * @param data     二维码数据
     * @return
     */
    String getQRCodeCommand(String rotation, int x, int y, String level, int width, String mode, String data);

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
     * @param m 指定打印的份数
     * @param n 指定每份打印的数量
     * @return
     */
    String getEndPrintCommand(int m, int n);
}
