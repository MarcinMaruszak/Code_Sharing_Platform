package platform.service;

import org.springframework.ui.Model;
import platform.model.Code;
import platform.model.Response;

import java.util.ArrayList;
import java.util.UUID;

public interface CodeService {

    String getCodeHTML(UUID uuid, Model model);

    Code getCode(UUID uuid);

    Response save(Code code);

    ArrayList<Code> getLatest();
}
