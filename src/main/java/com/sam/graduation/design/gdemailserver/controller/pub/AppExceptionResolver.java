package com.sam.graduation.design.gdemailserver.controller.pub;

import com.sam.graduation.design.gdemailserver.constvalue.ServiceResultType;
import com.sam.graduation.design.gdemailserver.controller.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class AppExceptionResolver implements HandlerExceptionResolver {

    public final Logger logger = LoggerFactory.getLogger(AppExceptionResolver.class);


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception excep) {

        logger.error("Request path : " + request.getRequestURI() + ",error : ", excep); // louxinhua added

        ModelAndView view = new ModelAndView(); //"Error", "errorMsg", msg);
        MappingJackson2JsonView view2JsonView = new MappingJackson2JsonView();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(BaseController.RESULT_ISSUCCESS_KEY, Boolean.FALSE);
        if (!(excep instanceof AppException)) {
            attributes.put(BaseController.RESULT_MSG_KEY, "系统异常,请稍后再试。");
            attributes.put(BaseController.RESULT_ERROR_CODE_KEY, ServiceResultType.RESULT_TYPE_SYSTEM_ERROR);
        } else {
            attributes.put(BaseController.RESULT_MSG_KEY, excep.getMessage());
            attributes.put(BaseController.RESULT_ERROR_CODE_KEY, ((AppException) excep).getErrCode());
        }
        view2JsonView.setAttributesMap(attributes);
        view.setView(view2JsonView);


        return view;
    }

}
