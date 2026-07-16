package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.ClienteDTO;
import jhony.ruiz.sigevi.model.Cliente;
import jhony.ruiz.sigevi.service.IClienteService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {
    private final IClienteService clienteService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<ClienteDTO> list = mapperUtil.mapList(clienteService.findAll(), ClienteDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable("id") Integer id) {
        Cliente obj = clienteService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, ClienteDTO.class));
    }

    @GetMapping("/documento/{numeroDocumento}")
    public ResponseEntity<ClienteDTO> findByNumeroDocumento(@PathVariable String numeroDocumento) {
        return clienteService.findByNumeroDocumento(numeroDocumento)
                .map(cliente -> ResponseEntity.ok(mapperUtil.map(cliente, ClienteDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> save(@Valid @RequestBody ClienteDTO dto) {
        Cliente obj = clienteService.save(mapperUtil.map(dto, Cliente.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdCliente()).toUri();
        return ResponseEntity.created(location).body(mapperUtil.map(obj, ClienteDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody ClienteDTO dto) {
        dto.setIdCliente(id);
        Cliente obj = clienteService.update(id, mapperUtil.map(dto, Cliente.class));
        return ResponseEntity.ok(mapperUtil.map(obj, ClienteDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
