import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sala {
    private int numero;
    private String bloco;
    private int capacidadeMaxima;
    private String tipoSala;
    private Veterinario veterinarioResponsavel;
    private final List<Atendimento> atendimentos = new ArrayList<>();

    public Sala(int numero, String bloco, int capacidadeMaxima, String tipoSala) {
        this.numero = numero;
        this.bloco = bloco;
        this.capacidadeMaxima = capacidadeMaxima;
        this.tipoSala = tipoSala;
    }

    public int getNumero() { return numero; }
    public String getBloco() { return bloco; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public String getTipoSala() { return tipoSala; }
    public Veterinario getVeterinarioResponsavel() { return veterinarioResponsavel; }

    public void setVeterinarioResponsavel(Veterinario veterinarioResponsavel) {
        this.veterinarioResponsavel = veterinarioResponsavel;
    }

    public List<Atendimento> getAtendimentos() {
        return Collections.unmodifiableList(atendimentos);
    }

    public void adicionarAtendimento(Atendimento atendimento) {
        if (veterinarioResponsavel == null) {
            throw new IllegalStateException("A sala " + numero + " não possui veterinário responsável. Use a opção 2 antes.");
        }
        if (atendimento.getStatus() == StatusAtendimento.AGENDADO) {
            throw new IllegalStateException("Atendimentos agendados não podem ter sala atribuída.");
        }
        if (atendimento.getSala() != null) {
            throw new IllegalStateException("O atendimento já está atribuído à sala " + atendimento.getSala().getNumero() + ".");
        }
        if (!atendimentos.isEmpty()) {
            String procedimentoDaSala = atendimentos.get(0).getProcedimento().getNome();
            if (!procedimentoDaSala.equalsIgnoreCase(atendimento.getProcedimento().getNome())) {
                throw new IllegalStateException("Esta sala só recebe atendimentos do procedimento \"" + procedimentoDaSala + "\".");
            }
        }
        if (atendimento.getStatus() == StatusAtendimento.EM_ANDAMENTO
                && contarPorStatus(StatusAtendimento.EM_ANDAMENTO) >= capacidadeMaxima) {
            throw new IllegalStateException("A sala " + numero + " atingiu a capacidade máxima de " + capacidadeMaxima + " animais.");
        }
        atendimentos.add(atendimento);
        atendimento.setSala(this);
    }

    public int contarPorStatus(StatusAtendimento status) {
        int total = 0;
        for (Atendimento a : atendimentos) {
            if (a.getStatus() == status) {
                total++;
            }
        }
        return total;
    }

    @Override
    public String toString() {
        String vet = (veterinarioResponsavel == null) ? "sem veterinário" : veterinarioResponsavel.getNome();
        return String.format("Sala %d | Bloco %s | Capacidade: %d | Tipo: %s | Responsável: %s", numero, bloco, capacidadeMaxima, tipoSala, vet);
    }
}
