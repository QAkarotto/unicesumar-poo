package br.edu.seugame.service;

import br.edu.seugame.dto.StatusResponse;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
    public StatusResponse consultar() {
        return new StatusResponse("SeuGame", "UP");
    }
}
