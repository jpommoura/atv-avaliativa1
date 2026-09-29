public class Veterinario {
    private String nome;
    private String cpf;
    private String especialidade;
    private String telefone;

    public Veterinario(String nome, String cpf, String especialidade, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.especialidade = especialidade;
        this.telefone = telefone;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEspecialidade() { return especialidade; }
    public String getTelefone() { return telefone; }

    @Override
    public String toString() {
        return String.format("%s | CPF: %s | Especialidade: %s | Tel: %s", nome, cpf, especialidade, telefone);
    }
}
