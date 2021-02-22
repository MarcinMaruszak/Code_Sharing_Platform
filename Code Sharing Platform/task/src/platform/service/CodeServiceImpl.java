package platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import platform.model.Code;
import platform.model.Response;
import platform.repository.CodeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class CodeServiceImpl implements CodeService{

    @Autowired
    CodeRepository codeRepository;

    @Override
    public String getCodeHTML(UUID uuid, Model model) {
        model.addAttribute("code", getCode(uuid));
        return "getCode";
    }

    @Override
    public Code getCode(UUID uuid) {
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

    @Override
    public Response save(Code code) {
        code=codeRepository.save(code);
        return new Response(String.valueOf(code.getUuid()));

    }

    @Override
    public ArrayList<Code> getLatest() {
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
