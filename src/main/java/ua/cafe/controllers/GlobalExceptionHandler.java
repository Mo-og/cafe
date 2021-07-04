package ua.cafe.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {
        if (e.getCause().getMessage().startsWith("org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException"))
            redirectAttributes.addFlashAttribute("message", "Отказано: размер загружаемого файла превышает 10МБ.");
        else
            redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/uploadStatus";
    }

}
