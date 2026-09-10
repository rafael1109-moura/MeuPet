package com.meupet.repository;

import com.meupet.model.Animal;
import com.meupet.model.Cachorro;
import com.meupet.model.Tarefa;
import com.meupet.model.Tarefa.Categoria;
import com.meupet.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties",
        properties = "spring.sql.init.mode=never")
class TarefaRepositoryTest {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Long usuarioId;

    private Usuario novoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Dono"); usuario.setEmail("dono" + System.nanoTime() + "@teste.com");
        usuario.setSenha("x");
        return usuario;
    }

    @BeforeEach
    void criarUsuario() {
        Usuario salvo = usuarioRepository.save(novoUsuario());
        usuarioId = salvo.getId();
    }

    private void salvarAnimal(String nome) {
        Cachorro c = new Cachorro();
        c.setNome(nome); c.setIdade(3); c.setRaca(Cachorro.RacaCachorro.SRD);
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        c.setUsuario(usuario);
        animalRepository.save(c);
    }

    private void salvarTarefa(String titulo, LocalDate dataPrevista, boolean concluida, Animal animal, Categoria categoria) {
        Tarefa t = new Tarefa();
        t.setTitulo(titulo); t.setDataPrevista(dataPrevista);
        t.setConcluida(concluida); t.setAnimal(animal); t.setCategoria(categoria);
        t.setPrioridade(Tarefa.Prioridade.MEDIA);
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        t.setUsuario(usuario);
        tarefaRepository.save(t);
    }

    @Test
    void buscarFiltradas_semFiltros_deveRetornarTodas() {
        Cachorro a = new Cachorro(); a.setNome("X"); a.setIdade(1); a.setRaca(Cachorro.RacaCachorro.SRD);
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        a.setUsuario(usuario);
        animalRepository.save(a);
        salvarTarefa("T1", LocalDate.of(2026, 9, 10), false, a, Categoria.PESSOAL);
        salvarTarefa("T2", LocalDate.of(2026, 9, 15), false, a, Categoria.CONSULTA);

        Page<Tarefa> result = tarefaRepository.buscarFiltradas(
                usuarioId, null, null, null, null, LocalDate.now(), PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void buscarFiltradas_porAnimalId_deveFiltrar() {
        Cachorro a = new Cachorro(); a.setNome("X"); a.setIdade(1); a.setRaca(Cachorro.RacaCachorro.SRD);
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        a.setUsuario(usuario);
        animalRepository.save(a);
        salvarTarefa("T1", LocalDate.now(), false, a, Categoria.PESSOAL);
        salvarTarefa("T2", LocalDate.now(), false, a, Categoria.VACINACAO);

        Page<Tarefa> result = tarefaRepository.buscarFiltradas(
                usuarioId, a.getId(), Categoria.PESSOAL, null, null, LocalDate.now(), PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("T1", result.getContent().get(0).getTitulo());
    }

    @Test
    void buscarFiltradas_porCategoria_deveFiltrar() {
        Cachorro a = new Cachorro(); a.setNome("X"); a.setIdade(1); a.setRaca(Cachorro.RacaCachorro.SRD);
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        a.setUsuario(usuario);
        animalRepository.save(a);
        salvarTarefa("T1", LocalDate.now(), false, a, Categoria.PESSOAL);
        salvarTarefa("T2", LocalDate.now(), false, a, Categoria.VACINACAO);

        Page<Tarefa> result = tarefaRepository.buscarFiltradas(
                usuarioId, null, Categoria.VACINACAO, null, null, LocalDate.now(), PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(Categoria.VACINACAO, result.getContent().get(0).getCategoria());
    }

    @Test
    void buscarFiltradas_concluidaFalse_deveRetornarPendentes() {
        Cachorro a = new Cachorro(); a.setNome("X"); a.setIdade(1); a.setRaca(Cachorro.RacaCachorro.SRD);
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        a.setUsuario(usuario);
        animalRepository.save(a);
        salvarTarefa("Pendente", LocalDate.now(), false, a, Categoria.PESSOAL);
        salvarTarefa("Concluida", LocalDate.now(), true, a, Categoria.PESSOAL);

        Page<Tarefa> result = tarefaRepository.buscarFiltradas(
                usuarioId, null, null, false, null, LocalDate.now(), PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Pendente", result.getContent().get(0).getTitulo());
    }

    @Test
    void buscarFiltradas_apenasAtrasadas_deveRetornarSoAtrasadas() {
        Cachorro a = new Cachorro(); a.setNome("X"); a.setIdade(1); a.setRaca(Cachorro.RacaCachorro.SRD);
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        a.setUsuario(usuario);
        animalRepository.save(a);
        salvarTarefa("Atrasada", LocalDate.of(2026, 1, 1), false, a, Categoria.PESSOAL);
        salvarTarefa("Futura", LocalDate.of(2026, 12, 31), false, a, Categoria.PESSOAL);
        salvarTarefa("AtrasadaConcluida", LocalDate.of(2026, 1, 1), true, a, Categoria.PESSOAL);

        Page<Tarefa> result = tarefaRepository.buscarFiltradas(
                usuarioId, null, null, null, true, LocalDate.now(), PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Atrasada", result.getContent().get(0).getTitulo());
    }

    @Test
    void buscarFiltradas_combinacaoFiltros_deveFuncionar() {
        Cachorro a1 = new Cachorro(); a1.setNome("Thor"); a1.setIdade(3); a1.setRaca(Cachorro.RacaCachorro.SRD);
        Cachorro a2 = new Cachorro(); a2.setNome("Mel"); a2.setIdade(1); a2.setRaca(Cachorro.RacaCachorro.SRD);
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        a1.setUsuario(usuario); a2.setUsuario(usuario);
        animalRepository.save(a1); animalRepository.save(a2);
        salvarTarefa("Passear", LocalDate.of(2026, 9, 16), false, a1, Categoria.PESSOAL);
        salvarTarefa("Racao", LocalDate.of(2026, 9, 10), false, a1, Categoria.PESSOAL);
        salvarTarefa("Consulta", LocalDate.of(2026, 9, 20), false, a1, Categoria.CONSULTA);
        salvarTarefa("Outra", LocalDate.now(), false, a2, Categoria.PESSOAL);

        Page<Tarefa> result = tarefaRepository.buscarFiltradas(
                usuarioId, a1.getId(), Categoria.PESSOAL, false, null, LocalDate.now(), PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void buscarFiltradas_deveIsolarPorUsuario() {
        Usuario outro = usuarioRepository.save(novoUsuario());

        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        Cachorro meuAnimal = new Cachorro();
        meuAnimal.setNome("Meu"); meuAnimal.setIdade(1); meuAnimal.setRaca(Cachorro.RacaCachorro.SRD);
        meuAnimal.setUsuario(usuario);
        animalRepository.save(meuAnimal);

        Usuario donoOutro = new Usuario();
        ReflectionTestUtils.setField(donoOutro, "id", outro.getId());
        Cachorro animalDoOutro = new Cachorro();
        animalDoOutro.setNome("DoOutro"); animalDoOutro.setIdade(1); animalDoOutro.setRaca(Cachorro.RacaCachorro.SRD);
        animalDoOutro.setUsuario(donoOutro);
        animalRepository.save(animalDoOutro);

        salvarTarefa("Minha tarefa", LocalDate.now(), false, meuAnimal, Categoria.PESSOAL);

        Tarefa t = new Tarefa();
        t.setTitulo("Tarefa do outro"); t.setDataPrevista(LocalDate.now());
        t.setConcluida(false); t.setAnimal(animalDoOutro); t.setCategoria(Categoria.PESSOAL);
        t.setPrioridade(Tarefa.Prioridade.MEDIA);
        t.setUsuario(donoOutro);
        tarefaRepository.save(t);

        Page<Tarefa> result = tarefaRepository.buscarFiltradas(
                usuarioId, null, null, null, null, LocalDate.now(), PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Minha tarefa", result.getContent().get(0).getTitulo());
    }

    @Test
    void findByIdAndUsuarioId_deveIsolarPorUsuario() {
        Usuario outro = usuarioRepository.save(novoUsuario());

        Tarefa t = new Tarefa();
        t.setTitulo("Privada"); t.setDataPrevista(LocalDate.now());
        t.setConcluida(false); t.setCategoria(Categoria.PESSOAL);
        t.setPrioridade(Tarefa.Prioridade.MEDIA);
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", usuarioId);
        t.setUsuario(usuario);
        Tarefa salva = tarefaRepository.save(t);

        assertTrue(tarefaRepository.findByIdAndUsuarioId(salva.getId(), usuarioId).isPresent());
        assertTrue(tarefaRepository.findByIdAndUsuarioId(salva.getId(), outro.getId()).isEmpty());
    }
}