import javax.annotation.processing.FilerException;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        long l = System.currentTimeMillis();

        // 使用穷举发写代码是为了方便控制

        // 查理成长日记第一季 统计20集
        ArrayList<String> strings1 = doIt("1.txt", "dst_1.txt");
        ArrayList<String> strings2 = doIt("2.txt", "dst_2.txt");
        ArrayList<String> strings3 = doIt("3.txt", "dst_3.txt");
        ArrayList<String> strings4 = doIt("4.txt", "dst_4.txt");
        ArrayList<String> strings5 = doIt("5.txt", "dst_5.txt");
        ArrayList<String> strings6 = doIt("6.txt", "dst_6.txt");
        ArrayList<String> strings7 = doIt("7.txt", "dst_7.txt");
        ArrayList<String> strings8 = doIt("8.txt", "dst_8.txt");
        ArrayList<String> strings9 = doIt("9.txt", "dst_9.txt");
        ArrayList<String> strings10 = doIt("10.txt", "dst_10.txt");
        ArrayList<String> strings11 = doIt("11.txt", "dst_11.txt");
        ArrayList<String> strings12 = doIt("12.txt", "dst_12.txt");
        ArrayList<String> strings13 = doIt("13.txt", "dst_13.txt");
        ArrayList<String> strings14 = doIt("14.txt", "dst_14.txt");
        ArrayList<String> strings15 = doIt("15.txt", "dst_15.txt");
        ArrayList<String> strings19 = doIt("19.txt", "dst_19.txt");
        ArrayList<String> strings22 = doIt("22.txt", "dst_22.txt");
        ArrayList<String> strings23 = doIt("23.txt", "dst_23.txt");
        ArrayList<String> strings24 = doIt("24.txt", "dst_24.txt");
        ArrayList<String> strings25 = doIt("25.txt", "dst_25.txt");


        strings1.addAll(strings2);
        strings1.addAll(strings3);
        strings1.addAll(strings4);
        strings1.addAll(strings5);
        strings1.addAll(strings6);
        strings1.addAll(strings7);
        strings1.addAll(strings8);
        strings1.addAll(strings9);
        strings1.addAll(strings10);
        strings1.addAll(strings11);
        strings1.addAll(strings12);
        strings1.addAll(strings13);
        strings1.addAll(strings14);
        strings1.addAll(strings15);
        strings1.addAll(strings19);
        strings1.addAll(strings22);
        strings1.addAll(strings23);
        strings1.addAll(strings24);
        strings1.addAll(strings25);


//         头脑特工队和超人总动员2
//        ArrayList<String> src1 = doIt("src.txt", "dst.txt");
//        ArrayList<String> src2 = doIt("src1.txt", "dst1.txt");
//        src1.addAll(src2);

        Map<String, Integer> stringIntegerMap = list2map(strings1);

        printMap(stringIntegerMap);

        result(strings1, stringIntegerMap);

        // 没有优化代码，目的很单纯，所以运行速度不快，反正要的不是效率，就算10秒钟才执行出来也没关系。
        // 优化应该会使用到线程池
        System.out.println("时间 = " + (System.currentTimeMillis() - l));

    }

    private static ArrayList<String> doIt(String src_name, String dst_name) {

        File src = new File("before/" + src_name);
        File dst = new File("after/" + dst_name);

        saveNewFile(src, dst);

        ArrayList<String> wordList = new ArrayList<>();

        traverseString(dst, new Handler() {
            @Override
            public void handleMessage(String str) throws IOException {
                String[] split = str.split(" ");
                for (String aSplit : split) {
                    if (!aSplit.equals("")) {
                        wordList.add(aSplit);
                    }
                }
            }
        });

        return wordList;

    }

    private static void result(ArrayList<String> wordList, Map<String, Integer> sortedMap) {

        int once = 0;

        for (String temp : wordList) {
            int count = sortedMap.get(temp);
            if (count == 1) {
                once++;
            }
        }

        System.out.println();
        System.out.println("--------------- 统计 ---------------");
        System.out.println("单词总使用个数：" + wordList.size());
        System.out.println("出现单词个数：" + sortedMap.entrySet().size());
        System.out.println("只出现过一次的单词的个数：" + once);
    }

    /**
     * 将list转化为排好序的map
     */
    private static Map<String, Integer> list2map(ArrayList<String> wordList) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String temp : wordList) {
            Integer count = map.get(temp);
            map.put(temp, (count == null) ? 1 : count + 1);
        }
        return sortByComparator(map);
    }

    /**
     * map按照value排序(从大到小)
     */
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

        LinkedList<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aList : list) {
            sortedMap.put(aList.getKey(), aList.getValue());
        }
        return sortedMap;
    }

    /**
     * 打印map
     */
    private static void printMap(Map<String, Integer> map) {
        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry.getKey() + "：" + entry.getValue());
        }
    }

    /**
     * 将字幕txt文件中的台词存储到一个新文件
     *
     * @param src 字幕文件
     * @param dst 新文件
     */
    private static void saveNewFile(File src, File dst) {

        try {

            if (dst.exists()) {
                dst.delete();
            }
            dst.createNewFile();

            OutputStreamWriter osw = new FileWriter(dst);
            BufferedWriter bw = new BufferedWriter(osw);

            traverseString(src, new Handler() {
                @Override
                public void handleMessage(String str) throws IOException {
                    if (filter(str)) {
                        String pureStr = pureStr(str);
                        bw.write(pureStr, 0, pureStr.length());
                        bw.newLine();
                        bw.flush();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 过滤规则
     */
    private static boolean filter(String str) {
        if (str.contains("-->") || str.length() == 0) {
            return false;
        } else {
            try {
                Integer.parseInt(str);
                return false;
            } catch (NumberFormatException e) {
                return true;
            }
        }
    }

    /**
     * 将字符串中乱七八糟的符号去掉（保留 空格 和 ' ）
     */
    private static String pureStr(String o) {
        String regEx = "[\n`~!@#$%^&*()+=|{}:;,\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(o);
        String trim = m.replaceAll(" ").trim();
        return trim.replace("-", " ").toLowerCase();
    }

    /**
     * 遍历文件中的每一行
     */
    private static void traverseString(File file, Handler handler) {
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while (((line = br.readLine()) != null)) {
                handler.handleMessage(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface Handler {
        void handleMessage(String str) throws IOException;
    }

}
