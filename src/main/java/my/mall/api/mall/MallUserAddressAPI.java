package my.mall.api.mall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import my.mall.api.mall.param.SaveMallUserAddressParam;
import my.mall.api.mall.param.UpdateMallUserAddressParam;
import my.mall.api.mall.vo.MallUserAddressVO;
import my.mall.common.ServiceResultEnum;
import my.mall.config.annotation.TokenToMallUser;
import my.mall.entity.User;
import my.mall.entity.MallUserAddress;
import my.mall.service.MallUserAddressService;
import my.mall.util.BeanUtil;
import my.mall.util.Result;
import my.mall.util.ResultGenerator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "mallUser", tags = "6.图书商城个人地址相关接口")
@RequestMapping("/api/mallUser")
public class MallUserAddressAPI {

    @Resource
    private MallUserAddressService mallUserAddressService;

    @GetMapping("/address")
    @ApiOperation(value = "我的收货地址列表", notes = "")
    public Result<List<MallUserAddressVO>> addressList(@TokenToMallUser User loginUser) {
        return ResultGenerator.genSuccessResult(mallUserAddressService.getMyAddresses(loginUser.getUserId()));
    }

    @PostMapping("/address")
    @ApiOperation(value = "添加地址", notes = "")
    public Result<Boolean> saveUserAddress(@RequestBody SaveMallUserAddressParam saveMallUserAddressParam,
                                           @TokenToMallUser User loginUser) {
        MallUserAddress userAddress = new MallUserAddress();
        BeanUtil.copyProperties(saveMallUserAddressParam, userAddress);
        userAddress.setUserId(loginUser.getUserId());
        Boolean saveResult = mallUserAddressService.saveUserAddress(userAddress);
        //添加成功
        if (saveResult) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult("添加失败");
    }

    @PutMapping("/address")
    @ApiOperation(value = "修改地址", notes = "")
    public Result<Boolean> updateMallUserAddress(@RequestBody UpdateMallUserAddressParam updateMallUserAddressParam,
                                                 @TokenToMallUser User loginUser) {
        MallUserAddress mallUserAddressById = mallUserAddressService.getMallUserAddressById(updateMallUserAddressParam.getAddressId());
        if (!loginUser.getUserId().equals(mallUserAddressById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        MallUserAddress userAddress = new MallUserAddress();
        BeanUtil.copyProperties(updateMallUserAddressParam, userAddress);
        userAddress.setUserId(loginUser.getUserId());
        Boolean updateResult = mallUserAddressService.updateMallUserAddress(userAddress);
        //修改成功
        if (updateResult) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult("修改失败");
    }

    @GetMapping("/address/{addressId}")
    @ApiOperation(value = "获取收货地址详情", notes = "传参为地址id")
    public Result<MallUserAddressVO> getMallUserAddress(@PathVariable("addressId") Long addressId,
                                                        @TokenToMallUser User loginUser) {
        MallUserAddress mallUserAddressById = mallUserAddressService.getMallUserAddressById(addressId);
        MallUserAddressVO mallUserAddressVO = new MallUserAddressVO();
        BeanUtil.copyProperties(mallUserAddressById, mallUserAddressVO);
        if (!loginUser.getUserId().equals(mallUserAddressById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        return ResultGenerator.genSuccessResult(mallUserAddressVO);
    }

    @GetMapping("/address/default")
    @ApiOperation(value = "获取默认收货地址", notes = "无传参")
    public Result getDefaultMallUserAddress(@TokenToMallUser User loginUser) {
        MallUserAddress mallUserAddressById = mallUserAddressService.getMyDefaultAddressByUserId(loginUser.getUserId());
        return ResultGenerator.genSuccessResult(mallUserAddressById);
    }

    @DeleteMapping("/address/{addressId}")
    @ApiOperation(value = "删除收货地址", notes = "传参为地址id")
    public Result deleteAddress(@PathVariable("addressId") Long addressId,
                                @TokenToMallUser User loginUser) {
        MallUserAddress mallUserAddressById = mallUserAddressService.getMallUserAddressById(addressId);
        if (!loginUser.getUserId().equals(mallUserAddressById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = mallUserAddressService.deleteById(addressId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }
}
