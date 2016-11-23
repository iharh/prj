package mygwt.web.export;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/export/*")
public interface ExportService {
    @RequestMapping(value = "custom_export", method = RequestMethod.POST)
    void downloadCustomExport(@RequestParam("projectId") long projectId, @RequestParam("sessionId") long sessionId, HttpServletResponse response);

    @RequestMapping(value = "sharedlexicon", method = RequestMethod.POST)
    void exportSharedLexicon(ExportSharedLexiconProperties properties, HttpServletResponse response);

    //@InitBinder
    //void initBinder(WebDataBinder binder);
}
