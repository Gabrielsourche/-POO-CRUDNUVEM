package Senac_Produto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

// Aluno: Gabriel Pereira da silva
// RA: 0025613
// Atividade: POO/CRUDNUVEM (RAILWAY)
public class Produto2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String url = "mysql://root:QWpxNnwGWZoIyevGeaLcLtrTQJgsWMWr@altaria.proxy.rlwy.net:46114/railway";
        String usuario = "root";
        String senha = "GjwVHeUmloQCOqFSxtIEfJEjXvjDYPdF";

        try {

            Connection conexao = DriverManager.getConnection(
                    url,
                    usuario,
                    senha
            );


            Statement criacaoBanco = conexao.createStatement();

            criacaoBanco.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS DBPRODUTO"
            );

            conexao.setCatalog("DBPRODUTO");

            criacaoBanco.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS produto (" +
                    "codigo INT, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "preco DECIMAL(10,2) NOT NULL" +
                    ")"
            );


            int opcao = -1;

            while (opcao != 0) {

                System.out.println(
                        "1 - Inserir | " +
                        "2 - Listar | " +
                        "3 - Atualizar | " +
                        "4 - Deletar | " +
                        "0 - Sair"
                );

                opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {

                    case 1:

                        System.out.print("Nome do produto : ");
                        String nome = sc.nextLine();

                        System.out.print("Código do produto : ");
                        int codigo = sc.nextInt();

                        System.out.print("Preço do produto : ");
                        float preco = sc.nextFloat();
                        sc.nextLine();

                        String sql =
                                "INSERT INTO produto" +
                                "(nome,codigo,preco) " +
                                "VALUES (?,?,?)";

                        PreparedStatement stmt =
                                conexao.prepareStatement(sql);

                        stmt.setString(1, nome);
                        stmt.setInt(2, codigo);
                        stmt.setFloat(3, preco);

                        stmt.executeUpdate();

                        System.out.println("Produto inserido com sucesso!!!");

                        break;

                    case 2:

                        System.out.println("Digite o código do produto : ");
                        int codigo2 = sc.nextInt();

                        String sql2 =
                                "SELECT * FROM produto " +
                                "WHERE codigo = ?";

                        PreparedStatement stmt2 =
                                conexao.prepareStatement(sql2);

                        stmt2.setInt(1, codigo2);

                        ResultSet rs =
                                stmt2.executeQuery();

                        while (rs.next()) {

                            System.out.println(
                                    "Nome Produto: "
                                    + rs.getString("Nome")
                                    + " | Preço Produto: "
                                    + rs.getFloat("Preco")
                                    + " | Código Produto: "
                                    + rs.getInt("Codigo")
                            );
                        }

                        break;

                    case 3:

                        System.out.println(
                                "Digite o código do Produto " +
                                "que deseja atualizar o preço : "
                        );

                        int codigobusca = sc.nextInt();

                        System.out.println("Digite o NOVO PREÇO : ");
                        float novopreco = sc.nextFloat();

                        String sqlupd =
                                "UPDATE produto " +
                                "SET Preco = ? " +
                                "WHERE Codigo = ?";

                        PreparedStatement stmtUpd =
                                conexao.prepareStatement(sqlupd);

                        stmtUpd.setFloat(1, novopreco);
                        stmtUpd.setInt(2, codigobusca);

                        int linhasAfetadas =
                                stmtUpd.executeUpdate();

                        if (linhasAfetadas > 0) {

                            System.out.println("Preço Atualizado!!!");

                        } else {

                            System.out.println("Produto não encontrado!!!");
                        }

                        break;

                    case 4:

                        System.out.println(
                                "Digite o Código do Produto " +
                                "que deseja deletar"
                        );

                        int codigoDel = sc.nextInt();

                        String sqlDel =
                                "DELETE FROM produto " +
                                "WHERE Codigo LIKE ?";

                        PreparedStatement stmtDel =
                                conexao.prepareStatement(sqlDel);

                        stmtDel.setString(
                                1,
                                "%" + codigoDel + "%"
                        );

                        stmtDel.executeUpdate();

                        System.out.println("Produto removido!");

                        break;

                    case 0:

                        System.out.println("Programa Finalizado!");

                        break;

                    default:

                        System.out.println("Opção inválida!");
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro de Cadastro "
                    + e.getMessage()
            );
        }
    }
}
