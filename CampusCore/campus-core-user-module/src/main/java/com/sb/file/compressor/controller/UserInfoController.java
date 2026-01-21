package com.sb.file.compressor.controller;

import com.sb.file.compressor.auth.config.RequiredPermission;
import com.sb.file.compressor.core.exception.ApiResponse;
import com.sb.file.compressor.core.exception.ResponseModel;
import com.sb.file.compressor.core.utils.PaginationUtils;
import com.sb.file.compressor.entity.User;
import com.sb.file.compressor.entity.UserConfiguration;
import com.sb.file.compressor.service.contract.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.sb.file.compressor.controller.UserControllerApis.*;

/**
 * @author Himal Rai on 2/1/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@RestController
@RequestMapping(USER_INFO_ROOT)
public class UserInfoController {
    private final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(LIST)
    ResponseEntity<ResponseModel> users() {
        List<User> users = this.userService.getUsers();
        return ApiResponse.success(HttpStatus.CREATED, "Success", users);
    }

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping(PAGEABLE_LIST)
    public ResponseEntity<?> getPageableList(@RequestParam("size") int size, @RequestParam("page") int page, @RequestBody Map<String,Object> body){
        try {
            Page<User> users = this.userService.findPageable(PaginationUtils.pageable(page,size));
            return ApiResponse.success(users);
        }catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping(PAGEABLE_LIST_WITH_SEARCH_OBJECT)
    public ResponseEntity<?> getPageableListWithSearchObject(@RequestParam("size") int size, @RequestParam("page") int page, @RequestBody Map<String, String> body) {
        try {
            if (body.containsKey("name")) {
                String name = body.get("name");
                Page<User> users = this.userService.findPageableWithSearchObject(name, PaginationUtils.pageable(page, size));
                return ApiResponse.success(users);
            } else {
                Page<User> users = this.userService.findPageableWithSearchObject(null, PaginationUtils.pageable(page, size));
                return ApiResponse.success(users);
            }
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping(GET_USER_CONFIGURATION)
    public ResponseEntity<?> userConfiguration(@PathVariable("email") String email) {
        try {
            UserConfiguration response = this.userService.findByEmail(email).getUserConfiguration();
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
