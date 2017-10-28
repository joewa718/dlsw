package com.dlsw.cn.web.service;

import com.dlsw.cn.dto.DeliveryAddressDTO;
import com.dlsw.cn.dto.RealInfoDTO;
import com.dlsw.cn.dto.UserDTO;
import com.dlsw.cn.enumerate.RoleType;
import com.dlsw.cn.web.exception.DuplicateAccountException;
import com.dlsw.cn.po.User;
import com.dlsw.cn.web.vo.DeliveryAddressVo;
import com.dlsw.cn.web.vo.PhoneVo;
import com.dlsw.cn.web.vo.RealInfoVo;
import com.dlsw.cn.web.vo.UserVo;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface UserService {

    String updateFile(MultipartFile file, String filePath) throws Exception;

    User regWxUser(WxMpOAuth2AccessToken auth2AccessToken, WxMpUser user);

    UserDTO bindPhone(UserVo userVo, HttpSession session) throws DuplicateAccountException, OperationNotSupportedException;

    void regRealInfo(String phone, RealInfoVo vo);

    UserDTO findUserDTOByPhone(String phone);

    RealInfoDTO findRealInfoByPhone(String phone);

    List<DeliveryAddressDTO> findDeliveryAddressByPhone(String phone);

    DeliveryAddressDTO saveDeliveryAddress(String phone, DeliveryAddressVo deliveryAddressVo);

    void editDeliveryAddress(String phone, DeliveryAddressVo deliveryAddressVo);

    void setDeliveryAddressDefault(String phone, long deliveryAddressId, boolean isDefault);

    void editPassword(String phone,String password);

    void editPhoto(String phone, String photo);

    void editRoleType(String phone, RoleType roleType);

    void editReceiveMessage(String phone, boolean isReceiveMessage);

    void editNickname(String phone, String nickname);

    DeliveryAddressDTO getDefaultAddressByUser(String phone);

    String sendRegCode(String phone, String captcha, HttpSession session) throws Exception;

    String sendPwFoundCode(String phone, String captcha, HttpSession session) throws Exception;

    void foundPasswordNext(String phone,String pwFoundCode,HttpSession session) throws OperationNotSupportedException;

    void foundPassword(String password,HttpSession session) throws OperationNotSupportedException;

    UserDTO getByAuthorizationCode(String authorizationCode);

    Map<String,List<UserDTO>> findMyTeamUser(String person,String search);

    Map<String, List<UserDTO>> findMyNewTeamUser(String phone, String search);

    Map<String, List<UserDTO>> findMyZxTeamUser(String phone, String search);

    Map<String,List<UserDTO>> findNewSeniorImmediateMemberList(String person, String search);

    Map<String,List<UserDTO>> findSleepMemberList(String person, String search);

    void setWxLogin(String appId,boolean isWxLogin);

    User findUserByPhone(String phone);

    UserDTO editPhone(PhoneVo phoneVo, String phone, HttpSession session) throws OperationNotSupportedException;
}
