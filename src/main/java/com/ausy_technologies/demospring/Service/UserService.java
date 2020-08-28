package com.ausy_technologies.demospring.Service;

import com.ausy_technologies.demospring.Controller.ErrorResponse;
import com.ausy_technologies.demospring.Model.DAO.Role;
import com.ausy_technologies.demospring.Model.DAO.User;
import com.ausy_technologies.demospring.Model.DTO.UserDto;
import com.ausy_technologies.demospring.Repository.RoleRepository;
import com.ausy_technologies.demospring.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(Role role) {
        return this.roleRepository.save(role);
    }

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    public User saveUser2(User user, int idRole) {

        Role role = this.roleRepository.findById(idRole).get();
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);

        if (role != null) {
            user.setRoleList(roleList);
            return this.userRepository.save(user);
        } else {
            throw new RuntimeException("Role not found");
        }
    }

    public User saveUser3(User user, List<Role> roleList) {
        user.setRoleList(roleList);
        return this.userRepository.save(user);
    }

    public Role findRoleById(int id) {
        return this.roleRepository.findById(id).get();
    }

    public User findUserById(int id) {
        return this.userRepository.findById(id);
    }

    public List<Role> findAllRoles() {
        return this.roleRepository.findAll();
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    public void deleteUserById(int id) {
        this.userRepository.deleteById(id);
    }

    public void deleteRoleById(int id) {
        roleRepository.deleteById(id);
    }

    public Role updateRole(int id, String name) {
        Role newRole = null;
        try {
            newRole = roleRepository.findById(id).get();
            newRole.setName(name);
            roleRepository.save(newRole);
        } catch (RuntimeException e) {
            throw new RuntimeException("Role NOT found !");
        }
        return newRole;
    }

    public User updateUser(int id, User user) {
        User modifiedUser = userRepository.findById(id);

        if (modifiedUser != null) {

            modifiedUser.setRoleList(user.getRoleList());
            modifiedUser.setFirstName(user.getFirstName());
            modifiedUser.setLastName(user.getLastName());
            modifiedUser.setEmail(user.getEmail());
            modifiedUser.setUsername(user.getUsername());
            modifiedUser.setPassword(user.getPassword());
            userRepository.save(modifiedUser);
        } else {
            throw new ErrorResponse("User NOT found !", 404);
        }
        return modifiedUser;
    }

    public UserDto convertToDto(User user) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(user, UserDto.class);
        List<String> roleList = new ArrayList<>();

        for (Role role : user.getRoleList())
            roleList.add(role.getName());

        userDto.setRoleList(roleList);
        return userDto;
    }

    public List<UserDto> convertListToDto(List<User> userList) {
        UserDto userDto;
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : userList) {
            userDto = convertToDto(user);
            userDtos.add(userDto);
        }
        return userDtos;
    }
}
