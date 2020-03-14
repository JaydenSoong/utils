package com.soong.utils.img;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 随机生成一个四位数字的验证码，验证码图片高度 32 px, 宽度 100 px。
 */
public class VerificationUtils {
    public static String createVerificationCode(HttpServletResponse response) throws IOException {
        //服务器通知浏览器不要缓存
        response.setHeader("pragma","no-cache");
        response.setHeader("cache-control","no-cache");
        response.setHeader("expires","0");

        int width = 100;
        int height = 32;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // 图片背景色
        Color gray = new Color(245, 245, 245);

        // 1.创建图片对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 2.创建画笔对象
        Graphics graphics = image.getGraphics();
        // 3.填充颜色
        fillColor(width, height, gray, graphics);
        // 4.写上字符
        drawStr(random, graphics, sb);
        // 5.画干扰线
        drawLine(random, graphics, width, height);
        // 6.将随机生成的验证码保存在 session 中
        //request.getSession().setAttribute("passcode", sb.toString());
        // 7.将图片对象写到页面中
        ImageIO.write(image, "PNG", response.getOutputStream());
        return sb.toString();
    }

    /**
     * 随机画上干扰线
     */
    private static void drawLine(Random random, Graphics graphics, int with, int height) {
        for (int i = 0; i < 8; i++) {
            int x1 = random.nextInt(with);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(with);
            int y2 = random.nextInt(height);
            Color lineColor = getColor(random);
            graphics.setColor(lineColor);
            graphics.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 为图片对象填充颜色
     */
    private static void fillColor(int width, int height, Color gray, Graphics graphics) {
        graphics.setColor(gray);
        graphics.fillRect(0, 0, width, height);
    }

    /**
     * 产生随机验证码
     *
     * @param sb 用于保存生成的验证码
     */
    private static void drawStr(Random random, Graphics graphics, StringBuilder sb) {
        String strs = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 1; i <= 4; i++) {
            int ch = random.nextInt(strs.length());
            char c = strs.charAt(ch);
            Color chColor = getColor(random);
            graphics.setColor(chColor);
            graphics.setFont(new Font("Arial", Font.BOLD, 18));
            graphics.drawString(String.valueOf(c), i * 20, 20);
            sb.append(c);
        }
    }

    /**
     * 随机生成一个颜色
     */
    private static Color getColor(Random random) {

        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return new Color(r, g, b);
    }
}

