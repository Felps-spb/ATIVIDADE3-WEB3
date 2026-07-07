package com.web3.controller;

import com.web3.model.Consulta;
import com.web3.model.Medicamento;
import com.web3.model.Medico;
import com.web3.model.Paciente;
import com.web3.repository.ConsultaRepository;
import com.web3.repository.MedicamentoRepository;
import com.web3.repository.MedicoRepository;
import com.web3.repository.PacienteRepository;
import com.web3.repository.ProntuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/atendente")
public class AtendenteController {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final ConsultaRepository consultaRepository;
    private final ProntuarioRepository prontuarioRepository;

    public AtendenteController(PacienteRepository pacienteRepository,
                                MedicoRepository medicoRepository,
                                MedicamentoRepository medicamentoRepository,
                                ConsultaRepository consultaRepository,
                                ProntuarioRepository prontuarioRepository) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.consultaRepository = consultaRepository;
        this.prontuarioRepository = prontuarioRepository;
    }

    @GetMapping
    public String home() {
        return "atendente/home";
    }

    // ==================== PACIENTE ====================

    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "atendente/paciente/list";
    }

    @GetMapping("/pacientes/novo")
    public String formNovoPaciente(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "atendente/paciente/form";
    }

    @PostMapping("/pacientes")
    public String salvarPaciente(@ModelAttribute Paciente paciente, RedirectAttributes ra) {
        if (pacienteRepository.findById(paciente.getCpf()).isPresent()) {
            pacienteRepository.update(paciente);
            ra.addFlashAttribute("mensagem", "Paciente atualizado com sucesso!");
        } else {
            pacienteRepository.save(paciente);
            ra.addFlashAttribute("mensagem", "Paciente cadastrado com sucesso!");
        }
        return "redirect:/atendente/pacientes";
    }

    @GetMapping("/pacientes/{cpf}/editar")
    public String formEditarPaciente(@PathVariable String cpf, Model model) {
        pacienteRepository.findById(cpf).ifPresent(p -> model.addAttribute("paciente", p));
        return "atendente/paciente/form";
    }

    @GetMapping("/pacientes/{cpf}/excluir")
    public String excluirPaciente(@PathVariable String cpf, RedirectAttributes ra) {
        pacienteRepository.deleteById(cpf);
        ra.addFlashAttribute("mensagem", "Paciente excluído com sucesso!");
        return "redirect:/atendente/pacientes";
    }

    // ==================== MEDICO ====================

    @GetMapping("/medicos")
    public String listarMedicos(Model model) {
        model.addAttribute("medicos", medicoRepository.findAll());
        return "atendente/medico/list";
    }

    @GetMapping("/medicos/novo")
    public String formNovoMedico(Model model) {
        model.addAttribute("medico", new Medico());
        return "atendente/medico/form";
    }

    @PostMapping("/medicos")
    public String salvarMedico(@ModelAttribute Medico medico, RedirectAttributes ra) {
        if (medicoRepository.findById(medico.getCrm()).isPresent()) {
            medicoRepository.update(medico);
            ra.addFlashAttribute("mensagem", "Médico atualizado com sucesso!");
        } else {
            medicoRepository.save(medico);
            ra.addFlashAttribute("mensagem", "Médico cadastrado com sucesso!");
        }
        return "redirect:/atendente/medicos";
    }

    @GetMapping("/medicos/{crm}/editar")
    public String formEditarMedico(@PathVariable String crm, Model model) {
        medicoRepository.findById(crm).ifPresent(m -> model.addAttribute("medico", m));
        return "atendente/medico/form";
    }

    @GetMapping("/medicos/{crm}/excluir")
    public String excluirMedico(@PathVariable String crm, RedirectAttributes ra) {
        medicoRepository.deleteById(crm);
        ra.addFlashAttribute("mensagem", "Médico excluído com sucesso!");
        return "redirect:/atendente/medicos";
    }

    // ==================== MEDICAMENTO ====================

    @GetMapping("/medicamentos")
    public String listarMedicamentos(Model model) {
        model.addAttribute("medicamentos", medicamentoRepository.findAll());
        return "atendente/medicamento/list";
    }

    @GetMapping("/medicamentos/novo")
    public String formNovoMedicamento(Model model) {
        model.addAttribute("medicamento", new Medicamento());
        return "atendente/medicamento/form";
    }

    @PostMapping("/medicamentos")
    public String salvarMedicamento(@ModelAttribute Medicamento medicamento, RedirectAttributes ra) {
        if (medicamento.getCodigo() > 0) {
            medicamentoRepository.update(medicamento);
            ra.addFlashAttribute("mensagem", "Medicamento atualizado com sucesso!");
        } else {
            medicamentoRepository.save(medicamento);
            ra.addFlashAttribute("mensagem", "Medicamento cadastrado com sucesso!");
        }
        return "redirect:/atendente/medicamentos";
    }

    @GetMapping("/medicamentos/{codigo}/editar")
    public String formEditarMedicamento(@PathVariable int codigo, Model model) {
        medicamentoRepository.findById(codigo).ifPresent(m -> model.addAttribute("medicamento", m));
        return "atendente/medicamento/form";
    }

    @GetMapping("/medicamentos/{codigo}/excluir")
    public String excluirMedicamento(@PathVariable int codigo, RedirectAttributes ra) {
        medicamentoRepository.deleteById(codigo);
        ra.addFlashAttribute("mensagem", "Medicamento excluído com sucesso!");
        return "redirect:/atendente/medicamentos";
    }

    // ==================== CONSULTA ====================

    @GetMapping("/consultas")
    public String listarConsultas(Model model) {
        model.addAttribute("consultas", consultaRepository.findAll());
        return "atendente/consulta/list";
    }

    @GetMapping("/consultas/nova")
    public String formNovaConsulta(Model model) {
        model.addAttribute("consulta", new Consulta());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicoRepository.findAll());
        return "atendente/consulta/form";
    }

    @PostMapping("/consultas")
    public String salvarConsulta(@ModelAttribute Consulta consulta, RedirectAttributes ra) {
        consultaRepository.save(consulta);
        ra.addFlashAttribute("mensagem", "Consulta cadastrada com sucesso!");
        return "redirect:/atendente/consultas";
    }

    @GetMapping("/consultas/{codigo}/excluir")
    public String excluirConsulta(@PathVariable int codigo, RedirectAttributes ra) {
        if (prontuarioRepository.findByConsultaCodigo(codigo).isPresent()) {
            ra.addFlashAttribute("erro", "Não é possível excluir uma consulta que já possui prontuário!");
            return "redirect:/atendente/consultas";
        }
        consultaRepository.deleteById(codigo);
        ra.addFlashAttribute("mensagem", "Consulta excluída com sucesso!");
        return "redirect:/atendente/consultas";
    }
}
