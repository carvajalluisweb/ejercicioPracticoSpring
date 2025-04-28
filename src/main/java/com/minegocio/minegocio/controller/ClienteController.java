package com.minegocio.minegocio.controller;

import com.minegocio.minegocio.dto.ClienteCompletoDTO;
import com.minegocio.minegocio.dto.ClienteDTO;
import com.minegocio.minegocio.dto.ClienteDireccionDTO;
import com.minegocio.minegocio.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

   @PostMapping
   public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        ClienteDTO nuevoCliente = clienteService.crearCliente(clienteDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }
    @GetMapping("/listarClientes")
    public ResponseEntity<List<ClienteCompletoDTO>> listarClientes() {
        List<ClienteCompletoDTO> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{identificacion}")
    public ResponseEntity<ClienteDTO> editarCliente(@PathVariable String identificacion, @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO clienteActualizado = clienteService.editarCliente(identificacion, clienteDTO);
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/{identificacion}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable String identificacion) {
        clienteService.eliminarCliente(identificacion);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{identificacion}")
    public ResponseEntity<List<ClienteCompletoDTO>> buscarClientes(@PathVariable String identificacion) {
        List<ClienteCompletoDTO> clientes = clienteService.buscarClientes(identificacion);
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/{identificacion}/direcciones")
    public ResponseEntity<ClienteDireccionDTO> agregarDireccion(@PathVariable String identificacion, @RequestBody ClienteDireccionDTO direccionDTO) {
        ClienteDireccionDTO nuevaDireccion = clienteService.agregarDireccion(identificacion, direccionDTO);
        return new ResponseEntity<>(nuevaDireccion, HttpStatus.CREATED);
    }

    @GetMapping("/{identificacion}/direcciones")
    public ResponseEntity<List<ClienteDireccionDTO>> listarDirecciones(@PathVariable String identificacion) {
        List<ClienteDireccionDTO> direcciones = clienteService.listarDirecciones(identificacion);
        return ResponseEntity.ok(direcciones);
    }
}
