package mygwt.web.export;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/export/*")
public interface ExportService {
    //@RequestMapping(value = "exp", method = RequestMethod.GET)
    @GetMapping("exp1")
    void exp1(HttpServletResponse response);

    @PostMapping("exp2")
    void exp2(HttpServletResponse response);
/*
    //@RequestMapping(value = "custom_export", method = RequestMethod.POST)
    @PostMapping("custom_export")
    void downloadCustomExport(@RequestParam("projectId") long projectId, @RequestParam("sessionId") long sessionId, HttpServletResponse response);

    //@RequestMapping(value = "sharedlexicon", method = RequestMethod.POST)
    @PostMapping("sharedlexicon")
    void exportSharedLexicon(ExportSharedLexiconProperties properties, HttpServletResponse response);
*/
    //@InitBinder
    //void initBinder(WebDataBinder binder);
}
