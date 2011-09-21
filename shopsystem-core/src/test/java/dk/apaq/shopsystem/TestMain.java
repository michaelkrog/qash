package dk.apaq.shopsystem;

import com.google.gson.Gson;
import org.junit.Test;

/**
 *
 */
public class TestMain {

    @Test
    public void dummy() {

    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        System.out.print(gson.toJson(TestMain.class));
        System.out.println(System.getProperty("user.dir"));
    }
}
