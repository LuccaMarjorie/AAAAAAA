// Importações:
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Classe base abstrata: Nota
abstract class Nota implements Serializable {
    // Atributos:
    protected String titulo;
    protected String conteudo;
    protected Date dataCriacao;
    protected Date dataAtualizacao;
    // Construtor: Permite a criação de novas notas
    public Nota(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
    }
    // Métodos abstratos: tem que existir mas cada um faz do seu jeito
    public abstract void salvar();

    public abstract void excluir();

// Getters e Setters
    public String getTitulo() { //get: pega o título (lê)
        return titulo;
    }

    public void setTitulo(String titulo) { // set: Muda o título (escreve)
        this.titulo = titulo;
        this.dataAtualizacao = new Date();
    }

    public String getConteudo() { //get: pega o conteúdo (lê)
        return conteudo;
    }

    public void setConteudo(String conteudo) { // set: Muda o conteúdo (escreve)
        this.conteudo = conteudo;
        this.dataAtualizacao = new Date();
    }

    public Date getDataCriacao() { //get: pega a data de criação (lê)
        return dataCriacao;
    }

    public Date getDataAtualizacao() { //get: pega a data de atualização (lê)
        return dataAtualizacao;
    }
}

// Subclasse: notas de texto (filha da classe Nota)
class NotaTexto extends Nota {
    // Atributo específico
    private String texto;
    // Construtor
    public NotaTexto(String titulo, String texto) {
        super(titulo, ""); // Chama o construtor da mãe (Nota)
        this.texto = texto; // Guarda o texto
    }
    // Método obrigatório (herdado da mãe)
    @Override
    public void salvar() {
    }

    @Override
    public void excluir() {
    }
    // Getter e Setter para o texto
    public String getTexto() {
        return texto; // Devolve o texto
    }

    public void setTexto(String texto) {
        this.texto = texto; // Muda o texto
        this.dataAtualizacao = new Date(); // Atualiza a data (AGORA)
    }
}

// Classe: itens do checklist
class ItemChecklist implements Serializable {
    // Atributos:
    private String descricao; // O que precisa fazer
    private boolean conclusao; // Já foi feito? (true = sim, false = não)
    // Construtor
    public ItemChecklist(String descricao) {
        this.descricao = descricao; // Guarda a descrição
        this.conclusao = false; // Começa como NÃO feito
    }
    // Getters e Setters
    public String getDescricao() {
        return descricao; // Pega a descrição
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao; // Muda a descrição
    }

    public boolean isConclusao() {
        return conclusao; // Pega se está concluído
    }

    public void setConclusao(boolean conclusao) {
        this.conclusao = conclusao; // Muda se está concluído
    }
}

// Subclasse: checklists
class Checklist extends Nota {
    // Atributo Específico
    private List<ItemChecklist> itens; // Lista de coisas para fazer
    // Construtor
    public Checklist(String titulo) {
        super(titulo, ""); // Chama construtor da mãe (Nota)
        this.itens = new ArrayList<>(); // Cria uma lista VAZIA de itens
    }

// MÉTODOS ESPECÍFICOS DE CHECKLIST:

    // Marca um item como FEITO
    public void marcarItem(int indice) { // Verifica se o índice existe 
        if (indice >= 0 && indice < itens.size()) {
            itens.get(indice).setConclusao(true); // Marca como concluído
            this.dataAtualizacao = new Date(); // Atualiza a data (AGORA)
        }
    }
    // Desmarca um item (volta para NÃO FEITO)
    public void desmarcarItem(int indice) {
        if (indice >= 0 && indice < itens.size()) {
            itens.get(indice).setConclusao(false); // Marca como NÃO concluído
            this.dataAtualizacao = new Date(); // Atualiza a data (AGORA)
        }
    }
    // Adiciona um NOVO item na lista
    public void adicionarItem(String descricao) {
        itens.add(new ItemChecklist(descricao)); // Cria e adiciona novo item
        this.dataAtualizacao = new Date(); // Atualiza a data (AGORA)
    }
    // Métodos obrigatórios (herdados da mãe)
    @Override
    public void salvar() {
    }

    @Override
    public void excluir() {
    }

    public List<ItemChecklist> getItens() {
        return itens; // Devolve a lista completa de itens
    }
}

// Subclasse: DataComemorativa
class DataComemorativa extends Nota {
    // Atributos Específicos
    private String ocasiao;
    private Date dataEvento;
    // Construtor
    public DataComemorativa(String titulo, String ocasiao, Date dataEvento) {
        super(titulo, ""); // Chama construtor da mãe (Nota)
        this.ocasiao = ocasiao; // Guarda a ocasião
        this.dataEvento = dataEvento; // Guarda a data do evento
    }
    // Métodos obrigatórios
    @Override
    public void salvar() {
    }

    @Override
    public void excluir() {
    }
    // Getters e Setters específicos:
    public String getOcasiao() {
        return ocasiao; // Pega a ocasião
    }

    public void setOcasiao(String ocasiao) {
        this.ocasiao = ocasiao; // Muda a ocasião
        this.dataAtualizacao = new Date(); // Atualiza a data (AGORA)
    }

    // Para data do evento
    public Date getDataEvento() {
        return dataEvento; // Pega a data do evento
    }

    public void setDataEvento(Date dataEvento) {
        this.dataEvento = dataEvento; // Muda a data do evento
        this.dataAtualizacao = new Date(); // Atualiza a data (AGORA)
    }
    // Formata a data bonitinha
    public String getDataEventoFormatada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/aaaa");
        return sdf.format(dataEvento); // Devolve a data formatada
    }
}

// Classe: Usuário
class Usuario implements Serializable {
    // Atributos
    private String nome; 
    private String senha;
    protected List<Nota> notas;
    // Construtor
    public Usuario(String nome, String senha) {
        this.nome = nome; // Guarda o nome
        this.senha = senha; // Guarda a senha
        this.notas = new ArrayList<>(); // Cria lista VAZIA de notas
    }
    // Método: Cria uma nova nota
    public void criarNota(Nota nota) {
        notas.add(nota); // Adiciona a nota na lista
        salvarDados(); // Salva tudo no computador
    }
    // Método: Mostra todas as notas na tela
    public void visualizarNotas() {
        System.out.println("\n--- Notas de " + nome + " ---");
        // Percorre todas as notas (começando do 1, não do 0)
        for (int i = 1; i < notas.size(); i++) {
            Nota nota = notas.get(i); // Pega a nota na posição i
            // Mostra informações básicas:
            System.out.println("[" + i + "] Título: " + nota.getTitulo());
            System.out.println("    Tipo: " + nota.getClass().getSimpleName());
            System.out.println("    Última atualização: " + nota.getDataAtualizacao());

            // Dependendo do tipo de nota, mostra informações específicas:
            if (nota instanceof NotaTexto) {
                NotaTexto notaTexto = (NotaTexto) nota;
                System.out.println("    Conteúdo: " + notaTexto.getTexto());
            } else if (nota instanceof Checklist) {
                Checklist checklist = (Checklist) nota;
                System.out.println("    Itens: " + checklist.getItens().size() + " itens");
            } else if (nota instanceof DataComemorativa) {
                DataComemorativa dataComem = (DataComemorativa) nota;
                System.out.println("    Ocasião: " + dataComem.getOcasiao());
                System.out.println("    Data do Evento: " + dataComem.getDataEventoFormatada()); // NOVA LINHA
            }
            System.out.println("------------------------");
        }
    }
    // Método: Excluir uma nota
    public void excluirNota(int indice) {
        // Verifica se o índice existe
        if (indice >= 1 && indice < notas.size()) {
            notas.remove(indice); // Remove a nota da lista
            salvarDados(); // Salva tudo no computador
        }
    }

    // Getter para a lista de notas
    public List<Nota> getNotas() {
        return notas; // Devolve todas as notas
    }

    // Método para salvar dados em arquivo (Importante)
    public void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("notas_" + senha + ".dat"))) {
            oos.writeObject(this); // Escreve este usuário no arquivo
            System.out.println("\nDados salvos automaticamente!\n");
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    // Método estático para carregar dados do arquivo
    public static Usuario carregarDados(String senha) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("notas_" + senha + ".dat"))) {
            return (Usuario) ois.readObject(); // Lê o usuário do arquivo
        } catch (FileNotFoundException e) {
        // Se não encontrar arquivo, é novo usuário
            System.out.println("Nenhum dado anterior encontrado. Criando novo usuário.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
            return null;
        }
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
