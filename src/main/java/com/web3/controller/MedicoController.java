package com.web3.controller;

import com.web3.model.*;
import com.web3.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final ReceituarioRepository receituarioRepository;
    private final ItemReceituarioRepository itemReceituarioRepository;
    private final MedicamentoRepository medicamentoRepository;

    public MedicoController(MedicoRepository medicoRepository,
                            ConsultaRepository consultaRepository,
                            ProntuarioRepository prontuarioRepository,
                            ReceituarioRepository receituarioRepository,
                            ItemReceituarioRepository itemReceituarioRepository,
                            MedicamentoRepository medicamentoRepository) {
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
        this.prontuarioRepository = prontuarioRepository;
        this.receituarioRepository = receituarioRepository;
        this.itemReceituarioRepository = itemReceituarioRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "medico/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String crm,
                        @RequestParam String senha,
                        HttpSession session,
                        RedirectAttributes ra) {
        Optional<Medico> medicoOpt = medicoRepository.findByCrmAndSenha(crm, senha);
        if (medicoOpt.isPresent()) {
            session.setAttribute("medicoLogado", medicoOpt.get());
            return "redirect:/medico/home";
        }
        ra.addFlashAttribute("erro", "CRM ou senha inválidos!");
        return "redirect:/medico/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/medico/login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model, RedirectAttributes ra) {
        Medico medico = (Medico) session.getAttribute("medicoLogado");
        if (medico == null) {
            ra.addFlashAttribute("erro", "Faça login para acessar!");
            return "redirect:/medico/login";
        }
        model.addAttribute("medico", medico);
        model.addAttribute("consultasPendentes",
            consultaRepository.findPendentesByMedicoCrm(medico.getCrm()));
        return "medico/home";
    }

    @GetMapping("/consulta/{codigo}")
    public String detalheConsulta(@PathVariable int codigo,
                                   HttpSession session,
                                   Model model,
                                   RedirectAttributes ra) {
        Medico medico = (Medico) session.getAttribute("medicoLogado");
        if (medico == null) {
            ra.addFlashAttribute("erro", "Faça login para acessar!");
            return "redirect:/medico/login";
        }

        Optional<Consulta> consultaOpt = consultaRepository.findById(codigo);
        if (consultaOpt.isEmpty()) {
            ra.addFlashAttribute("erro", "Consulta não encontrada!");
            return "redirect:/medico/home";
        }

        Consulta consulta = consultaOpt.get();
        if (!consulta.getMedicoCrm().equals(medico.getCrm())) {
            ra.addFlashAttribute("erro", "Acesso negado a esta consulta!");
            return "redirect:/medico/home";
        }

        model.addAttribute("consulta", consulta);
        model.addAttribute("temProntuario",
            prontuarioRepository.findByConsultaCodigo(codigo).isPresent());
        model.addAttribute("medicamentos", medicamentoRepository.findAll());

        return "medico/consulta-detalhe";
    }

    @PostMapping("/consulta/{codigo}/prontuario")
    public String salvarProntuario(@PathVariable int codigo,
                                    @RequestParam String descricao,
                                    @RequestParam(required = false) String observacao,
                                    @RequestParam(required = false) String receituarioObservacao,
                                    @RequestParam(required = false) List<Integer> medicamentoCodigo,
                                    @RequestParam(required = false) List<Integer> medicamentoDosagem,
                                    @RequestParam(required = false) List<Integer> medicamentoIntervalo,
                                    @RequestParam(required = false) List<String> medicamentoObservacao,
                                    HttpSession session,
                                    RedirectAttributes ra) {
        Medico medico = (Medico) session.getAttribute("medicoLogado");
        if (medico == null) {
            ra.addFlashAttribute("erro", "Faça login para acessar!");
            return "redirect:/medico/login";
        }

        Prontuario prontuario = new Prontuario();
        prontuario.setDescricao(descricao);
        prontuario.setObservacao(observacao);
        prontuario.setConsultaCodigo(codigo);
        Prontuario savedProntuario = prontuarioRepository.save(prontuario);

        boolean temMedicamentos = medicamentoCodigo != null && !medicamentoCodigo.isEmpty() && medicamentoCodigo.stream().anyMatch(c -> c != null);
        boolean temReceituario = (receituarioObservacao != null && !receituarioObservacao.isBlank()) || temMedicamentos;

        if (temReceituario) {
            Receituario receituario = new Receituario();
            receituario.setObservacao(receituarioObservacao);
            receituario.setProntuarioCodigo(savedProntuario.getCodigo());
            Receituario savedReceituario = receituarioRepository.save(receituario);

            if (medicamentoCodigo != null) {
                for (int i = 0; i < medicamentoCodigo.size(); i++) {
                    ItemReceituario item = new ItemReceituario();
                    item.setDosagem(
                        medicamentoDosagem != null && i < medicamentoDosagem.size()
                            ? medicamentoDosagem.get(i) : 0);
                    item.setIntervaloEntreDoses(
                        medicamentoIntervalo != null && i < medicamentoIntervalo.size()
                            ? medicamentoIntervalo.get(i) : 0);
                    item.setObservacao(
                        medicamentoObservacao != null && i < medicamentoObservacao.size()
                            ? medicamentoObservacao.get(i) : "");
                    item.setReceituarioCodigo(savedReceituario.getCodigo());
                    item.setMedicamentoCodigo(medicamentoCodigo.get(i));
                    itemReceituarioRepository.save(item);
                }
            }
        }

        ra.addFlashAttribute("mensagem", "Prontuário cadastrado com sucesso!");
        return "redirect:/medico/home";
    }

    @GetMapping("/consultas-realizadas")
    public String consultasRealizadas(HttpSession session, Model model, RedirectAttributes ra) {
        Medico medico = (Medico) session.getAttribute("medicoLogado");
        if (medico == null) {
            ra.addFlashAttribute("erro", "Faça login para acessar!");
            return "redirect:/medico/login";
        }
        model.addAttribute("medico", medico);
        model.addAttribute("consultasRealizadas",
            consultaRepository.findRealizadasByMedicoCrm(medico.getCrm()));
        return "medico/consultas-realizadas";
    }

    @GetMapping("/prontuario/{consultaCodigo}")
    public String verProntuario(@PathVariable int consultaCodigo,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes ra) {
        Medico medico = (Medico) session.getAttribute("medicoLogado");
        if (medico == null) {
            ra.addFlashAttribute("erro", "Faça login para acessar!");
            return "redirect:/medico/login";
        }

        Optional<Prontuario> prontuarioOpt = prontuarioRepository.findByConsultaCodigo(consultaCodigo);
        if (prontuarioOpt.isEmpty()) {
            ra.addFlashAttribute("erro", "Prontuário não encontrado!");
            return "redirect:/medico/consultas-realizadas";
        }

        Prontuario prontuario = prontuarioOpt.get();
        model.addAttribute("prontuario", prontuario);
        model.addAttribute("consulta", consultaRepository.findById(consultaCodigo).orElse(null));

        Optional<Receituario> receituarioOpt =
            receituarioRepository.findByProntuarioCodigo(prontuario.getCodigo());

        if (receituarioOpt.isPresent()) {
            Receituario receituario = receituarioOpt.get();
            model.addAttribute("receituario", receituario);
            model.addAttribute("itensReceituario",
                itemReceituarioRepository.findByReceituarioCodigo(receituario.getCodigo()));
        }

        return "medico/prontuario-detalhe";
    }
}
