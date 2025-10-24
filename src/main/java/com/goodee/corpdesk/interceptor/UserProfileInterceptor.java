package com.goodee.corpdesk.interceptor;

import com.goodee.corpdesk.employee.EmployeeService;
import com.goodee.corpdesk.employee.ResEmployeeDTO;
import com.goodee.corpdesk.file.entity.EmployeeFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Component
@Slf4j
public class UserProfileInterceptor implements HandlerInterceptor {

    @Autowired
    private EmployeeService employeeService;

    @Value("${app.upload.employee}")
    private String employeePath;

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

        // 0. 리다이렉트 혹은 restApi 응답이 아닌 경우에만 실행
        // restApi 응답인 경우
        if(modelAndView == null) return;
        // 리다이렉트인 경우
        String viewName = modelAndView.getViewName();
        if(viewName != null && viewName.startsWith("redirect:")) return;

        // 1. 토큰에서 사용자 정보 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 로그인하지 않은 사용자라면 DB에서 정보를 조회하지 않음
        if(authentication == null // SecurityContext에 인증 정보 자체가 없는 경우
            || !authentication.isAuthenticated() // 인증 객체는 있지만, 인증되지 않은 상태인 경우
            || authentication instanceof AnonymousAuthenticationToken // 로그인하지 않은 익명 사용자인 경우 (참고: AnonymousAuthenticationToken은 isAuthenticated() == true를 반환)
        ) return;

        String username = authentication.getName();

        // 2. 추출한 사용자 정보로 DB에서 정보 조회
        Optional<EmployeeFile> file = employeeService.getEmployeeFileByUsername(username);
        ResEmployeeDTO detail = employeeService.getFulldetail(username);

        // 3. 값 바인딩
        if(file.isPresent()) {
            EmployeeFile employeeFile = file.get();
            modelAndView.addObject("profileImgName", employeeFile.getSaveName() != null ? employeeFile.getSaveName() : "");
            modelAndView.addObject("profileImgExt", employeeFile.getExtension() != null ? employeeFile.getExtension() : "");
            modelAndView.addObject("profileImgPath", employeePath);
        }
        if(detail != null) {
            modelAndView.addObject("profileName", detail.getName()!= null ? detail.getName() : "");
            modelAndView.addObject("profilePosition", detail.getPositionName()!= null ? detail.getPositionName() : "");
        }

    }

}
