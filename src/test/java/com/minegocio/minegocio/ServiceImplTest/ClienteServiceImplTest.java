package com.minegocio.minegocio.ServiceImplTest;

import com.minegocio.minegocio.dto.ClienteCompletoDTO;
import com.minegocio.minegocio.dto.ClienteDTO;
import com.minegocio.minegocio.dto.ClienteDireccionDTO;
import com.minegocio.minegocio.entity.Cliente;
import com.minegocio.minegocio.entity.ClienteDireccion;
import com.minegocio.minegocio.entity.TipoIdentificacion;
import com.minegocio.minegocio.exception.DuplicateResourceException;
import com.minegocio.minegocio.exception.ResourceNotFoundException;
import com.minegocio.minegocio.repository.ClienteDireccionRepository;
import com.minegocio.minegocio.repository.ClienteRepository;
import com.minegocio.minegocio.service.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClienteServiceImplTest {

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteDireccionRepository clienteDireccionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Cliente crearClienteMock() {
        return Cliente.builder()
                .id(1L)
                .tipoIdentificacion(TipoIdentificacion.RUC)
                .numeroIdentificacion("1234567890")
                .nombres("Empresa Test")
                .correo("test@empresa.com")
                .celular("0999999999")
                .direcciones(new ArrayList<>())
                .build();
    }

    @Test
    void crearCliente_deberiaCrearClienteCorrectamente() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setTipoIdentificacion(TipoIdentificacion.RUC);
        clienteDTO.setNumeroIdentificacion("1234567890");
        clienteDTO.setNombres("Empresa Test");
        clienteDTO.setCorreo("test@empresa.com");
        clienteDTO.setCelular("0999999999");

        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(i -> i.getArgument(0));

        ClienteDTO resultado = clienteService.crearCliente(clienteDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNumeroIdentificacion()).isEqualTo("1234567890");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void crearCliente_deberiaLanzarDuplicateResourceException() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNumeroIdentificacion("1234567890");

        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.of(crearClienteMock()));

        assertThrows(DuplicateResourceException.class, () -> clienteService.crearCliente(clienteDTO));
    }

    @Test
    void listarClientes_deberiaListarClientesCorrectamente() {
        when(clienteRepository.findAll())
                .thenReturn(Collections.singletonList(crearClienteMock()));

        List<ClienteCompletoDTO> clientes = clienteService.listarClientes();

        assertThat(clientes).isNotEmpty();
        assertThat(clientes.get(0).getNumeroIdentificacion()).isEqualTo("1234567890");
    }

    @Test
    void editarCliente_deberiaActualizarClienteCorrectamente() {
        Cliente cliente = crearClienteMock();

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setTipoIdentificacion(TipoIdentificacion.CEDULA);
        clienteDTO.setNumeroIdentificacion("0987654321");
        clienteDTO.setNombres("Empresa Actualizada");
        clienteDTO.setCorreo("nuevo@empresa.com");
        clienteDTO.setCelular("0991234567");

        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.of(cliente));
        when(clienteRepository.findByNumeroIdentificacion("0987654321"))
                .thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(i -> i.getArgument(0));

        ClienteDTO resultado = clienteService.editarCliente("1234567890", clienteDTO);

        assertThat(resultado.getNumeroIdentificacion()).isEqualTo("0987654321");
    }

    @Test
    void editarCliente_deberiaLanzarResourceNotFoundException() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNumeroIdentificacion("0987654321");

        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.editarCliente("1234567890", clienteDTO));
    }

    @Test
    void eliminarCliente_deberiaEliminarClienteCorrectamente() {
        Cliente cliente = crearClienteMock();

        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.of(cliente));

        clienteService.eliminarCliente("1234567890");

        verify(clienteRepository).delete(cliente);
    }

    @Test
    void eliminarCliente_deberiaLanzarResourceNotFoundException() {
        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.eliminarCliente("1234567890"));
    }

    @Test
    void buscarClientes_deberiaRetornarListaClientes() {
        when(clienteRepository.findByNombresContainingIgnoreCaseOrNumeroIdentificacionContainingIgnoreCase("busqueda", "busqueda"))
                .thenReturn(Collections.singletonList(crearClienteMock()));

        List<ClienteCompletoDTO> resultado = clienteService.buscarClientes("busqueda");

        assertThat(resultado).isNotEmpty();
    }

    @Test
    void agregarDireccion_deberiaAgregarDireccionCorrectamente() {
        Cliente cliente = crearClienteMock();
        ClienteDireccionDTO direccionDTO = new ClienteDireccionDTO();
        direccionDTO.setProvincia("Pichincha");
        direccionDTO.setCiudad("Quito");
        direccionDTO.setDireccion("Av. Amazonas");
        direccionDTO.setEsMatriz(false);

        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.of(cliente));
        when(clienteDireccionRepository.save(any(ClienteDireccion.class)))
                .thenAnswer(i -> i.getArgument(0));

        ClienteDireccionDTO resultado = clienteService.agregarDireccion("1234567890", direccionDTO);

        assertThat(resultado.getCiudad()).isEqualTo("Quito");
    }

    @Test
    void agregarDireccion_deberiaLanzarRuntimeExceptionSiYaExisteMatriz() {
        Cliente cliente = crearClienteMock();
        ClienteDireccionDTO direccionDTO = new ClienteDireccionDTO();
        direccionDTO.setEsMatriz(true);

        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.of(cliente));
        when(clienteDireccionRepository.existsByClienteNumeroIdentificacionAndEsMatrizTrue("1234567890"))
                .thenReturn(true);

        assertThrows(RuntimeException.class, () -> clienteService.agregarDireccion("1234567890", direccionDTO));
    }

    @Test
    void listarDirecciones_deberiaListarDireccionesCorrectamente() {
        Cliente cliente = crearClienteMock();
        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.of(cliente));
        when(clienteDireccionRepository.findByClienteId(1L))
                .thenReturn(Collections.singletonList(
                        ClienteDireccion.builder()
                                .id(1L)
                                .provincia("Pichincha")
                                .ciudad("Quito")
                                .direccion("Av. Amazonas")
                                .esMatriz(true)
                                .cliente(cliente)
                                .build()
                ));

        List<ClienteDireccionDTO> direcciones = clienteService.listarDirecciones("1234567890");

        assertThat(direcciones).isNotEmpty();
    }
}
