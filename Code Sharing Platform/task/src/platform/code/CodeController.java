package platform.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import platform.response.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
public class CodeController {

    @Autowired
    CodeRepository codeRepository;

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
        codeRepository.deleteAllByValidUntilBefore(LocalDateTime.now());
        Optional<Code> codeOptional = codeRepository.findByUuid(uuid);
        if (codeOptional.isPresent()) {
            Code code = codeOptional.get();
            if (code.getViews() == 1) {
                codeRepository.deleteByUuid(code.getUuid());
                code.setLastView(true);
            }
            code.decreaseView();
            codeRepository.flush();
            return code;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No code with requested id");
        }
    }

    @PostMapping(path = "/api/code/new")
    public @ResponseBody
    Response addCode(@RequestBody Code code) {
        code = codeRepository.save(code);
        return new Response(String.valueOf(code.getUuid()));
    }

    @GetMapping(path = "/api/code/latest")
    public @ResponseBody
    ArrayList<Code> getLatest() {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        codeRepository.deleteAllByValidUntilBefore(dateTimeNow);
        ArrayList<Code> codes = (ArrayList<Code>) codeRepository.findTop10ByOrderByDateDesc();
        codes.forEach(code -> {
            if (code.getViews() == 1) {
                codeRepository.deleteByUuid(code.getUuid());
            }
            code.decreaseView();
        });
        codeRepository.flush();
        return codes;
    }
}
