package br.edu.seugame.repository;

import br.edu.seugame.domain.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.domain.Specification;

public final class TorneioFiltro {
    private TorneioFiltro() {}
    public static Specification<Torneio> por(String busca,TipoTorneio tipo,StatusTorneio status,LocalDate hoje) {
        return (root,query,builder) -> {
            var filtros=new ArrayList<jakarta.persistence.criteria.Predicate>();
            if(busca!=null && !busca.isBlank()) {
                String literal=busca.trim().toLowerCase(Locale.ROOT).replace("\\","\\\\").replace("%","\\%").replace("_","\\_");
                filtros.add(builder.like(builder.lower(root.get("nome")),"%"+literal+"%",'\\'));
            }
            if(tipo!=null)filtros.add(builder.equal(root.get("tipo"),tipo));
            if(status!=null) {
                var resultado=query.subquery(Long.class);var p=resultado.from(Partida.class);
                resultado.select(p.get("id")).where(builder.equal(p.get("torneio").get("id"),root.get("id")),
                        builder.isNotNull(p.get("placar1")),builder.isNotNull(p.get("placar2")));
                var iniciou=builder.or(builder.lessThanOrEqualTo(root.get("dataInicio"),hoje),builder.exists(resultado));
                filtros.add(switch(status) {
                    case FINALIZADO -> builder.equal(root.get("status"),StatusTorneio.FINALIZADO);
                    case FUTURO -> builder.and(builder.equal(root.get("status"),StatusTorneio.FUTURO),builder.not(iniciou));
                    case ANDAMENTO -> builder.or(builder.equal(root.get("status"),StatusTorneio.ANDAMENTO),
                            builder.and(builder.equal(root.get("status"),StatusTorneio.FUTURO),iniciou));
                });
            }
            return builder.and(filtros.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }
}
