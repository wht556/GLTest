package com.gl.gl_wechat.common.ZXing;

/**
 * @author: zhaoCong
 * @date: 2020-07-02 16:35
 */
public class QrCodeTest {
    public static void main(String[] args) throws Exception {
        // 存放在二维码中的内容
        String text = "我是tczc2";
        // 嵌入二维码中间的LOGO图片路径
        String imgPath = "D:/dog.jpg";
        // 生成的二维码的路径及名称
        String destPath = "C:/GL_WECHAT/gl/images/image.jpg";
        //生成二维码
        QRCodeUtil.encode(text, null, destPath, true);
        // 解析二维码
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);

    }
}
