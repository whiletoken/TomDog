import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.http.HttpUtil;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HttpTest {

    @Test
    public void test() {
        String result = HttpUtil.post("http://localhost:8081/task/startJob", "123");
        System.out.println(result);
    }

}
