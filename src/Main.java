import javax.annotation.processing.FilerException;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        // 没有优化代码，目的很单纯，所以运行速度不快，反正要的不是效率，就算10秒钟才执行出来也没关系。
        // 优化应该会使用到线程池

        // 头脑特工队、超人总动员2、功夫熊猫3
//        fastShow("inside out", "The Incredibles 2", "kung fu panda3");

        // 20集《查理成长日记》
//        fastShow("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "19", "22", "23", "24", "25");

        // 统计before目录下所有文件
        fastShow();

    }

    /**
     * 统计before目录下所有文件
     */
    private static void fastShow() {
        long l = System.currentTimeMillis();
        File file = new File("before");
        File[] files = file.listFiles();
        ArrayList<String> allData = new ArrayList<>();
        for (File file1 : files) {
            allData.addAll(doIt(file1));
        }
        Map<String, Integer> stringIntegerMap = list2map(allData);
        printMap(stringIntegerMap);
        result(allData, stringIntegerMap);
        System.out.println("运行时间：" + (System.currentTimeMillis() - l));
    }

    /**
     * 输入文件名字，只统计被输入的文件
     */
    private static void fastShow(String... datas) {
        long l = System.currentTimeMillis();

        ArrayList<String> allData = new ArrayList<>();
        for (String data : datas) {
            allData.addAll(doIt(data + ".txt", data + ".txt"));
        }
        Map<String, Integer> stringIntegerMap = list2map(allData);
        printMap(stringIntegerMap);
        result(allData, stringIntegerMap);
        System.out.println("运行时间：" + (System.currentTimeMillis() - l));

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

    private static ArrayList<String> doIt(File src) {

        File dst = new File("after/" + src.getName());

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
