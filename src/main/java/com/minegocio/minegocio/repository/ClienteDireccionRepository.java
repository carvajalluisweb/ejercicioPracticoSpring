package com.minegocio.minegocio.repository;

import com.minegocio.minegocio.entity.ClienteDireccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteDireccionRepository extends JpaRepository<ClienteDireccion, Long> {

    List<ClienteDireccion> findByClienteId(Long clienteId);
    boolean existsByClienteNumeroIdentificacionAndEsMatrizTrue(String identificacion);
}
