package com.minegocio.minegocio.repository;

import com.minegocio.minegocio.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByNumeroIdentificacion(String identificacion);
    List<Cliente> findByNombresContainingIgnoreCaseOrNumeroIdentificacionContainingIgnoreCase(String nombres, String identificacion);
}
