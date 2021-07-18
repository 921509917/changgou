package com.duan.order.feign;
import com.duan.entity.Result;
import com.duan.order.pojo.TaskHis;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/18 13:58
 *****/
@FeignClient(name="order")
@RequestMapping("/taskHis")
public interface TaskHisFeign {

    /***
     * TaskHis分页条件搜索实现
     * @param taskHis
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    Result<PageInfo> findPage(@RequestBody(required = false) TaskHis taskHis, @PathVariable int page,
                              @PathVariable int size);

    /***
     * TaskHis分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    Result<PageInfo> findPage(@PathVariable int page, @PathVariable int size);

    /***
     * 多条件搜索品牌数据
     * @param taskHis
     * @return
     */
    @PostMapping(value = "/search" )
    Result<List<TaskHis>> findList(@RequestBody(required = false) TaskHis taskHis);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改TaskHis数据
     * @param taskHis
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    Result update(@RequestBody TaskHis taskHis, @PathVariable Long id);

    /***
     * 新增TaskHis数据
     * @param taskHis
     * @return
     */
    @PostMapping
    Result add(@RequestBody TaskHis taskHis);

    /***
     * 根据ID查询TaskHis数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<TaskHis> findById(@PathVariable Long id);

    /***
     * 查询TaskHis全部数据
     * @return
     */
    @GetMapping
    Result<List<TaskHis>> findAll();
}