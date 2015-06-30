package com.hjc.note.string_char_byte_test;

import java.nio.charset.Charset;
import java.text.BreakIterator;

/**
 * java 中的 String 对应的字符，在内存中是UTF-16（BE）方式保存，那么2^16 = 65535 并不能表示所有的 Unicode 字符，于是有如下疑问：
 * 1、String.length 是什么？
 * 2、String.getByte().length 是怎么计算的？
 * 3、java 用何种方式表示了全体字符？
 * 4、code point 是什么？code unit 又是什么？
 * <p/>
 * <p/>
 * <p/>
 * UTF-16 (16-bit Unicode Transformation Format) is a character encoding. The encoding is a variable-length encoding as code points are encoded with one or two 16-bit code units.  也就是说：在基本平面点里的范围是：\u0000-\uFFFF，超过这一范围的字符在内存中java没办法只用16bit 来表示了。某些字符在java内存中，需要2个16bit 保存。
 * <p/>
 * String.length()表示的不是字符串代表的字符的个数，而仅仅表示的是把 String 编码为UTF-16时，需要的16bit 的code units的个数。String.codePointCount(beginIndex, endIndex)表示的才是code point 的个数，也就是真正字符串的个数。
 * <p/>
 * Character Length
 * Java encodes strings in UTF-16, which represents each character (code point) with one or two 16-bit code units. This is a variable-length encoding scheme. The most commonly used characters are represented by one 16-bit code unit, while rarer ones like some mathematical symbols are represented by two.
 * The length method of String objects is not the length of that String in characters. Instead, it only gives the number of 16-bit code units used to encode a string. This is not (always) the number of Unicode characters (code points) in the string.
 * Since Java 1.5, the actual number of characters (code points) can be determined by calling the codePointCount method.
 *
 * 参考：http://rosettacode.org/wiki/String_length
 */
public class StringCharByteTest {
    public static void main(String[] args) throws Exception {
        String str = "Hadoop";

        //  Byte Length
        System.out.println("Charset.defaultCharset():" + Charset.defaultCharset());
        System.out.println(String.format("String:[%s] getBytes().length = %d", str, str.getBytes().length));
        System.out.println(String.format("String:[%s] getBytes(\"utf8\").length = %d",
                str, str.getBytes("utf8").length));
        System.out.println(String.format("String:[%s] getBytes(\"utf16\").length = %d",
                str, str.getBytes("utf16").length)); // 输出：14，因为前两字节是 FEFF
        System.out.println(String.format("String:[%s] getBytes(\"utf-16be\").length = %d",
                str, str.getBytes("utf-16be").length)); // 输出：12，因为已经明确指定了 be
        System.out.println(String.format("String:[%s] getBytes(\"utf-16le\").length = %d",
                str, str.getBytes("utf-16le").length)); // 输出：12，因为已经明确指定了 le
        System.out.println(String.format("String:[%s] getBytes(\"utf-32\").length = %d",
                str, str.getBytes("utf-32").length)); // 输出：24，因为6个字符，每个字符


        //  Character Length
        str = "\uD834\uDD3A\u0061"; //U+1D12A

        System.out.println(String.format("String:[%s] length() = %d",
                str, str.length())); // 输出：3，实际并不是 characters 的长度

        System.out.println(String.format("String:[%s] toCharArray().length() = %d",
                str, str.toCharArray().length)); // 输出：3，实际并不是 characters 的长度

        System.out.println(String.format("String:[%s] codePointCount(0, str.length()) = %d",
                str, str.codePointCount(0, str.length()))); // 输出：2，这里才是 characters 的长度


        //  "𝔘𝔫𝔦𝔠𝔬𝔡𝔢"
        str = "\uD835\uDD18\uD835\uDD2B\uD835\uDD26\uD835\uDD20\uD835\uDD2C\uD835\uDD21\uD835\uDD22";

        System.out.println(String.format("String:[%s] length() = %d",
                str, str.length())); // 输出：14

        System.out.println(String.format("String:[%s] toCharArray().length() = %d",
                str, str.toCharArray().length)); // 输出：14

        System.out.println(String.format("String:[%s] codePointCount(0, str.length()) = %d",
                str, str.codePointCount(0, str.length()))); // 输出：7

        System.out.println(String.format("String:[%s] getBytes(\"utf8\").length = %d",
                str, str.getBytes("utf8").length)); // 输出：28

        System.out.println(String.format("String:[%s] getBytes(\"utf16\").length = %d",
                str, str.getBytes("utf16").length)); // 输出：30

        System.out.println(String.format("String:[%s] getBytes(\"utf-16be\").length = %d",
                str, str.getBytes("utf-16be").length)); // 输出：28


        str = "møøse";

        System.out.println(String.format("String:[%s] length() = %d",
                str, str.length())); // 输出：3，实际并不是 characters 的长度

        System.out.println(String.format("String:[%s] toCharArray().length() = %d",
                str, str.toCharArray().length)); // 输出：3，实际并不是 characters 的长度

        System.out.println(String.format("String:[%s] codePointCount(0, str.length()) = %d",
                str, str.codePointCount(0, str.length()))); // 输出：2，这里才是 characters 的长度

        System.out.println(String.format("String:[%s] getBytes(\"utf8\").length = %d",
                str, str.getBytes("utf8").length)); // 输出：7

        System.out.println(String.format("String:[%s] getBytes(\"utf16\").length = %d",
                str, str.getBytes("utf16").length)); // 输出：12

        System.out.println(String.format("String:[%s] getBytes(\"utf-16be\").length = %d",
                str, str.getBytes("utf-16be").length)); // 输出：10

        //  Grapheme（字形） Length
        printLength("møøse");
        printLength("𝔘𝔫𝔦𝔠𝔬𝔡𝔢");
        printLength("J̲o̲s̲é̲");
        System.out.println("møøse".getBytes("utf-16be").length);
        System.out.println(str.getBytes("utf-16be").length);
    }

    public static void printLength(String s) {
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(s);
        int count = 0;
        while (it.next() != BreakIterator.DONE) {
            count++;
        }

        System.out.println(String.format("String:[%s] 字形 length = %d",
                s, count));
    }
}
