package job;

import com.spring.ioc.Injector;
import com.tomdog.reward.service.job.JobTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.DataSourceService;

@Slf4j
public abstract class BaseStarterTest {

    public void init() {
        Injector injector = Injector.getInstance();

        injector.injectAll();
        log.info("inject success");

        injector.getBean(JobTaskService.class).doJob();
        log.info("doJob invoke");

        injector.getBean(DataSourceService.class).init();
        log.info("DataSourceService init");
    }

}
