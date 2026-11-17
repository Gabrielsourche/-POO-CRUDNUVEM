
package POO;

import java.sql.*;
import java.util.Scanner;

public class CRUDNUVEM {

    // ALUNO: Gabriel Pereira da Silva
    // RA: 0025613

    static Scanner entrada = new Scanner(System.in);

    static Connection conectar() {

        try {

            String url = System.getenv("mysql://root:ZicmTIAVgMCdMjNnCqwjWYPfOosLwhCA@mysql.railway.internal:3306/railway");
            String usuario = System.getenv("root");
            String senha = System.getenv("ZicmTIAVgMCdMjNnCqwjWYPfOosLwhCA");

            Connection conexao = DriverManager.getConnection(
                url, usuario, senha
            );

            System.out.println("Banco conectado com sucesso!");

            return conexao;

        } catch (SQLException e) {

            System.out.println("Erro na conexão: " + e.getMessage());
            return null;
        }
    }

    static void criarTabela(Connection conexao) {

        String sql = """
            CREATE TABLE IF NOT EXISTS produtos (
                codigo VARCHAR(30) PRIMARY KEY,
                nome VARCHAR(100) NOT NULL,
                preco DECIMAL(10,2) NOT NULL
            )
        """;

        try (Statement stmt = conexao.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

    static void inserir(Connection conexao) {

        String sql = "INSERT INTO produtos VALUES (?, ?, ?)";

        try (PreparedStatement stmt =
                conexao.prepareStatement(sql)) {

            System.out.print("Código: ");
            String codigo = entrada.nextLine();

            System.out.print("Nome: ");
            String nome = entrada.nextLine();

            System.out.print("Preço: ");
            double preco = Double.parseDouble(
                entrada.nextLine().replace(",", ".")
            );

            stmt.setString(1, codigo);
            stmt.setString(2, nome);
            stmt.setDouble(3, preco);

            stmt.executeUpdate();

            System.out.println("Produto cadastrado!");

        } catch (Exception e) {

            System.out.println("Erro: " + e.getMessage());
        }
    }

    static void listar(Connection conexao) {

        String sql = "SELECT * FROM produtos ORDER BY codigo";

        try (Statement stmt = conexao.createStatement();
             ResultSet resultado = stmt.executeQuery(sql)) {

            System.out.println("\n--- PRODUTOS ---");

            while (resultado.next()) {

                System.out.println(
                    "Código: " + resultado.getString("codigo")
                );

                System.out.println(
                    "Nome: " + resultado.getString("nome")
                );

                System.out.println(
                    "Preço: " + resultado.getBigDecimal("preco")
                );

                System.out.println("----------------");
            }

        } catch (SQLException e) {

            System.out.println("Erro: " + e.getMessage());
        }
    }

    static void atualizar(Connection conexao) {

        String sql =
            "UPDATE produtos SET nome = ?, preco = ? WHERE codigo = ?";

        try (PreparedStatement stmt =
                conexao.prepareStatement(sql)) {

            System.out.print("Código do produto: ");
            String codigo = entrada.nextLine();

            System.out.print("Novo nome: ");
            String nome = entrada.nextLine();

            System.out.print("Novo preço: ");
            double preco = Double.parseDouble(
                entrada.nextLine().replace(",", ".")
            );

            stmt.setString(1, nome);
            stmt.setDouble(2, preco);
            stmt.setString(3, codigo);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Produto atualizado!");
            } else {
                System.out.println("Produto não encontrado!");
            }

        } catch (Exception e) {

            System.out.println("Erro: " + e.getMessage());
        }
    }

    static void excluir(Connection conexao) {

        String sql = "DELETE FROM produtos WHERE codigo = ?";

        try (PreparedStatement stmt =
                conexao.prepareStatement(sql)) {

            System.out.print("Código do produto: ");
            String codigo = entrada.nextLine();

            stmt.setString(1, codigo);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Produto excluído!");
            } else {
                System.out.println("Produto não encontrado!");
            }

        } catch (SQLException e) {

            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        Connection conexao = conectar();

        if (conexao == null) {
            return;
        }

        criarTabela(conexao);

        int opcao;

        do {

            System.out.println("\n===== CRUDNUVEM =====");
            System.out.println("1 - Inserir produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Atualizar produto");
            System.out.println("4 - Excluir produto");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(entrada.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {

                case 1:
                    inserir(conexao);
                    break;

                case 2:
                    listar(conexao);
                    break;

                case 3:
                    atualizar(conexao);
                    break;

                case 4:
                    excluir(conexao);
                    break;

                case 0:
                    System.out.println("Encerrando...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        try {
            conexao.close();
            entrada.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
