package com.minegocio.minegocio.service;

import com.minegocio.minegocio.dto.ClienteCompletoDTO;
import com.minegocio.minegocio.dto.ClienteDTO;
import com.minegocio.minegocio.dto.ClienteDireccionDTO;
import com.minegocio.minegocio.entity.Cliente;
import com.minegocio.minegocio.entity.ClienteDireccion;
import com.minegocio.minegocio.exception.DuplicateResourceException;
import com.minegocio.minegocio.exception.ResourceNotFoundException;
import com.minegocio.minegocio.repository.ClienteDireccionRepository;
import com.minegocio.minegocio.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteDireccionRepository clienteDireccionRepository;

    @Override
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        if (clienteRepository.findByNumeroIdentificacion(clienteDTO.getNumeroIdentificacion()).isPresent()) {
            throw new DuplicateResourceException("El cliente ya está registrado.");
        }

        Cliente cliente = Cliente.builder()
                .tipoIdentificacion(clienteDTO.getTipoIdentificacion())
                .numeroIdentificacion(clienteDTO.getNumeroIdentificacion())
                .nombres(clienteDTO.getNombres())
                .correo(clienteDTO.getCorreo())
                .celular(clienteDTO.getCelular())
                .build();

        Cliente clienteGuardado = clienteRepository.save(cliente);

        if (clienteDTO.getDirecciones() != null) {
            for (ClienteDireccionDTO dirDTO : clienteDTO.getDirecciones()) {
                if (Boolean.TRUE.equals(dirDTO.getEsMatriz())) {
                    ClienteDireccion direccion = ClienteDireccion.builder()
                            .provincia(dirDTO.getProvincia())
                            .ciudad(dirDTO.getCiudad())
                            .direccion(dirDTO.getDireccion())
                            .esMatriz(true)
                            .cliente(clienteGuardado)
                            .build();
                    clienteDireccionRepository.save(direccion);
                }
            }
        }

        return mapClienteToDTO(clienteGuardado);
    }
    @Override
    public List<ClienteCompletoDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(this::mapClienteToDTOConMatriz)
                .collect(Collectors.toList());
    }
    @Override
    public ClienteDTO editarCliente(String identificacion, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findByNumeroIdentificacion(identificacion)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente " + identificacion + "no se encuentra registrado."));

        Optional<Cliente> clienteExistente = clienteRepository.findByNumeroIdentificacion(clienteDTO.getNumeroIdentificacion());
        if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(cliente.getId())) {
            throw new DuplicateResourceException("El número de identificación ya se encuentra registraado.");
        }

        cliente.setTipoIdentificacion(clienteDTO.getTipoIdentificacion());
        cliente.setNumeroIdentificacion(clienteDTO.getNumeroIdentificacion());
        cliente.setNombres(clienteDTO.getNombres());
        cliente.setCorreo(clienteDTO.getCorreo());
        cliente.setCelular(clienteDTO.getCelular());

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return mapClienteToDTO(clienteActualizado);
    }

    @Override
    public void eliminarCliente(String identificacion) {
        Cliente cliente = clienteRepository.findByNumeroIdentificacion(identificacion)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente " + identificacion + "no se encuentra registrado."));
        clienteRepository.delete(cliente);
    }


    @Override
    public List<ClienteCompletoDTO> buscarClientes(String identificacion) {
        List<Cliente> clientes = clienteRepository.findByNombresContainingIgnoreCaseOrNumeroIdentificacionContainingIgnoreCase(identificacion, identificacion);
        return clientes.stream()
                .map(this::mapClienteToDTOConMatriz)
                .collect(Collectors.toList());
    }

    private ClienteCompletoDTO mapClienteToDTOConMatriz(Cliente cliente) {
        ClienteDireccion direccionMatriz = cliente.getDirecciones().stream()
                .filter(ClienteDireccion::getEsMatriz)
                .findFirst()
                .orElse(null);

        return ClienteCompletoDTO.builder()
                .id(cliente.getId())
                .tipoIdentificacion(cliente.getTipoIdentificacion())
                .numeroIdentificacion(cliente.getNumeroIdentificacion())
                .nombres(cliente.getNombres())
                .correo(cliente.getCorreo())
                .celular(cliente.getCelular())
                .provincia(direccionMatriz != null ? direccionMatriz.getProvincia() : null)
                .ciudad(direccionMatriz != null ? direccionMatriz.getCiudad() : null)
                .direccion(direccionMatriz != null ? direccionMatriz.getDireccion() : null)
                .build();
    }




    @Override
    public ClienteDireccionDTO agregarDireccion(String identificacion, ClienteDireccionDTO direccionDTO) {
        Cliente cliente = clienteRepository.findByNumeroIdentificacion(identificacion)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente " + identificacion + "no se encuentra registrado."));

        if (Boolean.TRUE.equals(direccionDTO.getEsMatriz())) {
            if (clienteDireccionRepository.existsByClienteNumeroIdentificacionAndEsMatrizTrue(identificacion)) {
                throw new RuntimeException("El cliente ya cuenta con una dirección matriz registrada.");
            }
        }

        ClienteDireccion direccion = ClienteDireccion.builder()
                .provincia(direccionDTO.getProvincia())
                .ciudad(direccionDTO.getCiudad())
                .direccion(direccionDTO.getDireccion())
                .esMatriz(direccionDTO.getEsMatriz())
                .cliente(cliente)
                .build();

        clienteDireccionRepository.save(direccion);

        return mapDireccionToDTO(direccion);
    }

    @Override
    public List<ClienteDireccionDTO> listarDirecciones(String identificacion) {
        Cliente cliente = clienteRepository.findByNumeroIdentificacion(identificacion)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente " + identificacion + "no se encuentra registrado."));

        List<ClienteDireccion> direcciones = clienteDireccionRepository.findByClienteId(cliente.getId());
        return direcciones.stream()
                .map(this::mapDireccionToDTO)
                .collect(Collectors.toList());
    }
    private ClienteDTO mapClienteToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setTipoIdentificacion(cliente.getTipoIdentificacion());
        dto.setNumeroIdentificacion(cliente.getNumeroIdentificacion());
        dto.setNombres(cliente.getNombres());
        dto.setCorreo(cliente.getCorreo());
        dto.setCelular(cliente.getCelular());
        return dto;
    }

    private ClienteDireccionDTO mapDireccionToDTO(ClienteDireccion direccion) {
        ClienteDireccionDTO dto = new ClienteDireccionDTO();
        dto.setId(direccion.getId());
        dto.setProvincia(direccion.getProvincia());
        dto.setCiudad(direccion.getCiudad());
        dto.setDireccion(direccion.getDireccion());
        dto.setEsMatriz(direccion.getEsMatriz());
        return dto;
    }
}

