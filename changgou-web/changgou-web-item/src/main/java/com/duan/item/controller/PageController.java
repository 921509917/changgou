package com.duan.item.controller;

import com.duan.entity.Result;
import com.duan.entity.StatusCode;
import com.duan.item.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述
 *
 * @author www.itheima.com
 * @version 1.0
 * @package com.changgou.item.controller *
 * @since 1.0
 */

@RestController
@RequestMapping("/page")
public class PageController {
    @Autowired
    private PageService pageService;

    /**
     * 生成静态页面
     * @param id SPU的ID
     * @return
     */
    @RequestMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable(name="id") Long id){
        pageService.createPageHtml(id);
        return new Result(true, StatusCode.OK,"ok");
    }
    /**
     * 删除静态页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteHtml/{id}")
    public Result deleteHtml(@PathVariable(name = "id") Long id) {
        pageService.deletePageHtml(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
