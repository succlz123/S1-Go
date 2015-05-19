package org.succlz123.s1go.app.dao.api;

/**
 * Created by fashi on 2015/5/18.
 */
public class DeEnCode {

    public static String code(String code) {
        char[] array = code.toCharArray();//获取字符数组
        for (int i = 0; i < array.length; i++) {//历遍字符数组
            array[i] = (char) (array[i] ^ 10086);//对数组每个元素进行异或运算
        }
        return new String(array);
    }
}
