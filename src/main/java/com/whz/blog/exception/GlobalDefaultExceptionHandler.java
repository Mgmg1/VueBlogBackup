package com.whz.blog.exception;

import com.whz.blog.entity.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 *旧的全局异常处理器，处理范围很有限，并且由于没有监控所有异常，还必须依赖 controller层的 try catch 兜底
 */
@Deprecated
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler({ValidationException.class, BindException.class})
    @ResponseStatus(HttpStatus.OK)
    public Object exception(Exception e, HttpServletRequest request, HttpServletResponse response) {

        Result result = new Result();
        result.setCode(101);

        //参数校验get
        if(e instanceof ValidationException){
            StringBuffer sb = new StringBuffer();
            ConstraintViolationException exs = (ConstraintViolationException)e;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for(ConstraintViolation<?> item : violations){
                sb.append(item.getMessage()+"/");
            }
            result.setMessage(sb.toString());
            return result;
        }
        //参数校验post
        if(e instanceof BindException){
            BindException exs = (BindException)e;
            BindingResult bindingResult = exs.getBindingResult();
            StringBuffer sb = new StringBuffer();
            for(FieldError fieldError :bindingResult.getFieldErrors()){
                sb.append(fieldError.getDefaultMessage()+"/");
            }
            result.setMessage(sb.toString());
            return result;
        }
        return result;
    }
}
