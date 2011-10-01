package dk.apaq.shopsystem.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author michael
 */
public class ScriptAnnotation {
    
    public static class Annotation {
        private final String name;
        private final Map<String, String> map;

        public Annotation(String name, Map<String, String> map) {
            this.name = name;
            this.map = map;
        }

        public String getName() {
            return name;
        }

        public Map<String, String> getMap() {
            return map;
        }
        
    }
    
    public static List<Annotation> read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<Annotation> list = new ArrayList<Annotation>();
        
        String line;
        boolean withinComment = false;
        
        while((line = reader.readLine()) != null) {
            line = line.trim();
            String trimmed = trimCommentChars(line);
            
            if("".equals(line)) {
                continue;
            }
            
            if(line.startsWith("/*")) {
                withinComment = true;
            }
            
            if(withinComment && looksLikeAnnotation(trimmed)) {
                list.add(parseAnnotation(trimmed));
                continue;
            }
            
            if(line.endsWith("*/")) {
                withinComment = false;
                break;
            }
            
        }
        
        return list;
    }
    
    private static String trimCommentChars(String line) {
        if(line.startsWith("*")) {
            line = line.substring(1).trim();
        }
        return line;
    }
    
    private static boolean looksLikeAnnotation(String line) {
        line = line.trim();
        
        return line.startsWith("@");
    }
    
    private static Annotation parseAnnotation(String line) {
        String name;
        String data = null;
        if(line.contains("(") && line.endsWith(")")) {
            int dataIndex = line.indexOf("(");
            name = line.substring(1, dataIndex).trim();
            data = line.substring(dataIndex+1, line.length()-1);
        } else {
            name = line.substring(1);
        }
        
        Map<String, String> datamap;
        if(data!=null) {
            datamap = splitData(data);
        } else {
            datamap  = new HashMap<String, String>();
        }
        
        return new Annotation(name, datamap);
    }
    
    private static Map<String, String> splitData(String data) {
        boolean atName = true;
        boolean withinQuotes=false;
        boolean escaped = false;
        int lastIndex=0;
        String name = null;
        String value;
        Map<String, String> map = new HashMap<String, String>();
        
        for(int i = 0; i<data.length();i++) {
            if(!withinQuotes && data.charAt(i) == ',') {
                lastIndex = i+1;
                atName = true;
                continue;
            }
            
            if(atName && data.charAt(i) == '=') {
                name = data.substring(lastIndex,i).trim();
                atName=false;
                continue;
            }
            
            if(!atName && !escaped && data.charAt(i) == '\"' && withinQuotes) {
                value = data.substring(lastIndex, i);
                map.put(name, value);
                withinQuotes = false;
                continue;
            }
            
            if(!atName && !withinQuotes && data.charAt(i) == '\"') {
                withinQuotes = true;
                lastIndex = i+1;
                continue;        
            }
            
            if(withinQuotes && data.charAt(i)=='\\') {
                escaped = true;
            }
            
            
        }
        return map;
    }
}
