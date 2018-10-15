package org.succlz123.s1go.utils;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * Created by succlz123 on 2016/12/26.
 */

public class TarsyliaStr {

    public static final String TEXT1 = "黄昏来临的时候\n我知道，您会在那儿\n这，已足够\n命运，让人止步\n却不能夺走我眼中的微笑\n若爱，只是一个答案\n我愿像这些花草\n默默遥望。\n\n无名骑士";
    public static final String TEXT2 = "碎了碎了，傻瓜的南瓜……\n" + "算了算了，难过的傻瓜……\n" + "失去才是得到，挥别方能永驻\n" + "青春的真谛，就是一去不复返\n\n精灵民谣";
    public static final String TEXT3 = "你不再骄傲的背影，我叠成了青蛙，塞进行囊\n" + "你不再相信的话语，我织成了翅膀，挂在背上\n" + "就在这里分别吧，朋友。翅膀你带走，可以充饥\n" + "就在这里分别吧，朋友。行囊我留着，可以安息\n" + "我们的故事还很漫长，下一段孤独的旅行，由你陪伴\n" + "我们的结局还没写完，下一对哭泣的翅膀，由你扮演\n" + "没有人在故事外飞翔，只有人在故事里传奇\n" + "我是你的生日，请祝我快乐\n\n艾梅达尔";

    public static String getText() {
        int number = new Random().nextInt(3) + 1;
        String name = "TEXT" + number;
        Class<TarsyliaStr> c = TarsyliaStr.class;
        Field field = null;
        try {
            field = c.getDeclaredField(name);
            return field.get(null).toString();
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }
}
