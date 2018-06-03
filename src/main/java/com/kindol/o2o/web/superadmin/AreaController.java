package com.kindol.o2o.web.superadmin;

import com.kindol.o2o.entity.Area;
import com.kindol.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Controller 表明是controller
 * @RequestMapping 跟URL路由相关，调用此方法需要在这当中的路径下调用
 */

@Controller
    @RequestMapping("/superadmin")
public class AreaController {

    Logger logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaService;

    //value即对应的URL，写成小写便于写URL，method即对于的请求方法
    //@ResponseBody 表示自动转换成json
    @RequestMapping(value = "/listarea", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listArea(){
        //一般会在操作开始前给日志打标记
        logger.info("===start===");
        long startTime = System.currentTimeMillis();

        Map<String, Object> modelMap = new HashMap<>();
        List<Area> list;

        try {
            list = areaService.getAreaList();
            modelMap.put("rows", list);             //easyUI使用的命名规范
            modelMap.put("total", list.size());     //easyUI使用的命名规范
        }catch (Exception e){
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }

        logger.error("test error!");
        long endTime = System.currentTimeMillis();
        //一般会在操作完成后给日志打标记
        logger.debug("costTime:[{}ms]", endTime-startTime);
        logger.info("===end===");
        return modelMap;

    }

}
