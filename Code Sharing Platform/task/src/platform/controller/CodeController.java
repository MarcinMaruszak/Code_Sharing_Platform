package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import platform.model.Code;
import platform.model.Response;
import platform.service.CodeServiceImpl;

import java.util.ArrayList;
import java.util.UUID;

@Controller
public class CodeController {

    @Autowired
    CodeServiceImpl codeService;

    //HTML-----------------------------

    @GetMapping(path = "/code/{uuid}")
    public String getCodeHTML(@PathVariable UUID uuid, Model model) {
        model.addAttribute("code", getCode(uuid));
        return "getCode";
    }

    @GetMapping(path = "/code/new")
    public String addCodeHtml() {
        return "postCode";
    }

    @GetMapping(path = "/code/latest")
    public String getLatestHTML(Model model) {
        model.addAttribute("latests", getLatest());
        return "getLatests";
    }

    //JSON-------------------------------------

    @GetMapping(path = "/api/code/{uuid}")
    public @ResponseBody
    Code getCode(@PathVariable UUID uuid) {
        return codeService.getCode(uuid);
    }

    @PostMapping(path = "/api/code/new")
    public @ResponseBody
    Response addCode(@RequestBody Code code) {
        return codeService.save(code);
    }

    @GetMapping(path = "/api/code/latest")
    public @ResponseBody
    ArrayList<Code> getLatest() {
        return codeService.getLatest();
    }
}
