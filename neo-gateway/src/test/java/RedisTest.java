import com.tomdog.gateway.App;
import com.tomdog.gateway.util.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testGet() {
        this.redisTemplate.opsForValue().set("test", "testsssss");
        System.out.println(this.redisTemplate.opsForValue().get("test"));
    }

    @Test
    public void testCreateGroup() {
        RedisUtil.createGroup("yyj_stream", "yyj_group");
    }

    @Test
    public void testStreamAdd() {
        Map<String, String> map = new HashMap<>();
        map.put("test1", "stream1");
        RecordId recordId = RedisUtil.xAdd(map, "yyj_stream");
        System.out.println("recordId is " + recordId);
    }

}
