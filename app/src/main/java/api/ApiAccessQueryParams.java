package api;

import java.util.HashMap;

public class ApiAccessQueryParams extends HashMap<String, String> {
    public ApiAccessQueryParams(String... data){
        int count = data.length / 2;
        for (int i = 0; i < count*2; i+=2) {
            this.put(data[i], data[i+1]);
        }
    }
}
