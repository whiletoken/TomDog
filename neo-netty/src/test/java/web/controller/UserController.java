package web.controller;


import com.tomdog.netty.annotation.*;
import com.tomdog.netty.rest.HttpStatus;
import com.tomdog.netty.rest.ResponseEntity;
import com.tomdog.netty.util.JacksonUtils;
import web.User;

//默认为单例，singleton = false表示启用多例。
//@RestController(singleton = false)
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("")
    @JsonResponse
    public ResponseEntity<User> listUser() {
        // 查询用户
        User user = new User();
        user.setId(1);
        user.setName("Leo");
        user.setAge((short) 18);
        return ResponseEntity.ok().build(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putMethod(@PathVariable("id") int id, @RequestBody String body) {
        // 更新用户
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMethod(@PathVariable int id) {
        // 删除用户
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("")
    public ResponseEntity<?> postMethod(@RequestBody String body) {
        // 添加用户
        User user = JacksonUtils.jsonToBean(body, User.class);
        return ResponseEntity.status(HttpStatus.CREATED).build(user);
    }

}
