// Importações:
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

// Classe Principal
public class SistemaNotasTerminal {
    // Variáveis globais
    private static Usuario usuarioAtual; // O usuário que está usando AGORA
    private static Scanner scanner = new Scanner(System.in); // 'leitor' do programa

    public static void main(String[] args) { // Método principal
        inicializarSistema(); // Primeiro: configura tudo
        exibirMenuPrincipal(); // Depois: mostra o menu principal
    }

    // Método para dizer bom dia, boa atrde ou boa noite
    private static String getSaudacao() {
        Calendar calendario = Calendar.getInstance(); // Pega o relógio do computador
        int hora = calendario.get(Calendar.HOUR_OF_DAY); // Pega a hora atual
        
        // Decidir qual saudação usar:
        if (hora >= 5 && hora < 12) {
            return "Bom dia";
        } else if (hora >= 12 && hora < 18) {
            return "Boa tarde";
        } else {
            return "Boa noite";
        }
    }
    // Método para configurar o sistema quando liga
    private static void inicializarSistema() {
        String saudacao = getSaudacao(); // Pega a saudação certa
        // Tela de boas-vindas:
        System.out.println("\n=== " + saudacao + "! BEM-VINDO AO NOPAPER! ===");
        System.out.print("Digite sua senha (ou crie uma nova): ");
        String senha = scanner.nextLine(); // Lê o que usuário digitou
        
// Carregar dados existentes (se tiver)
        usuarioAtual = Usuario.carregarDados(senha);
        
        if (usuarioAtual == null) {

// Se não existir, criar novo usuário
            System.out.print("Digite seu nome: ");
            String nome = scanner.nextLine(); // Pede o nome
            usuarioAtual = new Usuario(nome, senha); // Cria usuário novo
            System.out.println("\nNovo usuário criado! " + saudacao + ", " + nome + "!");
        }
        else { // Se encontrou usuário (já existia):
            System.out.println("\nDados carregados! " + saudacao + ", " + usuarioAtual.getNome() + "!");
            System.out.println("Você tem " + usuarioAtual.getNotas().size() + " notas salvas.");
        }
    }
    // Método que mostra o meno principal (Importante)
    private static void exibirMenuPrincipal() {
        while (true) { // Loop infinito (fica mostrando menu até usuário sair):
            System.out.println("\n=== MENU PRINCIPAL ===\n");
            System.out.println("1. Criar nova anotação");
            System.out.println("2. Visualizar todas as notas");
            System.out.println("3. Excluir nota");
            System.out.println("4. Gerenciar checklist");
            System.out.println("5. Salvar dados manualmente");
            System.out.println("6. Sair\n");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();
            // Dependendo da opção, faz algo diferente:
            switch (opcao) {
                case 1:
                    criarNovaNota(); // Vai para criar nota
                    break;
                case 2:
                    visualizarNotasDetalhadas(); // Vai para ver notas
                    break;
                case 3:
                    excluirNota(); // Vai para excluir nota
                    break;
                case 4:
                    gerenciarChecklist(); // Vai para gerenciar listas
                    break;
                case 5:
                    usuarioAtual.salvarDados(); // Salva tudo
                    break;
                case 6:
                    // Sai do programa:
                    System.out.println("Salvando dados...");
                    usuarioAtual.salvarDados(); // Salva antes de sair
                    System.out.println("Saindo do sistema... Até logo!");
                    return; // Para o loop e sai do método
                default:
                    System.out.println("Opção inválida! Digite 1, 2, 3, 4, 5, ou 6.");
            }
        }
    }
    // Método para criar nova nota
    private static void criarNovaNota() {
        System.out.println("\n=== CRIAR NOVA ANOTAÇÃO ===\n");
        System.out.println("1. Nota de Texto Simples");
        System.out.println("2. Checklist");
        System.out.println("3. Data Comemorativa\n");
        System.out.print("Escolha o tipo de nota: ");

        int tipo = lerInteiro(); // Lê o tipo de nota

        System.out.print("Título da nota: ");
        String titulo = scanner.nextLine(); // Lê o título
        // Dependendo do tipo, cria nota diferente:
        switch (tipo) {
            case 1: // Nota de texto
                System.out.print("Conteúdo da nota: ");
                String texto = scanner.nextLine();
                NotaTexto notaTexto = new NotaTexto(titulo, texto);
                usuarioAtual.criarNota(notaTexto);
                System.out.println(" Nota de texto criada com sucesso!");
                break;

            case 2: // Checklist
                Checklist checklist = new Checklist(titulo); // Cria checklist
                System.out.println("Adicionando itens ao checklist (digite 'fim' para parar):");
                
                while (true) { // Loop para adicionar vários itens:
                    System.out.print("Digite um item: ");
                    String item = scanner.nextLine();
                    if (item.equalsIgnoreCase("fim")) break; // Se digitar fim, encerra o loop
                    checklist.adicionarItem(item);
                }
                usuarioAtual.criarNota(checklist);
                System.out.println(" Checklist criado com sucesso!");
                break;

            case 3: // Data comemorativa
                System.out.print("Descreva a ocasião: ");
                String ocasiao = scanner.nextLine();
                
                // Pede data até usuário digitar uma válida:
                Date dataEvento = null;
                while (dataEvento == null) {
                    System.out.print("Digite a data do evento (DD/MM/AAAA): ");
                    String dataStr = scanner.nextLine();
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        sdf.setLenient(false); // Não permitir datas inválidas
                        dataEvento = sdf.parse(dataStr); // Converte texto para data
                    } catch (ParseException e) {
                        System.out.println("Data inválida! Use o formato DD/MM/AAAA.");
                    }
                }
                
                DataComemorativa dataComem = new DataComemorativa(titulo, ocasiao, dataEvento);
                usuarioAtual.criarNota(dataComem);
                System.out.println(" Data comemorativa criada com sucesso!");
                break;

            default:
                System.out.println("Tipo de nota inválido!");
        }
    }
    // Método para mostrar todas as notas com detalhes
    private static void visualizarNotasDetalhadas() {
        System.out.println("\n=== SUAS NOTAS ===");
        
        List<Nota> notas = usuarioAtual.getNotas(); // Pega todas as notas
        // Se não tem notas:
        if (notas.isEmpty()) {
            System.out.println("Nenhuma nota encontrada.");
            return; // Volta ao menu
        }
        // Percorre todas as notas e mostra cada uma:
        for (int i = 0; i < notas.size(); i++) {
            Nota nota = notas.get(i); // Pega a nota na posição i
            // Informações básicas:
            System.out.println("\n[" + i + "] " + nota.getTitulo());
            System.out.println("Tipo: " + nota.getClass().getSimpleName());
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            System.out.println("Criada em: " + sdf.format(nota.getDataCriacao()));
            System.out.println("Atualizada em: " + sdf.format(nota.getDataAtualizacao()));
          
            // Informações específicas por tipo:
            if (nota instanceof NotaTexto) {
                NotaTexto notaTexto = (NotaTexto) nota;
                System.out.println("Conteúdo: " + notaTexto.getTexto());
            } else if (nota instanceof Checklist) {
                Checklist checklist = (Checklist) nota;
                System.out.println("Itens:");
                List<ItemChecklist> itens = checklist.getItens();
                for (int j = 0; j < itens.size(); j++) { // Mostra cada item com seu status:
                    ItemChecklist item = itens.get(j);
                    String status = item.isConclusao() ? "[]" : "[ ]";
                    System.out.println("  " + j + ". " + status + " " + item.getDescricao());
                }
            } else if (nota instanceof DataComemorativa) {
                DataComemorativa dataComem = (DataComemorativa) nota;
                System.out.println("Ocasião: " + dataComem.getOcasiao());
                System.out.println("Data do Evento: " + dataComem.getDataEventoFormatada());
            }
            System.out.println("---");
        }
    }
    // Método para excluir uma nota
    private static void excluirNota() {
        List<Nota> notas = usuarioAtual.getNotas(); // Pega todas as notas
        // Se não tem notas:
        if (notas.isEmpty()) {
            System.out.println("Não há notas para excluir.");
            return; // Volta ao menu
        }
        // Mostra todas as notas com números:
        System.out.println("\n=== EXCLUIR NOTA ===\n");
        for (int i = 0; i < notas.size(); i++) {
            System.out.println("[" + i + "] " + notas.get(i).getTitulo());
        }
        // Pede qual nota excluir:
        System.out.print("Digite o número da nota a ser excluída: ");
        int indice = lerInteiro();
        // Se número é válido:
        if (indice >= 0 && indice < notas.size()) {
            usuarioAtual.excluirNota(indice);
            System.out.println(" Nota excluída com sucesso!");
        } else {
            System.out.println("Número de nota inválido!");
        }
    }
    // Método para Gerencia checklists
    private static void gerenciarChecklist() {
        System.out.println("\n=== GERENCIAR CHECKLIST ===\n");
        
        List<Nota> notas = usuarioAtual.getNotas(); // Pega todas as notas

// Encontrar checklists
        List<Checklist> checklists = new ArrayList<>();
        for (Nota nota : notas) {
            if (nota instanceof Checklist) {
                checklists.add((Checklist) nota); // Adiciona à lista de checklists
            }
        }
        // Se não tem checklists:
        if (checklists.isEmpty()) {
            System.out.println("Nenhum checklist encontrado.");
            return; // Volta ao menu
        }

// Listar checklists
        System.out.println("Seus checklists:");
        for (int i = 0; i < checklists.size(); i++) {
            System.out.println(i + ". " + checklists.get(i).getTitulo());
        }
        // Pede qual checklist mexer:
        System.out.print("Escolha um checklist: ");
        int checklistIndex = lerInteiro();
        // Se número é válido:
        if (checklistIndex >= 0 && checklistIndex < checklists.size()) {
            Checklist checklist = checklists.get(checklistIndex); // Pega o checklist
            gerenciarChecklistIndividual(checklist);  // Vai gerenciar este checklist
        } else {
            System.out.println("Checklist inválido!");
        }
    }
    // Método para Gerenciar UM checklist específico
    private static void gerenciarChecklistIndividual(Checklist checklist) {
        while (true) { // Loop para ficar mexendo neste checklist até usuário voltar:
            System.out.println("\n=== " + checklist.getTitulo() + " ===\n");
            List<ItemChecklist> itens = checklist.getItens(); // Pega os itens
            // Se não tem itens:
            if (itens.isEmpty()) {
                System.out.println("Nenhum item neste checklist.");
            } else { // Se tem itens, mostra todos:
                for (int i = 0; i < itens.size(); i++) {
                    ItemChecklist item = itens.get(i);
                    String status = item.isConclusao() ? "[X]" : "[ ]"; // [X]=feito, [ ]=não
                    System.out.println(i + ". " + status + " " + item.getDescricao());
                }
            }
            // Menu do checklist:
            System.out.println("\n1. Marcar item");
            System.out.println("2. Desmarcar item");
            System.out.println("3. Adicionar item");
            System.out.println("4. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) { // Dependendo da opção:
                case 1: // Marcar item como feito
                    if (!itens.isEmpty()) {
                        System.out.print("Digite o número do item a marcar: ");
                        int itemMarcar = lerInteiro();
                        if (itemMarcar >= 0 && itemMarcar < itens.size()) {
                            checklist.marcarItem(itemMarcar); // Marca
                            usuarioAtual.salvarDados(); // Salva após modificar
                            System.out.println("Item marcado!");
                        } else {
                            System.out.println("Número de item inválido!");
                        }
                    } else {
                        System.out.println("Não há itens para marcar.");
                    }
                    break;
                case 2: // Desmarcar item
                    if (!itens.isEmpty()) {
                        System.out.print("Digite o número do item a desmarcar: ");
                        int itemDesmarcar = lerInteiro();
                        if (itemDesmarcar >= 0 && itemDesmarcar < itens.size()) {
                            checklist.desmarcarItem(itemDesmarcar); // Desmarca
                            usuarioAtual.salvarDados(); // Salva após modificar
                            System.out.println("Item desmarcado!");
                        } else {
                            System.out.println("Número de item inválido!");
                        }
                    } else {
                        System.out.println("Não há itens para desmarcar.");
                    }
                    break;
                case 3: // Adicionar novo item
                    System.out.print("Digite o novo item: ");
                    String novoItem = scanner.nextLine(); // Lê novo item
                    checklist.adicionarItem(novoItem); // Adiciona
                    usuarioAtual.salvarDados(); // Salva após adicionar
                    System.out.println("Item adicionado!");
                    break;
                case 4: // Voltar ao menu anterior
                    return; // Sai deste método (volta para gerenciarChecklist)
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    // MÉTODO AUXILIAR: Lê um número inteiro (evita erros se digitar letras)
    private static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Por favor, digite um número válido: ");
            }
        }
    }
}