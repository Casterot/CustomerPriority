package com.customerpriority.sig.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.customerpriority.sig.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
    Page<Usuario> findByUsernameContainingIgnoreCase(String keyword, Pageable pageable);
    
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.trabajador.nombreCompleto) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.trabajador.apellidoPaterno) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.trabajador.apellidoMaterno) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Usuario> buscarPorKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.trabajador.nombreCompleto) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.trabajador.apellidoPaterno) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.trabajador.apellidoMaterno) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Usuario> buscarPorKeywordParaExportar(@Param("keyword") String keyword);

    List<Usuario> findByUsernameContainingIgnoreCase(String keyword);
}
