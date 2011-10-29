package dk.apaq.shopsystem.rendering;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author michael
 */
public class BundleInfoIO {
    
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    
    public static BundleInfo read(InputStream inputStream) {
        return gson.fromJson(new InputStreamReader(inputStream), BundleInfo.class);
    }
}
