package com.customerpriority.sig.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.customerpriority.sig.model.Segmento;
import com.customerpriority.sig.model.TipoGestion;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SegmentoRepository extends JpaRepository<Segmento, Integer> {

    @Query("SELECT s FROM Segmento s " +
       "JOIN s.campana c " +
       "JOIN s.tipoGestion g " +
       "WHERE LOWER(s.nombreSegmento) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
       "OR LOWER(c.nombreCampana) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
       "OR LOWER(g.nombreGestion) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Segmento> buscarPorSegmentoNombreOGestion(@Param("keyword") String keyword, Pageable pageable);

    List<Segmento> findByCampanaIdCampana(int idCampana);

    @Query("SELECT s.tipoGestion FROM Segmento s WHERE s.idSegmento = :idSegmento")
    List<TipoGestion> findTipoGestionBySegmentoId(@Param("idSegmento") int idSegmento);

    // Nuevo método para filtrar segmentos activos (estado = 1)
    List<Segmento> findByEstado(int estado);
    
    // Nuevo método para filtrar segmentos activos por campaña
    List<Segmento> findByCampanaIdCampanaAndEstado(int idCampana, int estado);

}