package com.duan.goods.controller;

import com.duan.entity.Result;
import com.duan.entity.StatusCode;
import com.duan.goods.service.BrandService;
import com.duan.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BrandController
 * @Author DuanJinFei
 * @Date 2021/3/30 23:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/brand")
@CrossOrigin // 禁止跨域请求
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public Result<List<Brand>> findAll() {
        List<Brand> brands = brandService.findAll();
        return new Result<>(true, StatusCode.OK,"查询成功",brands);
    }

    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable Integer id){
        Brand brand = brandService.findById(id); // 通过id查询
        return new Result<>(true,StatusCode.OK,"查询成功",brand); // 返回封装之后的查询所得数据
    }

    @PostMapping
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result(true,StatusCode.OK,"添加成功"); // 执行添加或者更新数据只需要返回提示消息即可 无返回数据库数据
    }

    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Brand brand,@PathVariable Integer id){
        // 设置id
        brand.setId(id);
        // 修改数据
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"更新成功");
    }
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id){
        // 删除数据
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }
    @PostMapping(value = "/search" )
    public Result<List<Brand>> findList(@RequestBody(required = false) Brand brand){
        List<Brand> list = brandService.findList(brand);
        return new Result<List<Brand>>(true,StatusCode.OK,"查询成功",list);
    }

    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //分页查询
        PageInfo<Brand> pageInfo = brandService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }
    /***
     * 根据分类实现品牌列表查询
     * /brand/category/{id}  分类ID
     */
    @GetMapping(value = "/category/{id}")
    public Result<List<Brand>> findBrandByCategory(@PathVariable(value = "id")Integer categoryId){
        //调用Service查询品牌数据
        List<Brand> categoryList = brandService.findByCategory(categoryId);
        return new Result<List<Brand>>(true,StatusCode.OK,"查询成功！",categoryList);
    }
}
