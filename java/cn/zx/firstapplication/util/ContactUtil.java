package cn.zx.firstapplication.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by apple on 15-4-7.
 */
public class ContactUtil {
    private String[] names = new String[] { "Bob", "Bill", "Chris", "Candy", "Charles", "David", "Daniel", "Diego", "Diana", "Eva", "Frank", "Fernando", "Grace", "Gary", "Gina", "Henry",
            "Harry", "Helen", "Heidi" };

    public Map<Character, List<String>> processNames(String[] names) {
        Map<Character, List<String>> map = new TreeMap<>();
        Arrays.sort(names);
        for (int i = 0, j = 0; i < 26; i++) {
            List<String> list = new ArrayList<>();
            while (names[j].charAt(0) == 'A' + i) {
                list.add(names[j]);
                j++;
                if (j == names.length) {
                    break;
                }
            }
            if (!list.isEmpty()) {
                map.put((char) ('A' + i), list);
            }
            if (j == names.length) {
                break;
            }
        }
        return map;
    }

    public String[] getNames(){
        return names;
    }
}