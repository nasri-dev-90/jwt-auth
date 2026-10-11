import io.jsonwebtoken.lang.Collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        String str="hello,my,ghost";
        Set set=new HashSet();

        set=Set.of(str.split(","));
        System.out.println(set);
    }
}
