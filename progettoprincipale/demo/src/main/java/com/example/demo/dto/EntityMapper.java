package com.example.demo.dto;

import com.example.demo.entity.Distributore;
import com.example.demo.entity.Utente;

public class EntityMapper {

    public static DistributoreDTO toDistributoreDTO(Distributore entity) {
        if (entity == null) return null;
        DistributoreDTO dto = new DistributoreDTO();
        dto.setCodiceVisivo(entity.getCodiceVisivo());
        dto.setModello(entity.getModello());
        dto.setIndirizzo(entity.getIndirizzo());
        dto.setLivelloAcqua(entity.getLivelloAcqua());
        dto.setLivelloZucchero(entity.getLivelloZucchero());
        dto.setLivelloCaffe(entity.getLivelloCaffe());
        dto.setLivelloTe(entity.getLivelloTe());
        dto.setLivelloLatte(entity.getLivelloLatte());
        dto.setLivelloCioccolato(entity.getLivelloCioccolato());
        dto.setLivelloBicchieri(entity.getLivelloBicchieri());
        dto.setLivelloPalette(entity.getLivelloPalette());
        dto.setLatitudine(entity.getLatitudine());
        dto.setLongitudine(entity.getLongitudine());
        return dto;
    }

    public static ManutentoreDTO toManutentoreDTO(Utente entity) {
        if (entity == null || !"MANUTENTORE".equals(entity.getRuolo())) return null;
        ManutentoreDTO dto = new ManutentoreDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        return dto;
    }
}
