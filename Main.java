import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        Clinica clinica = new Clinica();
        popularDadosIniciais(clinica);

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            try {
                executarOpcao(opcao, clinica);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
        System.out.println("Sistema encerrado.");
    }

    private static void popularDadosIniciais(Clinica clinica) {
        clinica.cadastrarVeterinario(new Veterinario("Dra. Ana Souza", "111.111.111-11", "Clínica geral", "(31) 99999-1111"));
        clinica.cadastrarVeterinario(new Veterinario("Dr. Carlos Lima", "222.222.222-22", "Cirurgia", "(31) 99999-2222"));
        clinica.cadastrarVeterinario(new Veterinario("Dra. Mariana Alves", "333.333.333-33", "Dermatologia", "(31) 99999-3333"));

        clinica.cadastrarSala(new Sala(1, "A", 3, "Consultório"));
        clinica.cadastrarSala(new Sala(2, "A", 2, "Cirurgia"));
        clinica.cadastrarSala(new Sala(3, "B", 4, "Internação"));
    }

    private static void exibirMenu() {
        System.out.println("1 - Cadastrar atendimento");
        System.out.println("2 - Associar veterinário a uma sala");
        System.out.println("3 - Atribuir atendimento a uma sala");
        System.out.println("4 - Exibir atendimentos de uma sala");
        System.out.println("5 - Total de atendimentos finalizados por sala");
        System.out.println("6 - Buscar atendimentos por status");
        System.out.println("7 - Exibir detalhes de um atendimento");
        System.out.println("0 - Sair");
    }

    private static void executarOpcao(int opcao, Clinica clinica) {
        switch (opcao) {
            case 1: cadastrarAtendimento(clinica); break;
            case 2: associarVeterinario(clinica); break;
            case 3: atribuirAtendimento(clinica); break;
            case 4: exibirAtendimentosDaSala(clinica); break;
            case 5: exibirFinalizadosPorSala(clinica); break;
            case 6: buscarPorStatus(clinica); break;
            case 7: exibirDetalhes(clinica); break;
            case 0: break;
            default: System.out.println("Opção inválida.");
        }
    }

    private static void cadastrarAtendimento(Clinica clinica) {
        System.out.println("Cadastro");
        String animal = lerTexto("Nome do animal: ");
        String especie = lerTexto("Espécie: ");
        String tutor = lerTexto("Nome do tutor: ");
        LocalDate data = lerData("Data (dd/MM/yyyy): ");
        LocalTime horario = lerHorario("Horário (HH:mm): ");
        StatusAtendimento status = lerStatus();
        String observacoes = lerTexto("Observações: ");

        String nomeProc = lerTexto("Nome do procedimento: ");
        int duracao = lerInteiro("Duração estimada (minutos): ");
        double valor = lerDouble("Valor (R$): ");
        NivelComplexidade nivel = lerNivel();

        Procedimento procedimento = new Procedimento(nomeProc, duracao, valor, nivel);
        Atendimento a = clinica.cadastrarAtendimento(animal, especie, tutor, data, horario, status, observacoes, procedimento);
        System.out.println("Atendimento cadastrado com código " + a.getCodigo() + ".");
    }

    private static void associarVeterinario(Clinica clinica) {
        System.out.println("Salas");
        for (Sala s : clinica.getSalas()) {
            System.out.println(s);
        }
        int numeroSala = lerInteiro("Número da sala: ");

        System.out.println("Veterinários");
        for (Veterinario v : clinica.getVeterinarios()) {
            System.out.println(v);
        }
        String cpf = lerTexto("CPF do veterinário: ");

        clinica.associarVeterinarioASala(numeroSala, cpf);
        System.out.println("Veterinário associado com sucesso.");
    }

    private static void atribuirAtendimento(Clinica clinica) {
        int codigo = lerInteiro("Código do atendimento: ");
        int numeroSala = lerInteiro("Número da sala: ");
        clinica.atribuirAtendimentoASala(codigo, numeroSala);
        System.out.println("Atendimento atribuído à sala com sucesso.");
    }

    private static void exibirAtendimentosDaSala(Clinica clinica) {
        int numeroSala = lerInteiro("Número da sala: ");
        List<Atendimento> lista = clinica.listarAtendimentosDaSala(numeroSala);
        for (Atendimento a : lista) {
            a.exibirResumo();
        }
        System.out.println("Total de atendimentos na sala " + numeroSala + ": " + lista.size());
    }

    private static void exibirFinalizadosPorSala(Clinica clinica) {
        for (Sala s : clinica.getSalas()) {
            System.out.println("Sala " + s.getNumero() + ": " + clinica.contarFinalizadosDaSala(s) + " finalizado(s)");
        }
    }

    private static void buscarPorStatus(Clinica clinica) {
        StatusAtendimento status = lerStatus();
        List<Atendimento> lista = clinica.buscarPorStatus(status);
        if (lista.isEmpty()) {
            System.out.println("Nenhum atendimento com status " + status.getDescricao() + ".");
            return;
        }
        for (Atendimento a : lista) {
            a.exibirDetalhes();
        }
    }

    private static void exibirDetalhes(Clinica clinica) {
        int codigo = lerInteiro("Código do atendimento: ");
        clinica.buscarAtendimento(codigo).exibirDetalhes();
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return SC.nextLine().trim();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(mensagem));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            try {
                return Double.parseDouble(lerTexto(mensagem).replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor numérico válido.");
            }
        }
    }

    private static LocalDate lerData(String mensagem) {
        while (true) {
            try {
                return LocalDate.parse(lerTexto(mensagem), FMT_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd/MM/yyyy.");
            }
        }
    }

    private static LocalTime lerHorario(String mensagem) {
        while (true) {
            try {
                return LocalTime.parse(lerTexto(mensagem), FMT_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("Horário inválido. Use o formato HH:mm.");
            }
        }
    }

    private static StatusAtendimento lerStatus() {
        StatusAtendimento[] valores = StatusAtendimento.values();
        while (true) {
            for (int i = 0; i < valores.length; i++) {
                System.out.println((i + 1) + " - " + valores[i].getDescricao());
            }
            int op = lerInteiro("Status: ");
            if (op >= 1 && op <= valores.length) {
                return valores[op - 1];
            }
            System.out.println("Opção inválida.");
        }
    }

    private static NivelComplexidade lerNivel() {
        NivelComplexidade[] valores = NivelComplexidade.values();
        while (true) {
            for (int i = 0; i < valores.length; i++) {
                System.out.println((i + 1) + " - " + valores[i].getDescricao());
            }
            int op = lerInteiro("Nível de complexidade: ");
            if (op >= 1 && op <= valores.length) {
                return valores[op - 1];
            }
            System.out.println("Opção inválida.");
        }
    }
}
