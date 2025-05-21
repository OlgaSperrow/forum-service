package ait.cohort55.accounting.service;

import ait.cohort55.accounting.dao.UserAccountRepository;
import ait.cohort55.accounting.dto.RolesDto;
import ait.cohort55.accounting.dto.UserDto;
import ait.cohort55.accounting.dto.UserEditDto;
import ait.cohort55.accounting.dto.UserRegisterDto;
import ait.cohort55.accounting.dto.exception.UserExistException;
import ait.cohort55.accounting.dto.exception.UserNotFoundException;
import ait.cohort55.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if (userAccountRepository.existsById(userRegisterDto.getLogin())){
            throw new UserExistException();
        }
        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
        String password = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount= userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto removeUser(String login) {
        UserAccount userAccount= userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(userAccount);
        return  modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        UserAccount userAccount= userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String firstName = userEditDto.getFirstName();
        String lastName = userEditDto.getLastName();
        if(firstName!=null){
            userAccount.setFirstName(userEditDto.getFirstName());
        }
        if(lastName!=null){
            userAccount.setLastName(userEditDto.getLastName());
        }
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount userAccount= userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String roleUpperCase =role.toUpperCase();
        boolean isChanged = isAddRole ? userAccount.addRole(roleUpperCase) : userAccount.removeRole(roleUpperCase);
        if(isChanged){
            userAccountRepository.save(userAccount);
        }
        Set<String> roles = userAccount.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());


        return RolesDto.builder()
                .login(login)
                .roles(roles)
                .build();
    }

    @Override
    public void changePassword(String login, String newPassword) {

    }
}
