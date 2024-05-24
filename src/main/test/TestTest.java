import org.junit.jupiter.api.Test;

import java.util.*;

public class TestTest {
    @Test
    public void test(){

    }

    public int math(int n){
        int temp = 0;
        for(int i = 0; i < String.valueOf(n).length(); i++){
            temp += (int) Math.pow((n / (int) Math.pow(10, i)) % 10, 2);
        }
        return temp;
    }
}
