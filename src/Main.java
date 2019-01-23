import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    public static String src_name = "src.txt";// 字幕文件
    public static String dst_name = "dst.txt";// 台词文件

    public static void main(String[] args) {

        // 字幕文件
        File src = new File(src_name);
        // 提取台词后的文件
        File dst = new File(dst_name);

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

        Map<String, Integer> sortedMap = list2map(wordList);
        printMap(sortedMap);

        result(wordList, sortedMap);

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
            System.out.println(entry.getKey() + "," + entry.getValue());
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
