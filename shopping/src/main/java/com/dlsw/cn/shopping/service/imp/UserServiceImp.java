package com.dlsw.cn.shopping.service.imp;

import com.dlsw.cn.common.po.OAuthInfo;
import com.dlsw.cn.common.service.BaseService;
import com.dlsw.cn.shopping.service.UserService;
import com.dlsw.cn.shopping.mapper.DeliveryAddressMapper;
import com.dlsw.cn.common.dto.DeliveryAddressDTO;
import com.dlsw.cn.common.dto.RealInfoDTO;
import com.dlsw.cn.common.dto.UserDTO;
import com.dlsw.cn.common.enumerate.RoleType;
import com.dlsw.cn.shopping.exception.DuplicateAccountException;
import com.dlsw.cn.shopping.mapper.OauthInfoMapper;
import com.dlsw.cn.shopping.mapper.RealInfoMapper;
import com.dlsw.cn.shopping.mapper.UserMapper;
import com.dlsw.cn.common.po.DeliveryAddress;
import com.dlsw.cn.common.po.RealInfo;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.common.repositories.DeliveryAddressRepository;
import com.dlsw.cn.common.repositories.UserRepository;
import com.dlsw.cn.common.util.DateUtil;
import com.dlsw.cn.common.util.GenerateRandomCode;
import com.dlsw.cn.common.util.SmsSender;
import com.dlsw.cn.common.util.encrypt.AESCryptUtil;
import com.dlsw.cn.shopping.vo.DeliveryAddressVo;
import com.dlsw.cn.shopping.vo.PhoneVo;
import com.dlsw.cn.shopping.vo.RealInfoVo;
import com.dlsw.cn.shopping.vo.UserVo;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
@Service
@Scope("prototype")
public class UserServiceImp extends BaseService implements UserService {
    private final static Logger log = LoggerFactory.getLogger(UserServiceImp.class);
    @Value("${wechat.login.defaultPwd}")
    private String defaultPwd;
    @Value("${photo.path}")
    private String filePath;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RealInfoMapper realInfoMapper;
    @Autowired
    DeliveryAddressMapper deliveryAddressMapper;
    @Autowired
    SmsSender smsSender;
    @Autowired
    OauthInfoMapper oauthInfoMapper;
    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    public String updateFile(MultipartFile file, String filePath) throws Exception {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID() + suffixName;
            fileName = fileName.toLowerCase();
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return fileName;
        }
        throw new Exception("上传文件不存在");
    }

    @Override
    public User regWxUser(WxMpOAuth2AccessToken auth2AccessToken, WxMpUser wxMpUser) {
        User user = userRepository.findByAppId(wxMpUser.getOpenId());
        if (user == null) {
            user = new User();
            try {
                UUID uuid = UUID.randomUUID();
                log.debug("wxMpUser.getHeadImgUrl():" + wxMpUser.getHeadImgUrl());
                download(wxMpUser.getHeadImgUrl(), uuid + ".jpg", filePath);
                user.setHeadPortrait(uuid + ".jpg");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            user.setAppId(wxMpUser.getOpenId());
            user.setNickname(wxMpUser.getNickname());
            user.setDisable(false);
            user.setPhone(wxMpUser.getOpenId());
            user.setRoleType(RoleType.普通);
            user.setRegTime(new Date());
            user.setVerificationPhone(false);
            user.setPassword(AESCryptUtil.encrypt(defaultPwd));
        }
        OAuthInfo oAuthInfo = oauthInfoMapper.WxMpOAuth2AccessTokenToOAuthInfo(auth2AccessToken);
        user.setOAuthInfo(oAuthInfo);
        oAuthInfo.setUser(user);
        user = userRepository.save(user);
        return user;
    }


    public static void download(String urlString, String filename, String savePath) throws Exception {
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(5 * 1000);
        InputStream is = con.getInputStream();
        byte[] bs = new byte[1024];
        int len;
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath() + "/" + filename);
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.close();
        is.close();
    }

    @Override
    public UserDTO bindPhone(UserVo userVo ,HttpSession session) throws DuplicateAccountException, OperationNotSupportedException {
        User user = userRepository.findByPhone(userVo.getPhone());
        if (user != null) {
            throw new DuplicateAccountException("duplicate account phone.");
        }
        String regCode = (String) session.getAttribute("regCode");
        if (userVo.getRegCode() == null || !userVo.getRegCode().equals(regCode)) {
            throw new OperationNotSupportedException("手机验证码不正确");
        }
        Long regCodeTime = (Long) session.getAttribute("regCodeTime");
        long cur_time = DateUtil.getCurrentDate().getTime();
        if ((cur_time - regCodeTime) / 1000 > 600) {//10分钟
            throw new OperationNotSupportedException("手机验证码已经过期");
        }
        user = userRepository.findOne(userVo.getUserId());
        user.setPhone(userVo.getPhone());
        user = userRepository.save(user);
        session.removeAttribute("regCode");
        session.removeAttribute("regCodeTime");
        return userMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO editPhone(PhoneVo phoneVo, String phone, HttpSession session) throws OperationNotSupportedException {
        User user = userRepository.findByPhone(phoneVo.getPhone());
        if (user != null) {
            throw new OperationNotSupportedException("您修改的手机号码已存在");
        }
        String regCode = (String) session.getAttribute("regCode");
        if (phoneVo.getRegCode() == null || !phoneVo.getRegCode().equals(regCode)) {
            throw new OperationNotSupportedException("手机验证码不正确");
        }
        Long regCodeTime = (Long) session.getAttribute("regCodeTime");
        long cur_time = DateUtil.getCurrentDate().getTime();
        if ((cur_time - regCodeTime) / 1000 > 600) {//10分钟
            throw new OperationNotSupportedException("手机验证码已经过期");
        }
        user = userRepository.findByPhone(phone);
        user.setPhone(phoneVo.getPhone());
        user.setVerificationPhone(true);
        user = userRepository.save(user);
        session.removeAttribute("regCode");
        session.removeAttribute("regCodeTime");
        Authentication token = new UsernamePasswordAuthenticationToken(user.getPhone(), AESCryptUtil.decrypt(user.getPassword()));
        Authentication result = daoAuthenticationProvider.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(result);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public void regRealInfo(String phone, RealInfoVo realInfoVo) {
        User user = userRepository.findByPhone(phone);
        RealInfo realInfo = realInfoMapper.realInfoToVoRealInfo(realInfoVo);
        if (user.getRealInfo() == null) {
            user.setRealInfo(realInfo);
            realInfo.setUser(user);
        } else {
            RealInfo old_realInfo = user.getRealInfo();
            old_realInfo.setIdCard(realInfo.getIdCard());
            old_realInfo.setIdCardPhotoBack(realInfo.getIdCardPhotoBack());
            old_realInfo.setIdCardPhotoFront(realInfo.getIdCardPhotoFront());
            old_realInfo.setBirthday(realInfo.getBirthday());
            old_realInfo.setSex(realInfo.getSex());
            old_realInfo.setRealName(realInfo.getRealName());
            old_realInfo.setCity(realInfo.getCity());
            old_realInfo.setProvince(realInfo.getProvince());
            old_realInfo.setRegion(realInfo.getRegion());
            old_realInfo.setOccupation(realInfo.getOccupation());
        }
        userRepository.save(user);
    }

    @Override
    public void editPassword(String phone, String password) {
        User user = userRepository.findByPhone(phone);
        user.setPassword(AESCryptUtil.encrypt(password));
        userRepository.save(user);
    }

    @Override
    public void editPhoto(String phone, String photo) {
        User user = userRepository.findByPhone(phone);
        user.setHeadPortrait(photo);
        userRepository.save(user);
    }

    @Override
    public void editReceiveMessage(String phone, boolean isReceiveMessage) {
        User user = userRepository.findByPhone(phone);
        user.setReceiveMessage(isReceiveMessage);
        userRepository.save(user);
    }


    @Override
    public UserDTO findUserDTOByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public User findUserByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return user;
    }

    @Override
    public RealInfoDTO findRealInfoByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        RealInfo realInfo = user.getRealInfo();
        return realInfoMapper.realInfoToRealInfoDTO(realInfo);
    }

    @Override
    public List<DeliveryAddressDTO> findDeliveryAddressByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return deliveryAddressMapper.deliveryAddressToDeliveryAddressDTOList(user.getDeliveryAddressList());
    }

    @Override
    public DeliveryAddressDTO saveDeliveryAddress(String phone, DeliveryAddressVo deliveryAddressVo) {
        User user = userRepository.findByPhone(phone);
        Optional<Set<DeliveryAddress>> deliveryAddressList = Optional.ofNullable(user.getDeliveryAddressList());
        deliveryAddressList.orElse(new TreeSet<>());
        if (deliveryAddressVo.getIsDefault()) {
            deliveryAddressList.get().forEach(deliveryAddress -> {
                deliveryAddress.setIsDefault(false);
            });
        }
        DeliveryAddress deliveryAddress = deliveryAddressMapper.deliveryAddressVoToDeliveryAddress(deliveryAddressVo);
        deliveryAddress.setUser(user);
        deliveryAddressList.get().add(deliveryAddress);
        userRepository.save(user);
        Optional<DeliveryAddress> optional = user.getDeliveryAddressList().stream().filter(address -> {
            return address.getIsDefault();
        }).findFirst();
        if (optional.isPresent()) {
            return deliveryAddressMapper.deliveryAddressToDeliveryAddressDTO(optional.get());
        }
        throw new RuntimeException("未找到默认地址");
    }

    @Override
    public void editDeliveryAddress(String phone, DeliveryAddressVo deliveryAddressVo) {
        User user = userRepository.findByPhone(phone);
        for (DeliveryAddress deliveryAddress : user.getDeliveryAddressList()) {
            if (deliveryAddressVo.getIsDefault()) {
                deliveryAddress.setIsDefault(false);
            }
            if (deliveryAddressVo.getId() == deliveryAddress.getId()) {
                deliveryAddress.setPhone(deliveryAddressVo.getPhone());
                deliveryAddress.setRegion(deliveryAddressVo.getRegion());
                deliveryAddress.setDeliveryMan(deliveryAddressVo.getDeliveryMan());
                deliveryAddress.setCity(deliveryAddressVo.getCity());
                deliveryAddress.setProvince(deliveryAddressVo.getProvince());
                deliveryAddress.setDetailed(deliveryAddressVo.getDetailed());
                deliveryAddress.setIsDefault(deliveryAddressVo.getIsDefault());

            }
        }
        userRepository.save(user);
    }

    @Override
    public void setDeliveryAddressDefault(String phone, long deliveryAddressId, boolean isDefault) {
        User user = userRepository.findByPhone(phone);
        for (DeliveryAddress deliveryAddress : user.getDeliveryAddressList()) {
            if (isDefault) {
                deliveryAddress.setIsDefault(false);
            }
            if (deliveryAddressId == deliveryAddress.getId()) {
                deliveryAddress.setIsDefault(isDefault);
            }
        }
        userRepository.save(user);
    }

    @Override
    public void editRoleType(String phone, RoleType roleType) {
        User user = userRepository.findByPhone(phone);
        user.setRoleType(roleType);
        userRepository.save(user);
    }

    @Override
    public void editNickname(String phone, String nickname) {
        User user = userRepository.findByPhone(phone);
        user.setNickname(nickname);
        userRepository.save(user);
    }

    @Override
    public DeliveryAddressDTO getDefaultAddressByUser(String phone) {
        User user = userRepository.findByPhone(phone);
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findOneByIsDefaultAndUser(true, user);
        return deliveryAddressMapper.deliveryAddressToDeliveryAddressDTO(deliveryAddress);
    }

    @Override
    public String sendRegCode(String phone, String captcha, HttpSession session) throws Exception {
        try {
            String session_captcha = (String) session.getAttribute("captcha");
            if (captcha == null || !captcha.equals(session_captcha)) {
                throw new OperationNotSupportedException("图片验证码不正确");
            }
            String code = GenerateRandomCode.getRandNum(6);
            smsSender.sendRegCode(phone, code);
            return code;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String sendPwFoundCode(String phone, String captcha, HttpSession session) throws Exception {
        try {
            String session_captcha = (String) session.getAttribute("captcha");
            if (captcha == null || !captcha.equals(session_captcha)) {
                throw new OperationNotSupportedException("图片验证码不正确");
            }
            String code = GenerateRandomCode.getRandNum(6);
            smsSender.sendPwFoundCode(phone, code);
            return code;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void foundPasswordNext(String phone, String pwFoundCode, HttpSession session) throws OperationNotSupportedException {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            throw new OperationNotSupportedException("未找到用户手机号");
        }
        String session_pwFoundCode = (String) session.getAttribute("sendPwFoundCode");
        if (pwFoundCode == null || !pwFoundCode.equals(session_pwFoundCode)) {
            throw new OperationNotSupportedException("手机验证码不正确");
        }
        Long sendPwFoundTime = (Long) session.getAttribute("sendPwFoundTime");
        long cur_time = DateUtil.getCurrentDate().getTime();
        if ((cur_time - sendPwFoundTime) / 1000 > 600) {//10分钟
            throw new OperationNotSupportedException("手机验证码已经过期");
        }
        session.setAttribute("foundPhone", phone);
    }

    @Override
    public void foundPassword(String password, HttpSession session) throws OperationNotSupportedException {
        String phone = (String) session.getAttribute("foundPhone");
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            throw new OperationNotSupportedException("未找到用户手机号");
        }
        String session_pwFoundCode = (String) session.getAttribute("sendPwFoundCode");
        if (session_pwFoundCode == null) {
            throw new OperationNotSupportedException("手机验证码不正确");
        }
        Long sendPwFoundTime = (Long) session.getAttribute("sendPwFoundTime");
        long cur_time = DateUtil.getCurrentDate().getTime();
        if ((cur_time - sendPwFoundTime) / 1000 > 600) {//10分钟
            throw new OperationNotSupportedException("手机验证码已经过期");
        }
        user.setPassword(AESCryptUtil.encrypt(password));
        userRepository.save(user);
        session.removeAttribute("sendPwFoundCode");
        session.removeAttribute("sendPwFoundTime");
    }

    @Override
    public UserDTO getByAuthorizationCode(String authorizationCode) {
        User user = userRepository.getByAuthorizationCode(authorizationCode);
        if (user == null) {
            throw new RuntimeException("您的验证码无效,未查询到用户信息");
        }
        return userMapper.userToUserDTO(user);
    }

    @Override
    public Map<String, List<UserDTO>> findMyTeamUser(String phone, String search) {
        User user = userRepository.findByPhone(phone);
        List<User> userList = userRepository.findByLikeOrgPath(getOrgPath(user));
        return getMyTeamList(search, userList);
    }

    @Override
    public Map<String, List<UserDTO>> findMyNewTeamUser(String phone, String search) {
        User user = userRepository.findByPhone(phone);
        List<User> userList = userRepository.findByLikeOrgPath(getOrgPath(user));
        return getMyTeamList(search, userList);
    }

    @Override
    public Map<String, List<UserDTO>> findNewSeniorImmediateMemberList(String phone, String search) {
        User user = userRepository.findByPhone(phone);
        List<User> userList = userRepository.findNewSeniorImmediateMemberList(getOrgPath(user), RoleType.高级合伙人);
        return getMyTeamList(search, userList);
    }

    @Override
    public Map<String, List<UserDTO>> findSleepMemberList(String phone, String search) {
        User user = userRepository.findByPhone(phone);
        List<User> userList = userRepository.findSleepMemberList(getOrgPath(user));
        return getMyTeamList(search, userList);
    }

    @Override
    public Map<String, List<UserDTO>> findMyZxTeamUser(String phone, String search) {
        User user = userRepository.findByPhone(phone);
        List<User> userList = userRepository.findByOneLevelOrgPath(getOrgPath(user));
        return getMyTeamList(search, userList);
    }

    private Map<String, List<UserDTO>> getMyTeamList(String search, List<User> userList) {
        Map<String, List<UserDTO>> result = new LinkedHashMap<>();
        result.put(RoleType.高级合伙人.getName(), new ArrayList<>());
        result.put(RoleType.合伙人.getName(), new ArrayList<>());
        result.put(RoleType.VIP.getName(), new ArrayList<>());
        if (search != null) {
            userList = userList.stream().filter(u -> search.equals(u.getPhone()) || (u.getRealInfo() != null && search.equals(u.getRealInfo().getRealName()))).collect(Collectors.toList());
        }
        userList.forEach(u -> {
            if (result.containsKey(u.getRoleType().getName())) {
                result.get(u.getRoleType().getName()).add(userMapper.userToUserDTO(u));
            }
        });
        return result;
    }

    @Override
    public void setWxLogin(String appId, boolean isWxLogin) {
        User user = userRepository.findByAppId(appId);
        if (user != null) {
            userRepository.save(user);
        } else {
            throw new RuntimeException("not found appId");
        }


    }
}
