package com.ausy_technologies.demospring.Controller;

import com.ausy_technologies.demospring.Model.DAO.Role;
import com.ausy_technologies.demospring.Model.DAO.User;
import com.ausy_technologies.demospring.Model.DTO.UserDto;
import com.ausy_technologies.demospring.Repository.DateValidator;
import com.ausy_technologies.demospring.Service.DateValidatorUsingDateFormat;
import com.ausy_technologies.demospring.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addNewRole")
    public ResponseEntity<Object> addNewRole(@RequestBody Role role) {

        Role newRole = this.userService.saveRole(role);
        if (newRole.getName().isEmpty() || newRole.getName() == null) {
            return new ResponseEntity<>("The role cannot be empty or null\n", HttpStatus.BAD_REQUEST);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "addRole");
        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(newRole);
    }

    @PostMapping("/addRole")
    public Role saveRole(@RequestBody Role role) {
        return this.userService.saveRole(role);
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<Object> addNewUser(@RequestBody User user) {

        User newUser = this.userService.saveUser(user);

        if (checkValid(user) == null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Responded", "addUser");
            return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(newUser);
        } else
            throw new RuntimeException("User is NOT valid");
    }

    private ResponseEntity<String> checkValid(User newUser) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu", Locale.US).withResolverStyle(ResolverStyle.STRICT);
        DateValidator validator = new DateValidatorUsingDateFormat(dateFormatter);

        if (newUser.getFirstName().isEmpty() || newUser.getFirstName() == null) {
            return new ResponseEntity<>("The first name cannot be empty or null\n", HttpStatus.BAD_REQUEST);
        }
        if (newUser.getLastName().isEmpty() || newUser.getLastName() == null) {
            return new ResponseEntity<>("The last name cannot be empty or null\n", HttpStatus.BAD_REQUEST);
        }
        if (newUser.getEmail().isEmpty() || newUser.getEmail() == null) {
            return new ResponseEntity<>("The email cannot be empty or null\n", HttpStatus.BAD_REQUEST);
        }
        if (newUser.getUsername().isEmpty() || newUser.getUsername() == null) {
            return new ResponseEntity<>("The username cannot be empty or null\n", HttpStatus.BAD_REQUEST);
        }
        if (newUser.getBirthday() == null || validator.isValid(newUser.getBirthday().toString())) {
            return new ResponseEntity<>("The birthday should be valid\n", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @PostMapping("/addUser")
    public User saveUser(@RequestBody User user) {
        return this.userService.saveUser(user);
    }

    @PostMapping("/addNewUser2/{idRole}")
    public ResponseEntity<Object> addNewUser2(@RequestBody User user, @PathVariable int idRole) {

        User newUser = this.userService.saveUser(user);

        if (checkValid(user) == null && idRole > 0) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Responded", "addUser2");
            return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(newUser);
        } else
            throw new RuntimeException("User is NOT valid");
    }

    @PostMapping("/addUser2/{idRole}")
    public User saveUser2(@RequestBody User user, @PathVariable int idRole) {
        return this.userService.saveUser2(user, idRole);
    }

    @PostMapping("/addNewUser3/{roleList}")
    public ResponseEntity<Object> addNewUser3(@RequestBody User user, @PathVariable List<Role> roleList) {

        User newUser = this.userService.saveUser3(user, roleList);

        if (checkValid(user) == null && !roleList.isEmpty()) {
            for (int i = 0; i < roleList.size(); i++) {
                if (roleList.get(i) == null || roleList.get(i).toString().isEmpty()) {
                    throw new RuntimeException("Roles are NOT valid");
                }
            }
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Responded", "addUser3");
            return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(newUser);
        } else
            throw new ErrorResponse("Roles are NOT valid",404);
    }

    @PostMapping("/addUser3/{roleList}")
    public User saveUser3(@RequestBody User user, @PathVariable List<Role> roleList) {
        return this.userService.saveUser3(user, roleList);
    }

    @GetMapping("/findRoleBy/{id}")
    public Role findRoleById(@PathVariable int id) {

        if (id < 1)
            throw new ErrorResponse("Id is NOT valid",404);
        return this.userService.findRoleById(id);
    }

    @GetMapping("findUserBy/{id}")
    public User findUserById(@PathVariable int id){
        if (id < 1)
            throw new ErrorResponse("Id is NOT valid",404);
        return this.userService.findUserById(id);
    }

    @GetMapping("/findAllRoles")
    public List<Role> findAllRoles() {
        return userService.findAllRoles();
    }

    @GetMapping("/allUsers")
    public List<User> findAllUsers() {
        return this.userService.findAllUsers();
    }

    @DeleteMapping("/deleteUserById/{id}")
    public void deleteUser(@PathVariable int id) {

        if (id < 1)
            throw new ErrorResponse("Id is NOT valid",404);
        this.userService.deleteUserById(id);
    }

    @DeleteMapping("deleteRole/{id}")
    public void deleteRoleById(@PathVariable int id){
        userService.deleteRoleById(id);
    }

    @PutMapping("/updateRole/{name}")
    public ResponseEntity<Role> updateRole(@PathVariable String name, @RequestParam int id){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Response","updateRole");
        Role updatedRole =  null;
        try {
            updatedRole = userService.updateRole(id,name);
        }catch (ErrorResponse e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(null);
        }
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).headers(httpHeaders).body(updatedRole);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestParam int id, @RequestBody User user){
        User updatedUser = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Response","updateUser");
        try{
            updatedUser = userService.updateUser(id,user);

        }catch (ErrorResponse errorResponse){
            errorResponse.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(updatedUser);
    }

    @GetMapping("/findUserDtoById/{id}")
    public ResponseEntity<UserDto> findUserDtoById(@PathVariable int id) {
        User user = null;
        UserDto userDto = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Responded", "updateUserRole");

        try {
            user = this.userService.findUserById(id);
            userDto = this.userService.convertToDto(user);
        }catch (ErrorResponse errorResponse) {
            errorResponse.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(userDto);
        }

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(userDto);
    }

    @GetMapping("/findAllUsersDto")
    public ResponseEntity<List<UserDto>> findAllUsersDto() {
        List<User> userList = this.userService.findAllUsers();
        HttpHeaders httpHeaders = new HttpHeaders();
        List<UserDto> userDtos = this.userService.convertListToDto(userList);

        httpHeaders.add("Responded", "findAllUsersDto");
        return ResponseEntity.status(HttpStatus.ACCEPTED).headers(httpHeaders).body(userDtos);
    }
}
