package com.minegocio.minegocio.service;

import com.minegocio.minegocio.dto.ClienteCompletoDTO;
import com.minegocio.minegocio.dto.ClienteDTO;
import com.minegocio.minegocio.dto.ClienteDireccionDTO;

import java.util.List;

public interface ClienteService {

    ClienteDTO crearCliente(ClienteDTO clienteDTO);
    List<ClienteCompletoDTO> listarClientes();
    ClienteDTO editarCliente(String identificiacion, ClienteDTO clienteDTO);
    void eliminarCliente(String identificacion);
    List<ClienteCompletoDTO> buscarClientes(String identificacion);
    ClienteDireccionDTO agregarDireccion(String identificacion, ClienteDireccionDTO clienteDireccionDTO);
    List<ClienteDireccionDTO> listarDirecciones(String identificacion);
}
