package job;

import com.spring.ioc.Injector;
import com.tomdog.reward.service.task.GenShinSignService;
import org.junit.Before;
import org.junit.Test;

public class JobTaskTest extends BaseStarterTest {

    @Before
    public void init() {
        super.init();
    }

    @Test
    public void testJobTask() {
        GenShinSignService genShinSignService = Injector.getInstance().getBean(GenShinSignService.class);
        genShinSignService.doJob();
    }

}
