package com.github.zuihou.authority.controller.auth;

import java.util.List;

import javax.validation.Valid;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.zuihou.authority.dto.auth.ResourceQueryDTO;
import com.github.zuihou.authority.dto.auth.ResourceSaveDTO;
import com.github.zuihou.authority.dto.auth.ResourceUpdateDTO;
import com.github.zuihou.authority.entity.auth.Resource;
import com.github.zuihou.authority.service.auth.ResourceService;
import com.github.zuihou.base.BaseController;
import com.github.zuihou.base.R;
import com.github.zuihou.base.entity.SuperEntity;
import com.github.zuihou.common.utils.context.DozerUtils;
import com.github.zuihou.log.annotation.SysLog;
import com.github.zuihou.mybatis.conditions.Wraps;
import com.github.zuihou.mybatis.conditions.query.LbqWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * 资源
 * </p>
 *
 * @author zuihou
 * @date 2019-07-03
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/resource")
@Api(value = "Resource", description = "资源")
public class ResourceController extends BaseController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private DozerUtils dozer;

    /**
     * 分页查询资源
     *
     * @param data 分页查询对象
     * @return 查询结果
     */
    @ApiOperation(value = "分页查询资源", notes = "分页查询资源")
    @GetMapping("/page")
    @Validated(SuperEntity.OnlyQuery.class)
    @SysLog("分页查询资源")
    public R<IPage<Resource>> page(@Valid Resource data) {
        IPage<Resource> page = getPage();
        // 构建值不为null的查询条件
        LbqWrapper<Resource> query = Wraps.lbQ(data);
        resourceService.page(page, query);
        return success(page);
    }

    /**
     * 单体查询资源
     *
     * @param id 主键id
     * @return 查询结果
     */
    @ApiOperation(value = "单体查询资源", notes = "单体查询资源")
    @GetMapping("/{id}")
    @SysLog("单体查询资源")
    public R<Resource> get(@PathVariable Long id) {
        return success(resourceService.getById(id));
    }

    /**
     * 保存资源
     *
     * @param data 保存对象
     * @return 保存结果
     */
    @ApiOperation(value = "保存资源", notes = "保存资源不为空的字段")
    @PostMapping
    @SysLog("保存资源")
    public R<Resource> save(@RequestBody @Valid ResourceSaveDTO data) {
        Resource resource = dozer.map(data, Resource.class);
        resourceService.save(resource);
        return success(resource);
    }

    /**
     * 修改资源
     *
     * @param data 修改对象
     * @return 修改结果
     */
    @ApiOperation(value = "修改资源", notes = "修改资源不为空的字段")
    @PutMapping
    @Validated(SuperEntity.Update.class)
    @SysLog("修改资源")
    public R<Resource> update(@RequestBody @Valid ResourceUpdateDTO data) {
        Resource resource = dozer.map(data, Resource.class);
        resourceService.updateById(resource);
        return success(resource);
    }

    /**
     * 删除资源
     *
     * @param id 主键id
     * @return 删除结果
     */
    @ApiOperation(value = "删除资源", notes = "根据id物理删除资源")
    @DeleteMapping(value = "/{id}")
    @SysLog("删除资源")
    public R<Boolean> delete(@PathVariable Long id) {
        resourceService.removeById(id);
        return success(true);
    }

    /**
     * 查询用户可用的所有资源
     *
     * @param resource <br>
     *                 appCode 应用code * <br>
     *                 type 类型 <br>
     *                 group 菜单分组 <br>
     *                 resourceId 上级资源id <br>
     *                 accountId 当前登录人id
     * @return
     */
    @ApiOperation(value = "查询用户可用的所有资源", notes = "查询用户可用的所有资源")
    @GetMapping
    @SysLog("查询用户可用的所有资源")
    public R<List<Resource>> all(ResourceQueryDTO resource) {
        if (resource == null) {
            resource = new ResourceQueryDTO();
        }

        if (resource.getUserId() == null) {
            resource.setUserId(getUserId());
        }
        return success(resourceService.findVisibleResource(resource));
    }


}