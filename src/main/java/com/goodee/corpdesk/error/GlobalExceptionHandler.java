package com.goodee.corpdesk.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 400 Bad Request: 요청 구문이 잘못되었거나, 유효성 검사를 통과하지 못하는 등 요청 자체를 서버가 이해할 수 없을 때
    // IllegalArgumentException: 메소드에 부적절한 인자가 전달됨
    // HttpMessageNotReadableException: 요청 본문(JSON 등)의 형식이 잘못되어 파싱할 수 없음
    // MethodArgumentNotValidException: Spring의 @Valid 애너테이션을 사용한 유효성 검사에 실패함 -> 전역 예외 처리에서 처리하지 않고, controller와 service에서 처리
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public String handleBadRequest(Exception ex, RedirectAttributes redirectAttributes) {
        log.error("Bad Request: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "잘못된 형식의 요청입니다.");
        return "redirect:/error/400";
    }

    // 403 Forbidden: 인증은 되었지만, 해당 리소스에 접근할 권한이 없음
    // AccessDeniedException: 특정 권한이 필요한 페이지에 권한 없는 사용자가 접근함
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(Exception ex, RedirectAttributes redirectAttributes) {
        log.error("Forbidden: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "이 페이지에 접근할 권한이 없습니다.");
        return "redirect:/error/403";
    }

    // 404 Not Found: 요청한 리소스(URL 또는 특정 데이터)를 찾을 수 없음
    // NoHandlerFoundException: 요청을 처리할 핸들러(컨트롤러 메소드)를 찾지 못함
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public String handleNoHandlerFound(Exception ex, RedirectAttributes redirectAttributes) {
        log.error("Not Found: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "요청하신 리소스를 찾을 수 없습니다.");
        return "redirect:/error/404";
    }

    // 405 Method Not Allowed: 요청한 URL은 존재하지만, 지원하지 않는 HTTP 메소드로 요청함
    // HttpRequestMethodNotSupportedException
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleMethodNotAllowed(Exception ex, RedirectAttributes redirectAttributes) {
        log.error("Method Not Allowed: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "지원하지 않는 요청 방식입니다.");
        return "redirect:/error/405";
    }

    //  500 Internal Server Error: 서버 내부 로직에서 예상치 못한 오류가 발생
    /*
     * NullPointerException
     * IllegalStateException: 객체의 상태가 메소드 호출을 처리하기에 적절하지 않음
     * Exception (최상위 예외): 위에서 개별적으로 처리하지 않은 모든 예외는 여기에 해당
     */
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, RedirectAttributes redirectAttributes) {
        log.error("Internal Server Error: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요.");
        return "redirect:/error/500";
    }

}
