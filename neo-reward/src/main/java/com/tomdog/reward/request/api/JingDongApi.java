package com.tomdog.reward.request.api;

import com.spring.ioc.Injector;
import com.tomdog.netty.annotation.*;
import com.tomdog.reward.dto.NeoMapDto;
import com.tomdog.reward.service.NeoMapService;
import com.tomdog.reward.util.JsonResult;

import java.util.Optional;

@RestController
@RequestMapping(value = "/jingDong")
public class JingDongApi {

    private final NeoMapService neoMapService;

    public JingDongApi() {
        Injector injector = Injector.getInstance();
        this.neoMapService = injector.getBean(NeoMapService.class);
    }

    @GetMapping(value = "/getCookie")
    @JsonResponse
    public JsonResult<String> getCookie() {
        Optional<NeoMapDto> neoMapDto = neoMapService.selectByName("jingdong");
        return neoMapDto.map(mapDto -> JsonResult.succeed(mapDto.getValue())).orElseGet(() -> JsonResult.succeed("no message"));
    }

    @PostMapping(value = "/updateCookie")
    @JsonResponse
    public JsonResult<String> updateCookie(@RequestParam(value = "cookie") String cookie) {
        neoMapService.updateValueByName("jingdong", cookie);
        return JsonResult.succeed("succeed");
    }

}
