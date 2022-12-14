package com.tomdog.reward.request.api;

import com.spring.ioc.Injector;
import com.tomdog.netty.annotation.*;
import com.tomdog.reward.service.job.JobTaskService;
import com.tomdog.reward.service.NeoMapService;
import com.tomdog.reward.util.JsonResult;

import static com.tomdog.reward.constant.Constant.executorService;

@RestController
@RequestMapping(value = "/task")
public class TaskApi {

    private final NeoMapService neoMapService;
    private final JobTaskService jobTaskService;

    public TaskApi() {
        Injector injector = Injector.getInstance();
        this.neoMapService = injector.getBean(NeoMapService.class);
        this.jobTaskService = injector.getBean(JobTaskService.class);
    }

    @PostMapping(value = "/startJob")
    @JsonResponse
    public JsonResult<Boolean> startJob() {
        executorService.execute(jobTaskService::job);
        return JsonResult.succeed(true);
    }

    @PostMapping(value = "/updateCookie")
    @JsonResponse
    public JsonResult<Boolean> updateCookie(@RequestParam(value = "key") String key,
                                            @RequestParam(value = "cookie") String cookie) {
        if ("myCookie".equalsIgnoreCase(key)) {
            neoMapService.updateValueByName("myCookie", cookie);
        } else {
            neoMapService.updateValueByName("linaCookie", cookie);
        }
        return JsonResult.succeed(true);
    }

}
